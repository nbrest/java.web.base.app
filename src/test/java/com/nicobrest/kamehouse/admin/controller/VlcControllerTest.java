package com.nicobrest.kamehouse.admin.controller;

import com.nicobrest.kamehouse.admin.model.admincommand.VlcStartAdminCommand;
import com.nicobrest.kamehouse.admin.model.admincommand.VlcStatusAdminCommand;
import com.nicobrest.kamehouse.admin.model.admincommand.VlcStopAdminCommand;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

/**
 * Unit tests for the VlcController class.
 * 
 * @author nbrest
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@WebAppConfiguration
public class VlcControllerTest extends AbstractAdminCommandControllerTest {

  @InjectMocks
  private VlcController adminVlcController;

  @Before
  public void beforeTest() {
    adminCommandControllerTestSetup();
    mockMvc = MockMvcBuilders.standaloneSetup(adminVlcController).build();
  }

  /**
   * Start VLC player successful test.
   */
  @Test
  public void startVlcPlayerTest() throws Exception {
    executePostAdminCommandTest("/api/v1/admin/vlc?file=src/test/resources/media.video/"
        + "playlists/heroes/marvel/marvel.m3u", VlcStartAdminCommand.class);
  }

  /**
   * Start vlc exception test.
   */
  @Test
  public void startVlcExceptionTest() throws IOException, Exception {
    executePostInvalidAdminCommandTest("/api/v1/admin/vlc?file=invalid-file");
  }

  /**
   * Stop VLC player successful test.
   */
  @Test
  public void stopVlcPlayerTest() throws Exception {
    executeDeleteAdminCommandTest("/api/v1/admin/vlc", VlcStopAdminCommand.class);
  }

  /**
   * Stop VLC server error test.
   */
  @Test
  public void stopVlcPlayerServerErrorTest() throws Exception {
    executeDeleteServerErrorAdminCommandTest("/api/v1/admin/vlc", VlcStopAdminCommand.class);
  }

  /**
   * Status VLC successful test.
   */
  @Test
  public void statusVlcPlayerTest() throws Exception {
    executeGetAdminCommandTest("/api/v1/admin/vlc", VlcStatusAdminCommand.class);
  }
}