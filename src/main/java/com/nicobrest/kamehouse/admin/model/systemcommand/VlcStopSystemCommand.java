package com.nicobrest.kamehouse.admin.model.systemcommand;

import java.util.Arrays;

/**
 * System command to stop a vlc player.
 * 
 * @author nbrest
 *
 */
public class VlcStopSystemCommand extends SystemCommand {

  /**
   * Sets the command line for each operation system required for this SystemCommand.
   */
  public VlcStopSystemCommand(int sleepTime) {
    super();
    linuxCommand.addAll(Arrays.asList("skill", "-9", "vlc"));
    windowsCommand.addAll(Arrays.asList("cmd.exe", "/c", "start", "taskkill", "/im", "vlc.exe"));
    setOutputCommand();
    this.sleepTime = sleepTime;
  }
}
