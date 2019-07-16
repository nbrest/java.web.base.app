package com.nicobrest.kamehouse.systemcommand.model;

/**
 * Command line to execute for all the system commands that are to be
 * executed through the application.
 * For security purposes, I prefer to have them listed explicit in the code
 * rather than variable.
 * 
 * @author nbrest
 *
 */
public enum CommandLine {
  
  LOCK_SCREEN_LINUX("/bin/bash", "-c", "gnome-screensaver-command -l"),
  LOCK_SCREEN_WINDOWS("cmd.exe", "/c", "start", "rundll32.exe", "user32.dll,LockWorkStation"),
  
  SHUTDOWN_LINUX("/bin/bash", "-c", "sudo /sbin/shutdown -P "),
  SHUTDOWN_WINDOWS("cmd.exe", "/c", "start", "shutdown", "/s", "/t "),
  
  SHUTDOWN_CANCEL_LINUX("/bin/bash", "-c", "sudo /sbin/shutdown -c"),
  SHUTDOWN_CANCEL_WINDOWS("cmd.exe", "/c", "start", "shutdown", "/a"),
  
  SHUTDOWN_STATUS_LINUX("/bin/bash", "-c", 
      "ps aux | grep -e \"shutdown\\|COMMAND\" | grep -v grep"),
  SHUTDOWN_STATUS_WINDOWS("tasklist", "/FI", "IMAGENAME eq shutdown.exe"),
  
  VLC_START_LINUX("vlc"),
  VLC_START_WINDOWS("cmd.exe", "/c", "start", "vlc"),
  
  VLC_STATUS_LINUX("/bin/bash", "-c", "ps aux | grep -e \"vlc\\|COMMAND\" | grep -v grep"),
  VLC_STATUS_WINDOWS("tasklist", "/FI", "IMAGENAME eq vlc.exe"),
  
  VLC_STOP_LINUX("skill", "-9", "vlc"),
  VLC_STOP_WINDOWS("cmd.exe", "/c", "start", "taskkill", "/im", "vlc.exe"),
  
  VNCDO_KEY_LINUX("/bin/bash", "-c", "vncdo --server HOSTNAME --password VNC_SERVER_PASSWORD key"),
  VNCDO_KEY_WINDOWS("cmd.exe", "/c", "vncdo", "--server", "HOSTNAME", "--password",
      "VNC_SERVER_PASSWORD",  "key"),

  VNCDO_TYPE_LINUX("/bin/bash", "-c", 
      "vncdo --server HOSTNAME --password VNC_SERVER_PASSWORD type"),
  VNCDO_TYPE_WINDOWS("cmd.exe", "/c", "vncdo", "--server", "HOSTNAME", "--password",
      "VNC_SERVER_PASSWORD",  "type")
  ;
  
  private final String[] commandLine;
  
  private CommandLine(String... commandLine) {
    this.commandLine = commandLine;
  }
  
  /**
   * Returns the command line to execute.
   */
  public String[] get() {
    return commandLine.clone();
  }
}
