package com.nicobrest.kamehouse.admin.service;

import com.nicobrest.kamehouse.admin.dao.ApplicationUserDao;
import com.nicobrest.kamehouse.admin.model.ApplicationRole;
import com.nicobrest.kamehouse.admin.model.ApplicationUser;
import com.nicobrest.kamehouse.admin.security.PasswordUtils;
import com.nicobrest.kamehouse.admin.service.dto.ApplicationRoleDto;
import com.nicobrest.kamehouse.admin.service.dto.ApplicationUserDto;
import com.nicobrest.kamehouse.admin.validator.ApplicationUserValidator;
import com.nicobrest.kamehouse.main.validator.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer to manage the users in the application.
 * 
 * @author nbrest
 *
 */
@Service
public class ApplicationUserService implements UserDetailsService {

  @Autowired
  @Qualifier("applicationUserDaoJpa")
  private ApplicationUserDao applicationUserDao;

  @Autowired
  @Qualifier("anonymousUser")
  private ApplicationUser anonymousUser;

  public void setApplicationUserDao(ApplicationUserDao applicationUserDao) {
    this.applicationUserDao = applicationUserDao;
  }

  public ApplicationUserDao getApplicationUserDao() {
    return applicationUserDao;
  }

  public void setAnonymousUser(ApplicationUser anonymousUser) {
    this.anonymousUser = anonymousUser;
  }

  public ApplicationUser getAnonymousUser() {
    return anonymousUser;
  }

  @Override
  public ApplicationUser loadUserByUsername(String username) {
    if (username.equals("anonymousUser")) {
      return anonymousUser;
    }
    return applicationUserDao.loadUserByUsername(username);
  }

  /**
   * Creates a new application user in the repository.
   */
  public Long createUser(ApplicationUserDto applicationUserDto) {
    ApplicationUser applicationUser = getModel(applicationUserDto);
    validateApplicationUser(applicationUser);
    applicationUser
        .setPassword(PasswordUtils.generateHashedPassword(applicationUser.getPassword()));
    return applicationUserDao.createUser(applicationUser);
  }

  /**
   * Updates an application user in the repository.
   */
  public void updateUser(ApplicationUserDto applicationUserDto) {
    ApplicationUser applicationUser = getModel(applicationUserDto);
    validateApplicationUser(applicationUser);
    applicationUserDao.updateUser(applicationUser);
  }

  /**
   * Deletes an application user from the repository.
   */
  public ApplicationUser deleteUser(Long id) {
    return applicationUserDao.deleteUser(id);
  }

  /**
   * Get all application users.
   */
  public List<ApplicationUser> getAllUsers() {
    return applicationUserDao.getAllUsers();
  }

  /**
   * Validates the application user attributes.
   */
  private void validateApplicationUser(ApplicationUser applicationUser) {
    ApplicationUserValidator.validateFirstNameFormat(applicationUser.getFirstName());
    ApplicationUserValidator.validateLastNameFormat(applicationUser.getLastName());
    UserValidator.validateUsernameFormat(applicationUser.getUsername());
    UserValidator.validateEmailFormat(applicationUser.getEmail());
    UserValidator.validateStringLength(applicationUser.getFirstName());
    UserValidator.validateStringLength(applicationUser.getLastName());
    UserValidator.validateStringLength(applicationUser.getUsername());
    UserValidator.validateStringLength(applicationUser.getEmail());
    UserValidator.validateStringLength(applicationUser.getPassword());
  }

  /**
   * Gets an ApplicationUser model object from it's DTO. 
   */
  private ApplicationUser getModel(ApplicationUserDto applicationUserDto) {
    ApplicationUser applicationUser = new ApplicationUser();
    applicationUser.setAccountNonExpired(applicationUserDto.isAccountNonExpired());
    applicationUser.setAccountNonLocked(applicationUserDto.isAccountNonLocked());
    List<ApplicationRole> applicationRoles = new ArrayList<>();
    List<ApplicationRoleDto> applicationRoleDtos = applicationUserDto.getAuthorities();
    if (applicationRoleDtos != null) {
      for (ApplicationRoleDto applicationRoleDto : applicationRoleDtos) {
        ApplicationRole applicationRole = new ApplicationRole();
        applicationRole.setId(applicationRoleDto.getId());
        applicationRole.setName(applicationRoleDto.getName());
        applicationRole.setApplicationUser(applicationUser);
        applicationRoles.add(applicationRole);
      }
    }
    applicationUser.setAuthorities(applicationRoles);
    applicationUser.setCredentialsNonExpired(applicationUserDto.isCredentialsNonExpired());
    applicationUser.setEmail(applicationUserDto.getEmail());
    applicationUser.setEnabled(applicationUserDto.isEnabled());
    applicationUser.setFirstName(applicationUserDto.getFirstName());
    applicationUser.setId(applicationUserDto.getId());
    applicationUser.setLastLogin(applicationUserDto.getLastLogin());
    applicationUser.setLastName(applicationUserDto.getLastName());
    applicationUser.setPassword(applicationUserDto.getPassword());
    applicationUser.setUsername(applicationUserDto.getUsername());
    return applicationUser;
  }
}