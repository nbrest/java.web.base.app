package com.nicobrest.kamehouse.vlcrc.controller;

import com.nicobrest.kamehouse.commons.controller.AbstractSystemCommandController;
import com.nicobrest.kamehouse.commons.model.systemcommand.SystemCommand;
import com.nicobrest.kamehouse.vlcrc.model.kamehousecommand.VlcStartKameHouseSystemCommand;
import com.nicobrest.kamehouse.vlcrc.model.kamehousecommand.VlcStatusKameHouseSystemCommand;
import com.nicobrest.kamehouse.vlcrc.model.kamehousecommand.VlcStopKameHouseSystemCommand;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller class to start, stop and get the status of a local VLC player.
 * 
 * @author nbrest
 *
 */
@Controller
@RequestMapping(value = "/api/v1/vlc-rc")
public class VlcController extends AbstractSystemCommandController {

  /**
   * Starts a vlc player in the local server.
   */
  @PostMapping(path = "/vlc")
  @ResponseBody
  public ResponseEntity<List<SystemCommand.Output>>
      startVlcPlayer(@RequestParam(value = "file", required = false) String file) {
    logger.trace("/api/v1/vlc-rc/vlc?file=[file] (POST)");
    return execKameHouseSystemCommand(new VlcStartKameHouseSystemCommand(file));
  }

  /**
   * Stops vlc player in the local server.
   */
  @DeleteMapping(path = "/vlc")
  @ResponseBody
  public ResponseEntity<List<SystemCommand.Output>> stopVlcPlayer() {
    logger.trace("/api/v1/vlc-rc/vlc (DELETE)");
    return execKameHouseSystemCommand(new VlcStopKameHouseSystemCommand());
  }

  /**
   * Gets the status of vlc player in the local server.
   */
  @GetMapping(path = "/vlc")
  @ResponseBody
  public ResponseEntity<List<SystemCommand.Output>> statusVlcPlayer() {
    logger.trace("/api/v1/vlc-rc/vlc (GET)");
    return execKameHouseSystemCommand(new VlcStatusKameHouseSystemCommand());
  }
}
