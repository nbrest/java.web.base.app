package com.nicobrest.kamehouse.admin.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.nicobrest.kamehouse.admin.model.ApplicationUser;
import com.nicobrest.kamehouse.admin.service.dto.ApplicationUserDto;
import com.nicobrest.kamehouse.admin.testutils.ApplicationUserTestUtils;
import com.nicobrest.kamehouse.main.testutils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Unit tests for the ApplicationUserInMemoryDao class.
 *
 * @author nbrest
 */
public class ApplicationUserDaoInMemoryTest {

  private TestUtils<ApplicationUser, ApplicationUserDto> testUtils;
  private ApplicationUser applicationUser;
  private ApplicationUserDaoInMemory applicationUserDao;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Initializes test repositories.
   */
  @Before
  public void init() {
    testUtils = new ApplicationUserTestUtils();
    testUtils.initTestData();
    applicationUser = testUtils.getSingleTestData();
    applicationUserDao = new ApplicationUserDaoInMemory();
  }

  /**
   * Test for creating a ApplicationUser in the repository.
   */
  @Test
  public void createTest() {
    applicationUserDao.create(applicationUser);

    ApplicationUser createdUser =
        applicationUserDao.loadUserByUsername(applicationUser.getUsername());
    assertEquals(applicationUser, createdUser);
  }

  /**
   * Test for getting all the ApplicationUsers in the repository.
   */
  @Test
  public void readAllTest() {
    assertEquals(4, applicationUserDao.readAll().size());
  }

  /**
   * Test for updating an existing user in the repository.
   */
  @Test
  public void updateTest() {
    ApplicationUser originalUser = applicationUserDao.loadUserByUsername("admin");
    applicationUser.setId(originalUser.getId());
    applicationUser.setUsername(originalUser.getUsername());

    applicationUserDao.update(applicationUser);

    ApplicationUser updatedUser = applicationUserDao.loadUserByUsername("admin");
    assertEquals(applicationUser, updatedUser);
  }

  /**
   * Test for updating an existing user in the repository Exception flows.
   */
  @Test
  public void updateNotFoundExceptionTest() {
    applicationUser.setUsername(ApplicationUserTestUtils.INVALID_USERNAME);
    thrown.expect(UsernameNotFoundException.class);
    thrown.expectMessage(
        "User with username " + ApplicationUserTestUtils.INVALID_USERNAME + " not found.");

    applicationUserDao.update(applicationUser);
  }

  /**
   * Test for deleting an existing user from the repository.
   */
  @Test
  public void deleteTest() {
    ApplicationUser userToDelete = applicationUserDao.loadUserByUsername("admin");

    ApplicationUser deletedUser = applicationUserDao.delete(userToDelete.getId());

    assertEquals(userToDelete, deletedUser);
  }

  /**
   * Test for deleting an existing user from the repository Exception flows.
   */
  @Test
  public void deleteNotFoundExceptionTest() {
    thrown.expect(UsernameNotFoundException.class);
    thrown.expectMessage("User with id " + ApplicationUserTestUtils.INVALID_ID + " not found.");

    applicationUserDao.delete(ApplicationUserTestUtils.INVALID_ID);
  }

  /**
   * Test for getting a single ApplicationUser in the repository by its username.
   */
  @Test
  public void loadUserByUsernameTest() {
    ApplicationUser user = applicationUserDao.loadUserByUsername("admin");

    assertNotNull(user);
    assertEquals("admin", user.getUsername());
  }

  /**
   * Test for getting a single ApplicationUser in the repository Exception flows.
   */
  @Test
  public void loadUserByUsernameNotFoundExceptionTest() {
    thrown.expect(UsernameNotFoundException.class);
    thrown.expectMessage(
        "User with username " + ApplicationUserTestUtils.INVALID_USERNAME + " not found.");

    applicationUserDao.loadUserByUsername(ApplicationUserTestUtils.INVALID_USERNAME);
  }
}
