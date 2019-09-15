package com.nicobrest.kamehouse.admin.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nicobrest.kamehouse.admin.model.ApplicationUser;
import com.nicobrest.kamehouse.admin.service.ApplicationUserService;
import com.nicobrest.kamehouse.admin.service.dto.ApplicationUserDto;
import com.nicobrest.kamehouse.admin.testutils.ApplicationUserTestUtils;
import com.nicobrest.kamehouse.main.controller.AbstractControllerTest;
import com.nicobrest.kamehouse.main.exception.KameHouseBadRequestException;
import com.nicobrest.kamehouse.main.exception.KameHouseConflictException;
import com.nicobrest.kamehouse.main.exception.KameHouseNotFoundException;
import com.nicobrest.kamehouse.utils.JsonUtils;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.List;

/**
 * Unit tests for the ApplicationUserController class.
 *
 * @author nbrest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@WebAppConfiguration
public class ApplicationUserControllerTest extends AbstractControllerTest {

  private static ApplicationUser applicationUserMock = ApplicationUserTestUtils
      .getApplicationUserMock();
  private static List<ApplicationUser> applicationUsersMockList = ApplicationUserTestUtils
      .getApplicationUsersMockList();
  private static ApplicationUserDto applicationUserDtoMock = ApplicationUserTestUtils
      .getApplicationUserDtoMock();
  
  @InjectMocks
  private ApplicationUserController applicationUserController;

  @Mock(name = "applicationUserService")
  private ApplicationUserService applicationUserServiceMock;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Resets mock objects.
   */
  @Before
  public void beforeTest() {
    ApplicationUserTestUtils.initApplicationUserMocks();
    MockitoAnnotations.initMocks(this);
    Mockito.reset(applicationUserServiceMock);
    mockMvc = MockMvcBuilders.standaloneSetup(applicationUserController).build();
  }

  /**
   * Test getting all ApplicationUsers.
   */
  @Test
  public void getUsersTest() {
    when(applicationUserServiceMock.getAllUsers()).thenReturn(applicationUsersMockList);

    try {
      ResultActions requestResult = mockMvc.perform(get("/api/v1/admin/application/users")).andDo(
          print());
      requestResult.andExpect(status().isOk());
      requestResult.andExpect(content().contentType("application/json;charset=UTF-8"));
      requestResult.andExpect(jsonPath("$", hasSize(3)));
      requestResult.andExpect(jsonPath("$[0].id", equalTo(1001)));
      requestResult.andExpect(jsonPath("$[0].username", equalTo("goku")));
      requestResult.andExpect(jsonPath("$[0].email", equalTo("goku@dbz.com")));
      requestResult.andExpect(jsonPath("$[0].password", equalTo(null)));
      requestResult.andExpect(jsonPath("$[1].id", equalTo(1002)));
      requestResult.andExpect(jsonPath("$[1].username", equalTo("gohan")));
      requestResult.andExpect(jsonPath("$[1].email", equalTo("gohan@dbz.com")));
      requestResult.andExpect(jsonPath("$[1].password", equalTo(null)));
      requestResult.andExpect(jsonPath("$[2].id", equalTo(1003)));
      requestResult.andExpect(jsonPath("$[2].username", equalTo("goten")));
      requestResult.andExpect(jsonPath("$[2].email", equalTo("goten@dbz.com")));
      requestResult.andExpect(jsonPath("$[2].password", equalTo(null)));
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception thrown.");
    }
    verify(applicationUserServiceMock, times(1)).getAllUsers();
    verifyNoMoreInteractions(applicationUserServiceMock);
  }

  /**
   * Create a user test.
   */
  @Test
  public void postUsersTest() {
    try {
      Mockito.doReturn(applicationUserMock.getId()).when(applicationUserServiceMock).createUser(
          applicationUserDtoMock);
      when(applicationUserServiceMock.loadUserByUsername(applicationUserMock.getUsername()))
          .thenReturn(applicationUserMock);

      byte[] requestPayload = JsonUtils.toJsonByteArray(applicationUserDtoMock);
      ResultActions requestResult = mockMvc.perform(post("/api/v1/admin/application/users")
          .contentType(MediaType.APPLICATION_JSON_UTF8).content(requestPayload)).andDo(print());
      requestResult.andExpect(status().isCreated());
      requestResult.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
      requestResult.andExpect(content().bytes(JsonUtils.toJsonByteArray(applicationUserDtoMock
          .getId())));
      requestResult.andExpect(content().string(applicationUserDtoMock.getId().toString()));

      verify(applicationUserServiceMock, times(1)).createUser(applicationUserDtoMock);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception thrown.");
    }
  }

  /**
   * Create an user conflict exception test.
   */
  @Test
  public void postUsersConflictExceptionTest() throws Exception {
    thrown.expect(NestedServletException.class);
    thrown.expectCause(IsInstanceOf.<Throwable> instanceOf(KameHouseConflictException.class));
    Mockito.doThrow(new KameHouseConflictException("User already exists")).when(
        applicationUserServiceMock).createUser(applicationUserDtoMock);

    byte[] requestPayload = JsonUtils.toJsonByteArray(applicationUserDtoMock);
    ResultActions requestResult = mockMvc.perform(post("/api/v1/admin/application/users")
        .contentType(MediaType.APPLICATION_JSON_UTF8).content(requestPayload)).andDo(print());
    requestResult.andExpect(status().is4xxClientError());
    verify(applicationUserServiceMock, times(1)).createUser(applicationUserDtoMock);
  }

  /**
   * Get an application user test.
   */
  @Test
  public void getUserTest() {

    try {
      when(applicationUserServiceMock.loadUserByUsername("goku")).thenReturn(
          applicationUsersMockList.get(0));

      ResultActions requestResult = mockMvc.perform(get("/api/v1/admin/application/users/goku"))
          .andDo(print());
      requestResult.andExpect(status().isOk());
      requestResult.andExpect(content().contentType("application/json;charset=UTF-8"));
      requestResult.andExpect(jsonPath("$.id", equalTo(1001)));
      requestResult.andExpect(jsonPath("$.username", equalTo("goku")));
      requestResult.andExpect(jsonPath("$.email", equalTo("goku@dbz.com")));
      requestResult.andExpect(jsonPath("$.password", equalTo(null)));
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception thrown.");
    }
  }

  /**
   * Test get user not found exception.
   */
  @Test
  public void getUserNotFoundExceptionTest() throws Exception {
    thrown.expect(NestedServletException.class);
    thrown.expectCause(IsInstanceOf.<Throwable> instanceOf(KameHouseNotFoundException.class));

    Mockito.doThrow(new KameHouseNotFoundException("User trunks not found")).when(
        applicationUserServiceMock).loadUserByUsername("trunks");

    ResultActions requestResult = mockMvc.perform(get("/api/v1/admin/application/users/trunks"))
        .andDo(print());
    requestResult.andExpect(status().is4xxClientError());
    verify(applicationUserServiceMock, times(1)).loadUserByUsername("trunks");
  }

  /**
   * Update an user test.
   */
  @Test
  public void putUsersTest() {

    try {
      Mockito.doNothing().when(applicationUserServiceMock).updateUser(applicationUserDtoMock);

      byte[] requestPayload = JsonUtils.toJsonByteArray(applicationUserDtoMock);
      ResultActions requestResult = mockMvc.perform(put("/api/v1/admin/application/users/"
          + applicationUserDtoMock.getId()).contentType(MediaType.APPLICATION_JSON_UTF8).content(
              requestPayload)).andDo(print());
      requestResult.andExpect(status().isOk());
      verify(applicationUserServiceMock, times(1)).updateUser(any());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception thrown.");
    }
  }

  /**
   * Update an user with invalid path id. Exception expected.
   */
  @Test
  public void putUsersInvalidPathId() throws Exception {
    thrown.expect(NestedServletException.class);
    thrown.expectCause(IsInstanceOf.<Throwable> instanceOf(KameHouseBadRequestException.class));
    byte[] requestPayload = JsonUtils.toJsonByteArray(applicationUserDtoMock);
    ResultActions requestResult = mockMvc.perform(put("/api/v1/admin/application/users/"
        + applicationUserDtoMock.getId() + 1).contentType(MediaType.APPLICATION_JSON_UTF8).content(
            requestPayload)).andDo(print());
    requestResult.andExpect(status().is4xxClientError());
    verify(applicationUserServiceMock, times(0)).updateUser(any());
  }

  /**
   * Update an user not found test.
   */
  @Test
  public void putUsersUsernameNotFoundExceptionTest() throws Exception {
    thrown.expect(NestedServletException.class);
    thrown.expectCause(IsInstanceOf.<Throwable> instanceOf(KameHouseNotFoundException.class));
    Mockito.doThrow(new KameHouseNotFoundException("User not found")).when(
        applicationUserServiceMock).updateUser(applicationUserDtoMock);

    byte[] requestPayload = JsonUtils.toJsonByteArray(applicationUsersMockList.get(0));
    ResultActions requestResult = mockMvc.perform(put("/api/v1/admin/application/users/"
        + applicationUserDtoMock.getId()).contentType(MediaType.APPLICATION_JSON_UTF8).content(
            requestPayload)).andDo(print());
    requestResult.andExpect(status().is4xxClientError());
    verify(applicationUserServiceMock, times(1)).updateUser(applicationUserDtoMock);
  }

  /**
   * Delete an user test.
   */
  @Test
  public void deleteUserTest() {

    try {
      when(applicationUserServiceMock.deleteUser(applicationUsersMockList.get(0).getId()))
          .thenReturn(applicationUsersMockList.get(0));

      ResultActions requestResult = mockMvc.perform(delete("/api/v1/admin/application/users/"
          + applicationUsersMockList.get(0).getId())).andDo(print());
      requestResult.andExpect(status().isOk());
      requestResult.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
      requestResult.andExpect(jsonPath("$.id", equalTo(1001)));
      requestResult.andExpect(jsonPath("$.username", equalTo("goku")));
      requestResult.andExpect(jsonPath("$.email", equalTo("goku@dbz.com")));
      requestResult.andExpect(jsonPath("$.password", equalTo(null)));

      verify(applicationUserServiceMock, times(1)).deleteUser(applicationUsersMockList.get(0)
          .getId());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception thrown.");
    }
  }

  /**
   * Delete an user not found test.
   */
  @Test
  public void deleteUserNotFoundExceptionTest() throws Exception {
    thrown.expect(NestedServletException.class);
    thrown.expectCause(IsInstanceOf.<Throwable> instanceOf(KameHouseNotFoundException.class));
    Mockito.doThrow(new KameHouseNotFoundException("User not found")).when(
        applicationUserServiceMock).deleteUser(applicationUsersMockList.get(0).getId());

    ResultActions requestResult = mockMvc.perform(delete("/api/v1/admin/application/users/"
        + applicationUsersMockList.get(0).getId())).andDo(print());
    requestResult.andExpect(status().is4xxClientError());
    verify(applicationUserServiceMock, times(1)).deleteUser(applicationUsersMockList.get(0)
        .getId());
  }
}
