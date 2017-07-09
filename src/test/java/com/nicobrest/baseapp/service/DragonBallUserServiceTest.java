package com.nicobrest.baseapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nicobrest.baseapp.dao.DragonBallUserDao;
import com.nicobrest.baseapp.exception.BaseAppBadRequestException;
import com.nicobrest.baseapp.exception.BaseAppNotFoundException;
import com.nicobrest.baseapp.model.DragonBallUser;
import com.nicobrest.baseapp.service.DragonBallUserService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for the DragonBallUserService class.
 *
 * @author nbrest
 */
public class DragonBallUserServiceTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(DragonBallUserServiceTest.class);

  private static List<DragonBallUser> dragonBallUsersList;

  @InjectMocks
  private DragonBallUserService dragonBallUserService;

  @Mock(name = "dragonBallUserDao")
  private DragonBallUserDao dragonBallUserDaoMock;

  /**
   * Resets mock objects and initializes test repository.
   *
   * @author nbrest
   */
  @Before
  public void beforeTest() {
    /* Actions to perform before each test in the class */

    // Create test data to be returned by mock object dragonBallUserServiceMock
    DragonBallUser user1 = new DragonBallUser();
    user1.setId(1000L);
    user1.setAge(49);
    user1.setEmail("gokuTestMock@dbz.com");
    user1.setUsername("gokuTestMock");
    user1.setPowerLevel(30);
    user1.setStamina(1000);

    DragonBallUser user2 = new DragonBallUser();
    user2.setId(1001L);
    user2.setAge(29);
    user2.setEmail("gohanTestMock@dbz.com");
    user2.setUsername("gohanTestMock");
    user2.setPowerLevel(20);
    user2.setStamina(1000);

    DragonBallUser user3 = new DragonBallUser();
    user3.setId(1002L);
    user3.setAge(19);
    user3.setEmail("gotenTestMock@dbz.com");
    user3.setUsername("gotenTestMock");
    user3.setPowerLevel(10);
    user3.setStamina(1000);

    dragonBallUsersList = new LinkedList<DragonBallUser>();
    dragonBallUsersList.add(user1);
    dragonBallUsersList.add(user2);
    dragonBallUsersList.add(user3);

    // Reset mock objects before each test
    MockitoAnnotations.initMocks(this);
    Mockito.reset(dragonBallUserDaoMock);
  }

  /**
   * Test for calling the service to create a DragonBallUser in the repository.
   *
   * @author nbrest
   */
  @Test
  public void createDragonBallUserTest() {
    LOGGER.info("***** Executing createDragonBallUserTest");

    // Normal flow
    try {
      DragonBallUser userToAdd = new DragonBallUser(0L, "vegeta", "vegeta@dbz.com", 50, 50, 50);
      Mockito.doReturn(1L).when(dragonBallUserDaoMock).createDragonBallUser(userToAdd);

      dragonBallUserService.createDragonBallUser(userToAdd);

      verify(dragonBallUserDaoMock, times(1)).createDragonBallUser(userToAdd);
    } catch (BaseAppBadRequestException e) {
      e.printStackTrace();
      fail("Caught unexpected exception.");
    }
  }

  /**
   * Test for calling the service to get a single DragonBallUser in the
   * repository by id.
   *
   * @author nbrest
   */
  @Test
  public void getDragonBallUserTest() {
    LOGGER.info("***** Executing getDragonBallUserTest");

    // Normal flow
    try {
      when(dragonBallUserDaoMock.getDragonBallUser(1000L))
          .thenReturn(dragonBallUsersList.get(0));

      DragonBallUser user = dragonBallUserService.getDragonBallUser(1000L);

      LOGGER.info("user: " + user.getUsername());

      assertNotNull(user);
      assertEquals("1000", user.getId().toString());
      verify(dragonBallUserDaoMock, times(1)).getDragonBallUser(1000L);
    } catch (BaseAppNotFoundException e) {
      e.printStackTrace();
      fail("Caught unexpected exception.");
    }
  }

  /**
   * Test for calling the service to get a single DragonBallUser in the
   * repository by username.
   *
   * @author nbrest
   */
  @Test
  public void getDragonBallUserByUsernameTest() {
    LOGGER.info("***** Executing getDragonBallUserByUsernameTest");

    // Normal flow
    try {
      when(dragonBallUserDaoMock.getDragonBallUser("gokuTestMock"))
          .thenReturn(dragonBallUsersList.get(0));

      DragonBallUser user = dragonBallUserService.getDragonBallUser("gokuTestMock");

      LOGGER.info("user: " + user.getUsername());

      assertNotNull(user);
      assertEquals("gokuTestMock", user.getUsername());
      verify(dragonBallUserDaoMock, times(1)).getDragonBallUser("gokuTestMock");
    } catch (BaseAppNotFoundException e) {
      e.printStackTrace();
      fail("Caught unexpected exception.");
    }
  }

  /**
   * Test for calling the service to get a single DragonBallUser in the
   * repository by its email.
   *
   * @author nbrest
   */
  @Test
  public void getDragonBallUserByEmailTest() {
    LOGGER.info("***** Executing getDragonBallUserByEmailTest");

    // Normal flow
    try {
      when(dragonBallUserDaoMock.getDragonBallUserByEmail("gokuTestMock@dbz.com"))
          .thenReturn(dragonBallUsersList.get(0));

      DragonBallUser user = dragonBallUserService.getDragonBallUserByEmail("gokuTestMock@dbz.com");

      LOGGER.info("user: " + user.getUsername());

      assertNotNull(user);
      assertEquals("gokuTestMock", user.getUsername());
      verify(dragonBallUserDaoMock, times(1)).getDragonBallUserByEmail("gokuTestMock@dbz.com");
    } catch (BaseAppNotFoundException e) {
      e.printStackTrace();
      fail("Caught unexpected exception.");
    }
  }

  /**
   * Test for calling the service to update an existing DragonBallUser in the
   * repository.
   *
   * @author nbrest
   */
  @Test
  public void updateDragonBallUserTest() {
    LOGGER.info("***** Executing updateDragonBallUserTest");

    // Normal flow
    try {
      DragonBallUser userToUpdate = new DragonBallUser(0L, "goku", "gokuUpdated@dbz.com", 30, 30,
          30);
      Mockito.doNothing().when(dragonBallUserDaoMock).updateDragonBallUser(userToUpdate);

      dragonBallUserService.updateDragonBallUser(userToUpdate);

      verify(dragonBallUserDaoMock, times(1)).updateDragonBallUser(userToUpdate);
    } catch (BaseAppNotFoundException e) {
      e.printStackTrace();
      fail("Caught unexpected exception.");
    }
  }

  /**
   * Test for calling the service to delete an existing user in the repository.
   *
   * @author nbrest
   */
  @Test
  public void deleteDragonBallUserTest() {
    LOGGER.info("***** Executing deleteDragonBallUserTest");

    // Normal flow
    try {
      when(dragonBallUserDaoMock.deleteDragonBallUser(1L)).thenReturn(dragonBallUsersList.get(0));

      dragonBallUserService.deleteDragonBallUser(1L);

      verify(dragonBallUserDaoMock, times(1)).deleteDragonBallUser(1L);
    } catch (BaseAppNotFoundException e) {
      e.printStackTrace();
      fail("Caught unexpected exception.");
    }
  }

  /**
   * Test for calling the service to get all the DragonBallUsers in the
   * repository.
   *
   * @author nbrest
   */
  @Test
  public void getAllDragonBallUsersTest() {
    LOGGER.info("***** Executing getAllDragonBallUsersTest");

    when(dragonBallUserDaoMock.getAllDragonBallUsers()).thenReturn(dragonBallUsersList);

    List<DragonBallUser> usersList = dragonBallUserService.getAllDragonBallUsers();

    LOGGER.info("dragonBallUsers.get(0): " + usersList.get(0).getUsername());
    LOGGER.info("dragonBallUsers.get(1): " + usersList.get(1).getUsername());
    LOGGER.info("dragonBallUsers.get(2): " + usersList.get(2).getUsername());

    assertEquals("gokuTestMock", usersList.get(0).getUsername());
    assertEquals("gokuTestMock@dbz.com", usersList.get(0).getEmail());
    assertEquals(49, usersList.get(0).getAge());
    assertEquals("1000", usersList.get(0).getId().toString());
    assertEquals(30, usersList.get(0).getPowerLevel());
    assertEquals(1000, usersList.get(0).getStamina());

    assertEquals("gohanTestMock", usersList.get(1).getUsername());
    assertEquals("gohanTestMock@dbz.com", usersList.get(1).getEmail());
    assertEquals(29, usersList.get(1).getAge());
    assertEquals("1001", usersList.get(1).getId().toString());
    assertEquals(20, usersList.get(1).getPowerLevel());
    assertEquals(1000, usersList.get(1).getStamina());

    assertEquals("gotenTestMock", usersList.get(2).getUsername());
    assertEquals("gotenTestMock@dbz.com", usersList.get(2).getEmail());
    assertEquals(19, usersList.get(2).getAge());
    assertEquals("1002", usersList.get(2).getId().toString());
    assertEquals(10, usersList.get(2).getPowerLevel());
    assertEquals(1000, usersList.get(2).getStamina());

    verify(dragonBallUserDaoMock, times(1)).getAllDragonBallUsers();
  }
}
