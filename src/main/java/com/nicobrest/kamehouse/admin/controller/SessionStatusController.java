package com.nicobrest.kamehouse.admin.controller;

import com.nicobrest.kamehouse.admin.service.SessionStatusService;
import com.nicobrest.kamehouse.main.controller.AbstractController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Controller to obtain current session information.
 * 
 * @author nbrest
 *
 */
@Controller
@RequestMapping(value = "/api/v1/session")
public class SessionStatusController extends AbstractController {

  @Autowired
  private SessionStatusService sessionStatusService;

  /**
   * Returns the current session's status.
   */
  @GetMapping(path = "/status")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> getSessionStatus() {
    logger.trace("In controller /api/v1/session/status (GET)");
    Map<String, Object> sessionStatus = sessionStatusService.getSessionStatus();
    return generateGetResponseEntity(sessionStatus);
  }
}
