package com.nicobrest.kamehouse.vlcrc.controller;

import com.nicobrest.kamehouse.utils.ControllerUtils;
import com.nicobrest.kamehouse.vlcrc.model.VlcPlayer;
import com.nicobrest.kamehouse.vlcrc.model.VlcRcCommand;
import com.nicobrest.kamehouse.vlcrc.model.VlcRcStatus;
import com.nicobrest.kamehouse.vlcrc.service.VlcPlayerService;
import com.nicobrest.kamehouse.vlcrc.service.VlcRcService;
import com.nicobrest.kamehouse.vlcrc.service.dto.VlcPlayerDto;

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

  @Autowired
  private VlcPlayerService vlcPlayerService;

  /**
   * Creates a VLC Player.
   */
  @RequestMapping(value = "/players", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<Long> createVlcPlayer(@RequestBody VlcPlayerDto vlcPlayerDto) {

    logger.trace("In controller /vlc-rc/players (POST)");
    Long vlcPlayerId = vlcPlayerService.createVlcPlayer(vlcPlayerDto);
    return new ResponseEntity<Long>(vlcPlayerId, HttpStatus.CREATED);
  }

  /**
   * Gets all VLC Player registered in the application.
   */
  @RequestMapping(value = "/players", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> getAllVlcPlayers() {

    logger.trace("In controller /vlc-rc/players/ (GET)");
    List<VlcPlayer> vlcPlayers = vlcPlayerService.getAllVlcPlayers();
    return ControllerUtils.generateGetStandardResponseEntity(vlcPlayers);
  }

  /**
   * Gets the VLC Player passed as a URL parameter.
   */
  @RequestMapping(value = "/players/{vlcPlayerName}", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> getVlcPlayer(@PathVariable String vlcPlayerName) {

    logger.trace("In controller /vlc-rc/players/{vlcPlayerName} (GET)");
    VlcPlayer vlcPlayer = vlcPlayerService.getVlcPlayer(vlcPlayerName);
    return ControllerUtils.generateGetStandardResponseEntity(vlcPlayer);
  }

  /**
   * Updates the VLC Player passed as a URL parameter.
   */
  @RequestMapping(value = "/players/{vlcPlayerName}", method = RequestMethod.PUT)
  public ResponseEntity<?> updateVlcPlayer(@PathVariable String vlcPlayerName,
      @RequestBody VlcPlayerDto vlcPlayerDto) {

    //TODO: Move this to {id} and check that path id is the same as VlcPlayerDto id
    logger.trace("In controller /vlc-rc/players/{vlcPlayerName} (PUT)");
    vlcPlayerService.updateVlcPlayer(vlcPlayerDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Deletes the VLC Player passed as a URL parameter.
   */
  @RequestMapping(value = "/players/{vlcPlayerId}", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseEntity<VlcPlayer> deleteVlcPlayer(@PathVariable Long vlcPlayerId) {

    logger.trace("In controller /vlc-rc/players/{vlcPlayerId} (DELETE)");
    VlcPlayer vlcPlayer = vlcPlayerService.deleteVlcPlayer(vlcPlayerId);
    return new ResponseEntity<VlcPlayer>(vlcPlayer, HttpStatus.OK);
  }

  /**
   * Gets the status information of the VLC Player passed through the URL.
   */
  @RequestMapping(value = "/players/{vlcPlayerName}/status", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> getVlcRcStatus(@PathVariable String vlcPlayerName) {

    logger.trace("In controller /vlc-rc/players/{vlcPlayerName}/status (GET)");
    VlcRcStatus vlcRcStatus = vlcRcService.getVlcRcStatus(vlcPlayerName);
    return ControllerUtils.generateGetStandardResponseEntity(vlcRcStatus);
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
  public ResponseEntity<?> getPlaylist(@PathVariable String vlcPlayerName) {

    logger.trace("In controller /vlc-rc/players/{vlcPlayerName}/playlist (GET)");
    List<Map<String, Object>> vlcPlaylist = vlcRcService.getPlaylist(vlcPlayerName);
    return ControllerUtils.generateGetStandardResponseEntity(vlcPlaylist);
  }

  /**
   * Browse the VLC Player server's file system.
   */
  @RequestMapping(value = "/players/{vlcPlayerName}/browse", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> browse(
      @RequestParam(value = "uri", required = false) String uri,
      @PathVariable String vlcPlayerName) {

    logger.trace("In controller /vlc-rc/players/{vlcPlayerName}/browse (GET)");
    List<Map<String, Object>> vlcRcFileList = vlcRcService.browse(uri, vlcPlayerName);
    return ControllerUtils.generateGetStandardResponseEntity(vlcRcFileList);
  }
}
