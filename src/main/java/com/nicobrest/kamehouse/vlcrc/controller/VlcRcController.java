package com.nicobrest.kamehouse.vlcrc.controller;

import com.nicobrest.kamehouse.vlcrc.model.VlcRcCommand;
import com.nicobrest.kamehouse.vlcrc.model.VlcRcStatus;
import com.nicobrest.kamehouse.vlcrc.service.VlcRcService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Controller to manage the VLC Players registered in the application.
 * 
 * @author nbrest
 *
 */
@Controller
@RequestMapping(value = "/api/v1/vlc-rc")
public class VlcRcController {

  private static final Logger logger = LoggerFactory.getLogger(VlcRcController.class);

  @Autowired
  private VlcRcService vlcRcService;

  /**
   * Gets the status information of the VLC Player passed through the URL.
   */
  @RequestMapping(value = "/players/{vlcPlayerName}", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<VlcRcStatus> getVlcRcStatus(@PathVariable String vlcPlayerName) {

    logger.trace("In controller /vlc-rc/players/{vlcPlayerName} (GET)");
    VlcRcStatus vlcRcStatus = vlcRcService.getVlcRcStatus(vlcPlayerName);
    return new ResponseEntity<VlcRcStatus>(vlcRcStatus, HttpStatus.OK);
  }

  /**
   * Executes a command in the selected VLC Player.
   */
  @RequestMapping(value = "/players/{vlcPlayerName}/commands", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<VlcRcStatus> executeCommand(@RequestBody VlcRcCommand vlcRcCommand,
      @PathVariable String vlcPlayerName) {

    logger.trace("In controller /vlc-rc/players/{vlcPlayerName}/commands (POST)");
    VlcRcStatus vlcRcStatus = vlcRcService.execute(vlcRcCommand, vlcPlayerName);
    return new ResponseEntity<VlcRcStatus>(vlcRcStatus, HttpStatus.CREATED);
  }

  /**
   * Gets the current playlist from the selected VLC Player.
   */
  @RequestMapping(value = "/players/{vlcPlayerName}/playlist", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<List<Map<String, Object>>> getPlaylist(
      @PathVariable String vlcPlayerName) {

    logger.trace("In controller /vlc-rc/players/{vlcPlayerName}/playlist (GET)");
    List<Map<String, Object>> vlcPlaylist = vlcRcService.getPlaylist(vlcPlayerName);
    return new ResponseEntity<List<Map<String, Object>>>(vlcPlaylist, HttpStatus.OK);
  }

  /**
   * Browse the VLC Player server's file system.
   */
  @RequestMapping(value = "/players/{vlcPlayerName}/browse", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<List<Map<String, Object>>> browse(@RequestParam(value = "uri",
      required = false) String uri, @PathVariable String vlcPlayerName) {

    logger.trace("In controller /vlc-rc/players/{vlcPlayerName}/browse (GET)");
    List<Map<String, Object>> vlcRcFileList = vlcRcService.browse(uri, vlcPlayerName);
    return new ResponseEntity<List<Map<String, Object>>>(vlcRcFileList, HttpStatus.OK);
  }
}
