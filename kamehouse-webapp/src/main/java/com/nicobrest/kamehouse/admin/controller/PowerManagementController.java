package com.nicobrest.kamehouse.admin.controller;

import com.nicobrest.kamehouse.admin.service.PowerManagementService;
import com.nicobrest.kamehouse.commons.exception.KameHouseBadRequestException;
import com.nicobrest.kamehouse.commons.model.KameHouseGenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller class for the power management commands.
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
  public ResponseEntity<KameHouseGenericResponse>
      setShutdown(@RequestParam(value = "delay", required = true) Integer delay) {
    logger.trace("{}/shutdown (POST)", BASE_URL);
    powerManagementService.scheduleShutdown(delay);
    KameHouseGenericResponse response = new KameHouseGenericResponse();
    response.setMessage("Scheduled shutdown at the specified delay of " + delay + " seconds");
    return generatePostResponseEntity(response);
  }

  /**
   * Gets the status of a shutdown command.
   */
  @GetMapping(path = "/shutdown")
  @ResponseBody
  public ResponseEntity<KameHouseGenericResponse> statusShutdown() {
    logger.trace("{}/shutdown (GET)", BASE_URL);
    String suspendStatus = powerManagementService.getShutdownStatus();
    KameHouseGenericResponse response = new KameHouseGenericResponse();
    response.setMessage(suspendStatus);
    return generateGetResponseEntity(response);
  }

  /**
   * Cancels a shutdown command.
   */
  @DeleteMapping(path = "/shutdown")
  @ResponseBody
  public ResponseEntity<KameHouseGenericResponse> cancelShutdown() {
    logger.trace("{}/shutdown (DELETE)", BASE_URL);
    String cancelSuspendStatus = powerManagementService.cancelScheduledShutdown();
    KameHouseGenericResponse response = new KameHouseGenericResponse();
    response.setMessage(cancelSuspendStatus);
    return generateGetResponseEntity(response);
  }

  /**
   * Schedule a job to suspend the server. Executed through a scheduled job because the
   * suspend command doesn't natively support scheduling/delay in windows.
   */
  @PostMapping(path = "/suspend")
  @ResponseBody
  public ResponseEntity<KameHouseGenericResponse>
      setSuspend(@RequestParam(value = "delay", required = true) Integer delay) {
    logger.trace("{}/suspend?delay=[delay] (POST)", BASE_URL);
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
  public ResponseEntity<KameHouseGenericResponse> wakeOnLan(
      @RequestParam(value = "server", required = false) String server,
      @RequestParam(value = "mac", required = false) String mac,
      @RequestParam(value = "broadcast", required = false) String broadcast) {
    KameHouseGenericResponse response = new KameHouseGenericResponse();
    if (server != null) {
      logger.trace("{}/wol?server=[server] (POST)", BASE_URL);
      powerManagementService.wakeOnLan(server);
      response.setMessage("WOL packet sent to " + server);
    } else if (mac != null && broadcast != null) {
      logger.trace("{}/wol?mac=[mac]&broadcast=[broadcast] (POST)", BASE_URL);
      powerManagementService.wakeOnLan(mac, broadcast);
      response.setMessage("WOL packet sent to " + mac + " over " + broadcast);
    } else {
      throw new KameHouseBadRequestException("server OR mac and broadcast parameters are required");
    }
    return generatePostResponseEntity(response);
  }
}
