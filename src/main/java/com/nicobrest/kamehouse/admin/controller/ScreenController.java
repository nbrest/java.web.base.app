package com.nicobrest.kamehouse.admin.controller;

import com.nicobrest.kamehouse.admin.model.ScreenLockAdminCommand;
import com.nicobrest.kamehouse.admin.model.ScreenUnlockAdminCommand;
import com.nicobrest.kamehouse.admin.model.ScreenWakeUpAdminCommand;
import com.nicobrest.kamehouse.admin.service.AdminCommandService;
import com.nicobrest.kamehouse.systemcommand.controller.AbstractSystemCommandController;
import com.nicobrest.kamehouse.systemcommand.model.SystemCommandOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller to execute commands to control the screen.
 * 
 * @author nbrest
 *
 */
@Controller
@RequestMapping(value = "/api/v1/admin/screen")
public class ScreenController extends AbstractSystemCommandController {

  @Autowired
  private AdminCommandService adminCommandService;

  /**
   * Lock screen in the server running the application.
   */
  @PostMapping(path = "/lock")
  @ResponseBody
  public ResponseEntity<List<SystemCommandOutput>> lockScreen() {
    logger.trace("/api/v1/admin/screen/lock (POST)");
    return executeAdminCommand(adminCommandService, new ScreenLockAdminCommand());
  }

  /**
   * Unlock screen in the server running the application.
   */
  @PostMapping(path = "/unlock")
  @ResponseBody
  public ResponseEntity<List<SystemCommandOutput>> unlockScreen() {
    logger.trace("/api/v1/admin/screen/unlock (POST)");
    return executeAdminCommand(adminCommandService, new ScreenUnlockAdminCommand());
  }

  /**
   * Wake up the screen. Run it when the screen goes dark after being idle for a
   * while.
   */
  @PostMapping(path = "/wake-up")
  @ResponseBody
  public ResponseEntity<List<SystemCommandOutput>> wakeUpScreen() {
    logger.trace("/api/v1/admin/screen/wake-up (POST)");
    return executeAdminCommand(adminCommandService, new ScreenWakeUpAdminCommand());
  }
}