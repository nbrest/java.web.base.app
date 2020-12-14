package com.nicobrest.kamehouse.testmodule.controller;

import com.nicobrest.kamehouse.main.controller.AbstractController;
import com.nicobrest.kamehouse.main.model.KameHouseGenericResponse;
import com.nicobrest.kamehouse.testmodule.service.TestSchedulerService;
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
 * Controller class for the test scheduler commands.
 * 
 * @author nbrest
 *
 */
@Controller
@RequestMapping(value = "/api/v1/test-module/test-scheduler")
public class TestSchedulerController extends AbstractController {

  private static final String BASE_URL = "/api/v1/test-module/test-scheduler";

  @Autowired
  TestSchedulerService testSchedulerService;

  /**
   * Schedules the sample job at the specified delay, or at a fixed schedule, if no delay specified.
   */
  @PostMapping(path = "/sample-job")
  @ResponseBody
  public ResponseEntity<KameHouseGenericResponse>
      setShutdown(@RequestParam(value = "delay", required = false) Integer delay) {
    logger.trace("{}/sample-job?delay=[delay] (POST)", BASE_URL);
    testSchedulerService.scheduleSampleJob(delay);
    KameHouseGenericResponse response = new KameHouseGenericResponse();
    if (delay != null) {
      response.setMessage("Scheduled sample job at the specified delay of " + delay + " seconds");
    } else {
      response.setMessage("Scheduled sample job at a fixed delay.");
    }
    return generatePostResponseEntity(response);
  }

  /**
   * Gets the status of a sample-job command.
   */
  @GetMapping(path = "/sample-job")
  @ResponseBody
  public ResponseEntity<KameHouseGenericResponse> statusShutdown() {
    logger.trace("{}/sample-job (GET)", BASE_URL);
    String suspendStatus = testSchedulerService.getSampleJobStatus();
    KameHouseGenericResponse response = new KameHouseGenericResponse();
    response.setMessage(suspendStatus);
    return generateGetResponseEntity(response);
  }

  /**
   * Cancels a sample-job scheduled command.
   */
  @DeleteMapping(path = "/sample-job")
  @ResponseBody
  public ResponseEntity<KameHouseGenericResponse> cancelShutdown() {
    logger.trace("{}/sample-job (DELETE)", BASE_URL);
    String cancelSuspendStatus = testSchedulerService.cancelScheduledSampleJob();
    KameHouseGenericResponse response = new KameHouseGenericResponse();
    response.setMessage(cancelSuspendStatus);
    return generateGetResponseEntity(response);
  }
}
