package com.nicobrest.kamehouse.admin.model;

public class AdminShutdownCommand {

  private String command;
  private int time;
  
  public String getCommand() {
    return command;
  }
  
  public void setCommand(String command) {
    this.command = command;
  } 
  
  public int getTime() {
    return time;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
}
