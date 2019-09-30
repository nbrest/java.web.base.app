package com.nicobrest.kamehouse.admin.service;

import com.nicobrest.kamehouse.admin.model.SystemCommandOutput;
import com.nicobrest.kamehouse.admin.model.admincommand.AdminCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to execute admin commands.
 * 
 * @author nbrest
 *
 */
@Service
public class AdminCommandService {

  @Autowired
  private SystemCommandService systemCommandService;

  public SystemCommandService getSystemCommandService() {
    return systemCommandService;
  }

  public void setSystemCommandService(SystemCommandService systemCommandService) {
    this.systemCommandService = systemCommandService;
  }

  /**
   * Execute AdminCommand. Translate it to system commands and execute them.
   */
  public List<SystemCommandOutput> execute(AdminCommand adminCommand) {
    return systemCommandService.execute(adminCommand.getSystemCommands());
  }
}
