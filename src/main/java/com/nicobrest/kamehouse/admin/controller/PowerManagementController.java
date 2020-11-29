package com.nicobrest.kamehouse.admin.controller;
 
import com.nicobrest.kamehouse.admin.model.admincommand.ShutdownAdminCommand;
import com.nicobrest.kamehouse.admin.model.admincommand.ShutdownCancelAdminCommand;
import com.nicobrest.kamehouse.admin.model.admincommand.ShutdownStatusAdminCommand;
import com.nicobrest.kamehouse.admin.model.admincommand.SuspendAdminCommand;
import com.nicobrest.kamehouse.admin.model.systemcommand.SystemCommand;

import com.nicobrest.kamehouse.admin.service.PowerManagementService;
import com.nicobrest.kamehouse.main.exception.KameHouseBadRequestException;
import com.nicobrest.kamehouse.main.model.KameHouseGenericResponse;
import com.nicobrest.kamehouse.main.utils.DateUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Controller class for the shutdown commands.
 * 
 * @author nbrest
 *
 */
@Controller
@RequestMapping(value = "/api/v1/admin/power-management")
public class PowerManagementController extends AbstractSystemCommandController {

  private static final String BASE_URL = "/api/v1/admin/power-management";

  @Autowired
  PowerManagementService powerManagementService;

  /**
   * Shutdowns the local server with the specified delay in seconds.
   */
  @PostMapping(path = "/shutdown")
  @ResponseBody
  public ResponseEntity<List<SystemCommand.Output>>
      setShutdown(@RequestParam(value = "delay", required = true) Integer delay) {
    logger.trace("{}/shutdown?delay={} (POST)", BASE_URL, delay);
    return execAdminCommand(new ShutdownAdminCommand(delay));
  }

  /**
   * Cancels a shutdown command.
   */
  @DeleteMapping(path = "/shutdown")
  @ResponseBody
  public ResponseEntity<List<SystemCommand.Output>> cancelShutdown() {
    logger.trace("{}/shutdown (DELETE)", BASE_URL);
    return execAdminCommand(new ShutdownCancelAdminCommand());
  }

  /**
   * Gets the status of a shutdown command.
   */
  @GetMapping(path = "/shutdown")
  @ResponseBody
  public ResponseEntity<List<SystemCommand.Output>> statusShutdown() {
    logger.trace("{}/shutdown (GET)", BASE_URL);
    return execAdminCommand(new ShutdownStatusAdminCommand());
  }

  /**
   * Schedule a job to suspend the server. Executed through a scheduled job because the
   * suspend command doesn't natively support scheduling/delay in windows.
   */
  @PostMapping(path = "/suspend")
  @ResponseBody
  public ResponseEntity<KameHouseGenericResponse>
      setSuspend(@RequestParam(value = "delay", required = true) Integer delay) {
    logger.trace("{}/suspend?delay={} (POST)", BASE_URL, delay);
    powerManagementService.scheduleSuspend(delay);
    KameHouseGenericResponse response = new KameHouseGenericResponse();
    response.setMessage("Scheduled suspend at the specified delay of " + delay + " seconds");
    return generatePostResponseEntity(response);
  }

  /**
   * Gets the status of a scheduled suspend.
   */
  @GetMapping(path = "/suspend")
  @ResponseBody
  public ResponseEntity<KameHouseGenericResponse> getSuspend() {
    logger.trace("{}/suspend (GET)", BASE_URL);
    String suspendStatus = powerManagementService.getSuspendStatus();
    KameHouseGenericResponse response = new KameHouseGenericResponse();
    response.setMessage(suspendStatus);
    return generateGetResponseEntity(response);
  }

  /**
   * Cancel a scheduled suspend.
   */
  @DeleteMapping(path = "/suspend")
  @ResponseBody
  public ResponseEntity<KameHouseGenericResponse> cancelSuspend() {
    logger.trace("{}/suspend (DELETE)", BASE_URL);
    String cancelSuspendStatus = powerManagementService.cancelScheduledSuspend();
    KameHouseGenericResponse response = new KameHouseGenericResponse();
    response.setMessage(cancelSuspendStatus);
    return generateGetResponseEntity(response);
  }

  /**
   * Wake on lan the specified server or mac address.
   */
  @PostMapping(path = "/wol")
  @ResponseBody
  public ResponseEntity<Void> wakeOnLan(
      @RequestParam(value = "server", required = false) String server,
      @RequestParam(value = "mac", required = false) String mac,
      @RequestParam(value = "broadcast", required = false) String broadcast) {
    if (server != null) {
      logger.trace("{}/wol?server={} (POST)", BASE_URL, server);
      powerManagementService.wakeOnLan(server);
    } else if (mac != null && broadcast != null) {
      logger.trace("{}/wol?mac={}&broadcast={} (POST)", BASE_URL, mac, broadcast);
      powerManagementService.wakeOnLan(mac, broadcast);
    } else {
      throw new KameHouseBadRequestException("server OR mac and broadcast parameters are required");
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
