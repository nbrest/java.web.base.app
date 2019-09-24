package com.nicobrest.kamehouse.admin.controller;

import com.nicobrest.kamehouse.admin.model.ApplicationUser;
import com.nicobrest.kamehouse.admin.service.ApplicationUserService;
import com.nicobrest.kamehouse.admin.service.dto.ApplicationUserDto;
import com.nicobrest.kamehouse.main.controller.AbstractCrudController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller class for the application users.
 *
 * @author nbrest
 */
@Controller
@RequestMapping(value = "/api/v1/admin/application")
public class ApplicationUserController extends AbstractCrudController {
 
  @Autowired
  private ApplicationUserService applicationUserService;

  /**
   * Creates a new ApplicationUser in the repository.
   */
  @PostMapping(path = "/users")
  @ResponseBody
  public ResponseEntity<Long> create(@RequestBody ApplicationUserDto dto) { 
    return create("/application/users", applicationUserService, dto);
  }
  
  /**
   * Reads an application user by it's id.
   */
  @GetMapping(path = "/users/{id}")
  @ResponseBody
  public ResponseEntity<ApplicationUser> read(@PathVariable Long id) {
    logger.trace("In controller /application/users/{id} (GET)");
    ApplicationUser applicationUser = applicationUserService.read(id);
    // Don't return the passwords through the API.
    applicationUser.setPassword(null);
    return generateGetResponseEntity(applicationUser);
  }
  
  /**
   * Reads all application users.
   */
  @GetMapping(path = "/users")
  @ResponseBody
  public ResponseEntity<List<ApplicationUser>> readAll() {
    logger.trace("In controller /application/users/ (GET)");
    List<ApplicationUser> applicationUsers = applicationUserService.readAll();
    // Don't return the passwords through the API.
    for (ApplicationUser appUser : applicationUsers) {
      appUser.setPassword(null);
    }
    return generateGetResponseEntity(applicationUsers);
  }

  /**
   * Updates a user in the repository.
   */
  @PutMapping(path = "/users/{id}")
  @ResponseBody
  public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody ApplicationUserDto dto) {
    logger.trace("In controller /application/users/{id} (PUT)");
    validatePathAndRequestBodyIds(id, dto.getId());
    applicationUserService.update(dto);
    return generatePutResponseEntity();
  }

  /**
   * Deletes an existing user from the repository.
   */
  @DeleteMapping(path = "/users/{id}")
  @ResponseBody
  public ResponseEntity<ApplicationUser> delete(@PathVariable Long id) {
    logger.trace("In controller /application/users/{id} (DELETE)");
    ApplicationUser deletedAppUser = applicationUserService.delete(id);
    // Don't return the passwords through the API.
    deletedAppUser.setPassword(null);
    return generateDeleteResponseEntity(deletedAppUser);
  }
  
  /**
   * Gets a specific ApplicationUser from the repository based on the username.
   */
  @GetMapping(path = "/users/username/{username:.+}")
  @ResponseBody
  public ResponseEntity<ApplicationUser> loadUserByUsername(@PathVariable String username) {
    logger.trace("In controller /application/users/username/{username:.+} (GET)");
    ApplicationUser applicationUser = applicationUserService.loadUserByUsername(username);
    // Don't return the password through the API.
    applicationUser.setPassword(null);
    return generateGetResponseEntity(applicationUser);
  }
}
