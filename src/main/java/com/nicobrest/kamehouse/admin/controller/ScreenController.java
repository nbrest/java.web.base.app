package com.nicobrest.kamehouse.admin.controller;

import com.nicobrest.kamehouse.admin.model.SystemCommandOutput;
import com.nicobrest.kamehouse.admin.model.admincommand.ScreenLockAdminCommand;
import com.nicobrest.kamehouse.admin.model.admincommand.ScreenUnlockAdminCommand;
import com.nicobrest.kamehouse.admin.model.admincommand.ScreenWakeUpAdminCommand;

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

  /**
   * Lock screen in the server running the application.
   */
  @PostMapping(path = "/lock")
  @ResponseBody
  public ResponseEntity<List<SystemCommandOutput>> lockScreen() {
    logger.trace("/api/v1/admin/screen/lock (POST)");
    return executeAdminCommand(new ScreenLockAdminCommand());
  }

  /**
   * Unlock screen in the server running the application.
   */
  @PostMapping(path = "/unlock")
  @ResponseBody
  public ResponseEntity<List<SystemCommandOutput>> unlockScreen() {
    logger.trace("/api/v1/admin/screen/unlock (POST)");
    return executeAdminCommand(new ScreenUnlockAdminCommand());
  }

  /**
   * Wake up the screen. Run it when the screen goes dark after being idle for a
   * while.
   */
  @PostMapping(path = "/wake-up")
  @ResponseBody
  public ResponseEntity<List<SystemCommandOutput>> wakeUpScreen() {
    logger.trace("/api/v1/admin/screen/wake-up (POST)");
    return executeAdminCommand(new ScreenWakeUpAdminCommand());
  }
}