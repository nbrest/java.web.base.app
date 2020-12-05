package com.nicobrest.kamehouse.admin.controller;

import com.nicobrest.kamehouse.admin.model.JobSchedule;
import com.nicobrest.kamehouse.admin.service.SchedulerService;
import com.nicobrest.kamehouse.main.controller.AbstractController;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller class for the scheduler commands.
 * 
 * @author nbrest
 *
 */
@Controller
@RequestMapping(value = "/api/v1/admin/scheduler")
public class SchedulerController extends AbstractController {

  private static final String BASE_URL = "/api/v1/admin/scheduler";

  @Autowired
  SchedulerService schedulerService;

  /**
   * Gets the status of all jobs in the system.
   */
  @GetMapping(path = "/jobs")
  @ResponseBody
  public ResponseEntity<List<JobSchedule>> getAllJobs() {
    logger.trace("{}/jobs (GET)", BASE_URL);
    List<JobSchedule> jobs = schedulerService.getAllJobsStatus();
    return generateGetResponseEntity(jobs);
  }

  /**
   * Cancel the execution of the specified job.
   */
  @DeleteMapping(path = "/jobs")
  @ResponseBody
  public ResponseEntity<List<JobSchedule>> cancelJob(
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "group", required = true) String group) {
    logger.trace("{}/jobs?name={}&group={} (DELETE)", BASE_URL, name, group);
    JobKey jobKey = new JobKey(name, group);
    schedulerService.cancelScheduledJob(jobKey);
    List<JobSchedule> jobs = schedulerService.getAllJobsStatus();
    return generateGetResponseEntity(jobs);
  }
}
