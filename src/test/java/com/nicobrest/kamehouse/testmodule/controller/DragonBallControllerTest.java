package com.nicobrest.kamehouse.testmodule.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nicobrest.kamehouse.main.controller.AbstractControllerTest;
import com.nicobrest.kamehouse.main.exception.KameHouseBadRequestException;
import com.nicobrest.kamehouse.main.exception.KameHouseConflictException;
import com.nicobrest.kamehouse.main.exception.KameHouseException;
import com.nicobrest.kamehouse.main.exception.KameHouseNotFoundException;
import com.nicobrest.kamehouse.testmodule.model.DragonBallUser;
import com.nicobrest.kamehouse.testmodule.service.DragonBallUserService;
import com.nicobrest.kamehouse.testmodule.service.dto.DragonBallUserDto;
import com.nicobrest.kamehouse.testmodule.testutils.DragonBallUserTestUtils;
import com.nicobrest.kamehouse.utils.JsonUtils;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
//import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.io.IOException;
import java.util.List;

/**
 * Unit tests for the DragonBallController class.
 *
 * @author nbrest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@WebAppConfiguration
public class DragonBallControllerTest extends AbstractControllerTest {

  public static final String API_V1_DRAGONBALL_USERS =
      DragonBallUserTestUtils.API_V1_DRAGONBALL_USERS;
  private static DragonBallUser dragonBallUser;
  private static DragonBallUserDto dragonBallUserDto;
  private static List<DragonBallUser> dragonBallUsersList;

  @InjectMocks
  private DragonBallController dragonBallController;

  @Mock(name = "dragonBallUserService")
  private DragonBallUserService dragonBallUserServiceMock;

  /**
   * Actions to perform once before all tests.
   */
  @BeforeClass
  public static void beforeClassTest() {
    /* Initialization tasks that happen once for all tests. */
    DragonBallUserTestUtils.initTestData();
  }

  /**
   * Resets mock objects and test data.
   */
  @Before
  public void beforeTest() {
    DragonBallUserTestUtils.initTestData();
    DragonBallUserTestUtils.setIds();
    dragonBallUser = DragonBallUserTestUtils.getSingleTestData();
    dragonBallUserDto = DragonBallUserTestUtils.getTestDataDto();
    dragonBallUsersList = DragonBallUserTestUtils.getTestDataList();

    MockitoAnnotations.initMocks(this);
    Mockito.reset(dragonBallUserServiceMock);
    mockMvc = MockMvcBuilders.standaloneSetup(dragonBallController).build();
  }

  /**
   * Clean up after each test.
   */
  @After
  public void afterTest() {
    /* Actions to perform after each test */

  }

  /**
   * Cleanup after all tests have executed.
   */
  @AfterClass
  public static void afterClassTest() {
    /* Actions to perform ONCE after all tests in the class */
  }

  /**
   * /dragonball/model-and-view (GET) Test the endpoint /dragonball/model-and-view
   * with the HTTP method GET. The service should respond with HTTP status 200 OK
   * and a view defined in dragonball/modelAndView.jsp. 
   */
  @Test
  public void getModelAndViewTest() throws Exception {
    MockHttpServletResponse response = executeGet("/api/v1/dragonball/model-and-view");
    //ModelAndView responseBody = response.getContentAsByteArray(); -> Convert from byte[] to Object
    
    verifyResponseStatus(response, HttpStatus.OK);
    //assertEquals("jsp/test-module/jsp/dragonball/model-and-view", responseBody.getViewName());
    assertEquals("jsp/test-module/jsp/dragonball/model-and-view", response.getForwardedUrl());
    //assertEquals("Goku", responseBody.getModel().get("name"));
    //assertEquals("message: dragonball ModelAndView!", responseBody.getModel().get("message"));
    verifyZeroInteractions(dragonBallUserServiceMock);
  }

  /**
   * /dragonball/users (GET) Test the rest web service on the endpoint
   * /dragonball/users with the HTTP method GET. The service should respond with
   * HTTP status 200 OK and a json array in the response body. 
   */
  @Test
  public void getUsersTest() throws Exception {
    when(dragonBallUserServiceMock.getAllDragonBallUsers()).thenReturn(dragonBallUsersList);

    MockHttpServletResponse response = executeGet(API_V1_DRAGONBALL_USERS);
    List<DragonBallUser> responseBody = getResponseBodyList(response, DragonBallUser.class);
    
    verifyResponseStatus(response, HttpStatus.OK);
    verifyContentType(response, MediaType.APPLICATION_JSON_UTF8);
    assertEquals(dragonBallUsersList.size(), responseBody.size());
    assertEquals(dragonBallUsersList, responseBody);
    verify(dragonBallUserServiceMock, times(1)).getAllDragonBallUsers();
    verifyNoMoreInteractions(dragonBallUserServiceMock);
  }

  /**
   * /dragonball/users (GET) Test the rest web service on the endpoint
   * /dragonball/users with the parameter to throw an exception. 
   */
  @Test
  public void getUsersExceptionTest() throws Exception {
    thrown.expect(NestedServletException.class);
    thrown.expectCause(IsInstanceOf.<Throwable> instanceOf(KameHouseException.class));
    
    executeGet(API_V1_DRAGONBALL_USERS + "?action=KameHouseException");
  }

  /**
   * /dragonball/users (GET) Test the rest web service on the endpoint
   * /dragonball/users with the parameter to throw an exception.
   */
  @Test
  public void getUsersNotFoundExceptionTest() {

    try {
      ResultActions requestResult =
          mockMvc.perform(get(API_V1_DRAGONBALL_USERS + "?action=KameHouseNotFoundException"))
              .andDo(print());
      requestResult.andExpect(status().isNotFound());
      fail("Expected an exception to be thrown.");
    } catch (Exception e) {
      // Do nothing. Expected an exception
    }
    // Verify gotenDragonBallUserMock invocations
    verifyZeroInteractions(dragonBallUserServiceMock);
  }

  /**
   * /dragonball/users (POST) Test creating a new DragonBallUser in the
   * repository.
   */
  @Test
  public void postUsersTest() throws Exception {
    Mockito.doReturn(dragonBallUser.getId()).when(dragonBallUserServiceMock)
        .createDragonBallUser(dragonBallUserDto);
    when(dragonBallUserServiceMock.getDragonBallUser(dragonBallUser.getUsername()))
        .thenReturn(dragonBallUser);
    byte[] requestPayload = JsonUtils.toJsonByteArray(dragonBallUserDto);

    MockHttpServletResponse response = executePost(API_V1_DRAGONBALL_USERS, requestPayload);
    Long responseBody = getResponseBody(response, Long.class);

    verifyResponseStatus(response, HttpStatus.CREATED);
    assertEquals(dragonBallUserDto.getId(), responseBody);
    verify(dragonBallUserServiceMock, times(1)).createDragonBallUser(dragonBallUserDto);
  }

  /**
   * /dragonball/users (POST) Test creating a new DragonBallUser in the repository
   * that already exists.
   */
  @Test
  public void postUsersConflictExceptionTest() {

    // Exception flows
    try {
      // Setup mock object dragonBallUserServiceMock
      Mockito.doThrow(new KameHouseConflictException("User already exists"))
          .when(dragonBallUserServiceMock).createDragonBallUser(dragonBallUserDto);

      // Execute HTTP POST on the /dragonball/users endpoint
      ResultActions requestResult =
          mockMvc.perform(post(API_V1_DRAGONBALL_USERS).contentType(MediaType.APPLICATION_JSON_UTF8)
              .content(JsonUtils.toJsonByteArray(dragonBallUserDto))).andDo(print());
      requestResult.andExpect(status().is4xxClientError());

      verify(dragonBallUserServiceMock, times(1)).createDragonBallUser(dragonBallUserDto);
    } catch (Exception e) {
      if (!(e.getCause() instanceof KameHouseConflictException)) {
        e.printStackTrace();
        fail("Unexpected exception thrown.");
      }
    }
  }

  /**
   * /dragonball/users/{id} (GET) Tests getting a specific user from the
   * repository.
   */
  @Test
  public void getUsersIdTest() {

    try {
      when(dragonBallUserServiceMock.getDragonBallUser(101L))
          .thenReturn(dragonBallUsersList.get(0));

      ResultActions requestResult =
          mockMvc.perform(get(API_V1_DRAGONBALL_USERS + "101")).andDo(print());
      requestResult.andExpect(status().isOk());
      requestResult.andExpect(content().contentType("application/json;charset=UTF-8"));
      requestResult.andExpect(jsonPath("$.username", equalTo("gokuTestMock")));
      requestResult.andExpect(jsonPath("$.email", equalTo("gokuTestMock@dbz.com")));
      requestResult.andExpect(jsonPath("$.age", equalTo(49)));
      requestResult.andExpect(jsonPath("$.powerLevel", equalTo(30)));
      requestResult.andExpect(jsonPath("$.stamina", equalTo(1000)));
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception thrown.");
    }
  }

  /**
   * /dragonball/users/username/{username} (GET) Tests getting a specific user
   * from the repository.
   */
  @Test
  public void getUsersUsernameTest() {

    try {
      when(dragonBallUserServiceMock.getDragonBallUser("gokuTestMock"))
          .thenReturn(dragonBallUsersList.get(0));

      ResultActions requestResult =
          mockMvc.perform(get(API_V1_DRAGONBALL_USERS + "username/gokuTestMock")).andDo(print());
      requestResult.andExpect(status().isOk());
      requestResult.andExpect(content().contentType("application/json;charset=UTF-8"));
      requestResult.andExpect(jsonPath("$.username", equalTo("gokuTestMock")));
      requestResult.andExpect(jsonPath("$.email", equalTo("gokuTestMock@dbz.com")));
      requestResult.andExpect(jsonPath("$.age", equalTo(49)));
      requestResult.andExpect(jsonPath("$.powerLevel", equalTo(30)));
      requestResult.andExpect(jsonPath("$.stamina", equalTo(1000)));
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception thrown.");
    }

    // Exception flows
    try {
      Mockito.reset(dragonBallUserServiceMock);
      Mockito.doThrow(new KameHouseNotFoundException("User trunks not found"))
          .when(dragonBallUserServiceMock).getDragonBallUser("trunks");

      ResultActions requestResult =
          mockMvc.perform(get(API_V1_DRAGONBALL_USERS + "/username/trunks")).andDo(print());
      requestResult.andExpect(status().is4xxClientError());
      verify(dragonBallUserServiceMock, times(1)).getDragonBallUser("trunks");
    } catch (Exception e) {
      if (!(e.getCause() instanceof KameHouseNotFoundException)) {
        e.printStackTrace();
        fail("Unexpected exception thrown.");
      }
    }
  }

  /**
   * /dragonball/users/username/{username} (GET) Tests user not found when getting
   * a specific user from the repository.
   */
  @Test
  public void getUsersUsernameNotFoundExceptionTest() {

    // Exception flows
    try {
      Mockito.doThrow(new KameHouseNotFoundException("User trunks not found"))
          .when(dragonBallUserServiceMock).getDragonBallUser("trunks");

      ResultActions requestResult =
          mockMvc.perform(get(API_V1_DRAGONBALL_USERS + "/username/trunks")).andDo(print());
      requestResult.andExpect(status().is4xxClientError());
      verify(dragonBallUserServiceMock, times(1)).getDragonBallUser("trunks");
    } catch (Exception e) {
      if (!(e.getCause() instanceof KameHouseNotFoundException)) {
        e.printStackTrace();
        fail("Unexpected exception thrown.");
      }
    }
  }

  /**
   * /dragonball/users/emails/{email} (GET) Tests getting a specific user from the
   * repository by email.
   */
  @Test
  public void getUsersUsernameByEmailTest() {

    try {
      // Setup mock object dragonBallUserServiceMock
      when(dragonBallUserServiceMock.getDragonBallUserByEmail("gokuTestMock@dbz.com"))
          .thenReturn(dragonBallUsersList.get(0));

      // Execute HTTP GET on the /dragonball/users/{username} endpoint
      ResultActions requestResult = mockMvc
          .perform(get(API_V1_DRAGONBALL_USERS + "/emails/gokuTestMock@dbz.com")).andDo(print());
      requestResult.andExpect(status().isOk());
      requestResult.andExpect(content().contentType("application/json;charset=UTF-8"));
      requestResult.andExpect(jsonPath("$.username", equalTo("gokuTestMock")));
      requestResult.andExpect(jsonPath("$.email", equalTo("gokuTestMock@dbz.com")));
      requestResult.andExpect(jsonPath("$.age", equalTo(49)));
      requestResult.andExpect(jsonPath("$.powerLevel", equalTo(30)));
      requestResult.andExpect(jsonPath("$.stamina", equalTo(1000)));
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception thrown.");
    }
  }

  /**
   * /dragonball/users/{id} (PUT) Tests updating an existing user in the
   * repository.
   */
  @Test
  public void putUsersUsernameTest() {

    // Normal flow
    try {
      // Setup mock object dragonBallUserServiceMock
      Mockito.doNothing().when(dragonBallUserServiceMock).updateDragonBallUser(dragonBallUserDto);
      when(dragonBallUserServiceMock.getDragonBallUser(dragonBallUsersList.get(0).getUsername()))
          .thenReturn(dragonBallUsersList.get(0));

      // Execute HTTP PUT on the /dragonball/users/{id} endpoint
      ResultActions requestResult =
          mockMvc.perform(put(API_V1_DRAGONBALL_USERS + dragonBallUsersList.get(0).getId())
              .contentType(MediaType.APPLICATION_JSON_UTF8)
              .content(JsonUtils.toJsonByteArray(dragonBallUsersList.get(0)))).andDo(print());
      requestResult.andExpect(status().isOk());

      verify(dragonBallUserServiceMock, times(1)).updateDragonBallUser(dragonBallUserDto);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception thrown.");
    }
  }

  /**
   * /dragonball/users/{id} (PUT) Tests trying to update a non existing user in
   * the repository.
   */
  @Test
  public void putUsersUsernameNotFoundExceptionTest() {

    // Exception flows
    try {
      // Setup mock object dragonBallUserServiceMock
      Mockito.doThrow(new KameHouseNotFoundException("User not found"))
          .when(dragonBallUserServiceMock).updateDragonBallUser(dragonBallUserDto);

      // Execute HTTP PUT on the /dragonball/users/{id} endpoint
      mockMvc.perform(put(API_V1_DRAGONBALL_USERS + dragonBallUser.getId())
          .contentType(MediaType.APPLICATION_JSON_UTF8)
          .content(JsonUtils.toJsonByteArray(dragonBallUser))).andDo(print());
      verify(dragonBallUserServiceMock, times(1)).updateDragonBallUser(dragonBallUserDto);
    } catch (Exception e) {
      if (!(e.getCause() instanceof KameHouseNotFoundException)) {
        e.printStackTrace();
        fail("Unexpected exception thrown.");
      }
    }
  }

  /**
   * /dragonball/users/{id} (PUT) Tests failing to update an existing user in the
   * repository with forbidden access.
   */
  @Test
  public void putUsersUsernameForbiddenExceptionTest() throws IOException, Exception {
    thrown.expect(NestedServletException.class);
    thrown.expectCause(IsInstanceOf.<Throwable>instanceOf(KameHouseBadRequestException.class));

    // Setup mock object dragonBallUserServiceMock
    Mockito.doNothing().when(dragonBallUserServiceMock).updateDragonBallUser(dragonBallUserDto);
    when(dragonBallUserServiceMock.getDragonBallUser(dragonBallUsersList.get(0).getUsername()))
        .thenReturn(dragonBallUsersList.get(0));

    // Execute HTTP PUT on the /dragonball/users/{id} endpoint
    ResultActions requestResult = mockMvc
        .perform(put(API_V1_DRAGONBALL_USERS + "/987").contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(JsonUtils.toJsonByteArray(dragonBallUsersList.get(0))))
        .andDo(print());
    requestResult.andExpect(status().is4xxClientError());
    verify(dragonBallUserServiceMock, times(0)).updateDragonBallUser(dragonBallUserDto);
    verify(dragonBallUserServiceMock, times(0))
        .getDragonBallUser(dragonBallUsersList.get(0).getUsername());
  }

  /**
   * /dragonball/users/{id} (DELETE) Tests for deleting an existing user from the
   * repository.
   */
  @Test
  public void deleteUsersUsernameTest() {

    // Normal flow
    try {
      // Setup mock object dragonBallUserServiceMock
      when(dragonBallUserServiceMock.deleteDragonBallUser(dragonBallUsersList.get(0).getId()))
          .thenReturn(dragonBallUsersList.get(0));

      // Execute HTTP DELETE on the /dragonball/users/{id} endpoint
      ResultActions requestResult =
          mockMvc.perform(delete(API_V1_DRAGONBALL_USERS + dragonBallUsersList.get(0).getId()))
              .andDo(print());
      requestResult.andExpect(status().isOk());
      requestResult.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
      requestResult
          .andExpect(content().bytes(JsonUtils.toJsonByteArray(dragonBallUsersList.get(0))));

      verify(dragonBallUserServiceMock, times(1))
          .deleteDragonBallUser(dragonBallUsersList.get(0).getId());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception thrown.");
    }
  }

  /**
   * /dragonball/users/{id} (DELETE) Tests for deleting an user not found in the
   * repository.
   */
  @Test
  public void deleteUsersUsernameNotFoundExceptionTest() {

    // Exception flows
    try {
      // Setup mock object dragonBallUserServiceMock
      Mockito.doThrow(new KameHouseNotFoundException("User not found"))
          .when(dragonBallUserServiceMock).deleteDragonBallUser(dragonBallUsersList.get(0).getId());

      // Execute HTTP DELETE on the /dragonball/users/{id} endpoint
      ResultActions requestResult =
          mockMvc.perform(delete(API_V1_DRAGONBALL_USERS + dragonBallUsersList.get(0).getId()))
              .andDo(print());
      requestResult.andExpect(status().is4xxClientError());
      verify(dragonBallUserServiceMock, times(1))
          .deleteDragonBallUser(dragonBallUsersList.get(0).getId());
    } catch (Exception e) {
      if (!(e.getCause() instanceof KameHouseNotFoundException)) {
        e.printStackTrace();
        fail("Unexpected exception thrown.");
      }
    }
  }

  /*
   * @Ignore("Disabled test example")
   *
   * @Test public void disabledTest() { // @Ignore disables the execution of the
   * test assertEquals("disabledTest not yet implemented", 0, 0);
   *
   * // Assert statements:
   *
   * // fail(message) assertTrue([message,] boolean condition) //
   * assertFalse([message,] boolean condition) assertEquals([message,] //
   * expected, actual) assertEquals([message,] expected, actual, tolerance) //
   * assertNull([message,] object) assertNotNull([message,] object) //
   * assertSame([message,] expected, actual) assertNotSame([message,] // expected,
   * actual) }
   */
}
