package com.nicobrest.kamehouse.admin.service;

import com.nicobrest.kamehouse.admin.dao.ApplicationUserDao;
import com.nicobrest.kamehouse.commons.exception.KameHouseNotFoundException;
import com.nicobrest.kamehouse.commons.model.ApplicationRole;
import com.nicobrest.kamehouse.commons.model.ApplicationUser;
import com.nicobrest.kamehouse.commons.model.dto.ApplicationRoleDto;
import com.nicobrest.kamehouse.commons.model.dto.ApplicationUserDto;
import com.nicobrest.kamehouse.commons.service.AbstractCrudService;
import com.nicobrest.kamehouse.commons.service.CrudService;
import com.nicobrest.kamehouse.commons.utils.PasswordUtils;
import com.nicobrest.kamehouse.commons.validator.ApplicationUserValidator;
import com.nicobrest.kamehouse.commons.validator.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service layer to manage the users in the application.
 * 
 * @author nbrest
 *
 */
@Service
public class ApplicationUserService extends
    AbstractCrudService<ApplicationUser, ApplicationUserDto> implements
    CrudService<ApplicationUser, ApplicationUserDto>, UserDetailsService {

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
  public Long create(ApplicationUserDto dto) {
    dto.setPassword(PasswordUtils.generateHashedPassword(dto.getPassword()));
    return create(applicationUserDao, dto);
  }

  @Override
  public ApplicationUser read(Long id) {
    return read(applicationUserDao, id);
  }

  @Override
  public List<ApplicationUser> readAll() {
    return readAll(applicationUserDao);
  }

  @Override
  public void update(ApplicationUserDto dto) {
    update(applicationUserDao, dto);
  }

  @Override
  public ApplicationUser delete(Long id) {
    return delete(applicationUserDao, id);
  }

  @Override
  public ApplicationUser loadUserByUsername(String username) {
    logger.trace("loadUserByUsername {}", username);
    if (username.equals("anonymousUser")) {
      logger.trace("loadUserByUsername {} response {}", username, anonymousUser);
      return anonymousUser;
    }
    try {
      ApplicationUser applicationUser = applicationUserDao.loadUserByUsername(username);
      logger.trace("loadUserByUsername {} response {}", username, applicationUser);
      return applicationUser;
    } catch (KameHouseNotFoundException e) {
      throw new UsernameNotFoundException(e.getMessage(), e);
    }
  }

  @Override
  protected ApplicationUser getModel(ApplicationUserDto applicationUserDto) {
    ApplicationUser applicationUser = new ApplicationUser();
    applicationUser.setAccountNonExpired(applicationUserDto.isAccountNonExpired());
    applicationUser.setAccountNonLocked(applicationUserDto.isAccountNonLocked());
    Set<ApplicationRole> applicationRoles = new HashSet<>();
    Set<ApplicationRoleDto> applicationRoleDtos = applicationUserDto.getAuthorities();
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

  @Override
  protected void validate(ApplicationUser applicationUser) {
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
}