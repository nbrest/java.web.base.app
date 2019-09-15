package com.nicobrest.kamehouse.admin.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nicobrest.kamehouse.admin.dao.ApplicationUserDao;
import com.nicobrest.kamehouse.admin.model.ApplicationUser;
import com.nicobrest.kamehouse.admin.service.dto.ApplicationUserDto;
import com.nicobrest.kamehouse.admin.testutils.ApplicationUserTestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * Unit tests for the ApplicationUserService class.
 *
 * @author nbrest
 */
public class ApplicationUserServiceTest {

  private static ApplicationUser applicationUser;
  private static List<ApplicationUser> applicationUsersList;
  private static ApplicationUserDto applicationUserDto;
  
  @InjectMocks
  private ApplicationUserService applicationUserService;

  @Mock
  private ApplicationUserDao applicationUserDaoMock;

  /**
   * Resets mock objects and initializes test repository.
   */
  @Before
  public void beforeTest() {   
    ApplicationUserTestUtils.initApplicationUserTestData();
    applicationUser = ApplicationUserTestUtils.getApplicationUser();
    applicationUsersList = ApplicationUserTestUtils.getApplicationUsersList();
    applicationUserDto = ApplicationUserTestUtils.getApplicationUserDto();
    
    MockitoAnnotations.initMocks(this);
    Mockito.reset(applicationUserDaoMock);
  }

  /**
   * Test for calling the service to create an ApplicationUser in the
   * repository.
   */
  @Test
  public void createUserTest() {
    try {
      Mockito.doReturn(1L).when(applicationUserDaoMock).createUser(applicationUser);
      applicationUserService.createUser(applicationUserDto);
      verify(applicationUserDaoMock, times(1)).createUser(applicationUser);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Caught unexpected exception.");
    }
  }
  
  /**
   * Test for calling the service to get a single ApplicationUser in the
   * repository by username.
   */
  @Test
  public void loadUserByUsernameTest() {

    try {
      when(applicationUserDaoMock.loadUserByUsername(applicationUser.getUsername()))
          .thenReturn(applicationUser);
      ApplicationUser user = applicationUserService.loadUserByUsername(applicationUser
          .getUsername());
      assertNotNull(user);
      assertEquals("1001", user.getId().toString());
      verify(applicationUserDaoMock, times(1)).loadUserByUsername(applicationUser
          .getUsername());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Caught unexpected exception.");
    }
  }

  /**
   * Test for getting all users of the application.
   */
  @Test
  public void getAllUsersTest() { 
    try {
      when(applicationUserDaoMock.getAllUsers()).thenReturn(applicationUsersList);
      List<ApplicationUser> returnedApplicationUsers = applicationUserService.getAllUsers();
      assertEquals(applicationUsersList.size(), returnedApplicationUsers.size());
      verify(applicationUserDaoMock, times(1)).getAllUsers();
    } catch (Exception e) {
      e.printStackTrace();
      fail("Caught unexpected exception.");
    }
  }
  
  /**
   * Test for calling the service to update an existing ApplicationUser in the
   * repository.
   */
  @Test
  public void updateUserTest() {

    try {
      Mockito.doNothing().when(applicationUserDaoMock).updateUser(applicationUser);
      applicationUserService.updateUser(applicationUserDto);
      verify(applicationUserDaoMock, times(1)).updateUser(applicationUser);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Caught unexpected exception.");
    }
  }

  /**
   * Test for calling the service to delete an existing user in the repository.
   */
  @Test
  public void deleteUserTest() {

    try {
      when(applicationUserDaoMock.deleteUser(1000L)).thenReturn(applicationUser);
      applicationUserService.deleteUser(1000L);
      verify(applicationUserDaoMock, times(1)).deleteUser(1000L);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Caught unexpected exception.");
    }
  }
}
