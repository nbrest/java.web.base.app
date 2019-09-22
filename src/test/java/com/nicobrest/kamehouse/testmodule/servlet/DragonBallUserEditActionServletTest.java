package com.nicobrest.kamehouse.testmodule.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.nicobrest.kamehouse.testmodule.service.DragonBallUserService;
import com.nicobrest.kamehouse.testmodule.servlet.DragonBallUserEditActionServlet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * Test class for the DragonBallUserEditActionServlet.
 * 
 * @author nbrest
 *
 */
public class DragonBallUserEditActionServletTest {

  @Mock
  private static DragonBallUserService dragonBallUserServiceMock;

  private MockHttpServletRequest request = new MockHttpServletRequest();
  private MockHttpServletResponse response = new MockHttpServletResponse();

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Tests the method to edit a DragonBallUser from the system through the
   * servlet.
   */
  @Test
  public void doPostTest() throws ServletException, IOException {
    DragonBallUserEditActionServlet dragonBallUserEditActionServlet =
        new DragonBallUserEditActionServlet();
    DragonBallUserEditActionServlet.setDragonBallUserService(dragonBallUserServiceMock);
    request.setParameter("id", "100");
    request.setParameter("username", "goku");
    request.setParameter("email", "goku@dbz.com");
    request.setParameter("age", "100");
    request.setParameter("stamina", "100");
    request.setParameter("powerLevel", "100");
    doNothing().when(dragonBallUserServiceMock).update(any());

    dragonBallUserEditActionServlet.doPost(request, response);

    verify(dragonBallUserServiceMock, times(1)).update(any());
    assertEquals("users-list", response.getRedirectedUrl());
  }
}
