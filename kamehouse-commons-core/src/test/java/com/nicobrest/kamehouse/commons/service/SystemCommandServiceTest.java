package com.nicobrest.kamehouse.commons.service;

import static org.powermock.api.mockito.PowerMockito.when;
import com.nicobrest.kamehouse.commons.model.kamehousecommand.KameHouseSystemCommand;
import com.nicobrest.kamehouse.commons.model.systemcommand.SystemCommand;
import com.nicobrest.kamehouse.commons.model.systemcommand.VncDoKeyPressSystemCommand;
import com.nicobrest.kamehouse.commons.model.systemcommand.VncDoMouseClickSystemCommand;
import com.nicobrest.kamehouse.commons.model.systemcommand.VncDoTypeSystemCommand;
import com.nicobrest.kamehouse.commons.testutils.SystemCommandOutputTestUtils;
import com.nicobrest.kamehouse.commons.utils.ProcessUtils;
import com.nicobrest.kamehouse.commons.utils.PropertiesUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for the SystemCommandService class.
 * 
 * @author nbrest
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ PropertiesUtils.class, ProcessUtils.class })
public class SystemCommandServiceTest {

  private SystemCommandService systemCommandService;
  private SystemCommandOutputTestUtils testUtils = new SystemCommandOutputTestUtils();
  private static final List<String> EMPTY_LIST = new ArrayList<>();
  private static final String COMPLETED = "completed";
  private static final String FAILED = "failed";
  private static final String RUNNING = "running";
  private static final List<String> INPUT_STREAM_LIST = Arrays.asList("/home /bin /opt");

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void before() {
    PowerMockito.mockStatic(PropertiesUtils.class, ProcessUtils.class);
    systemCommandService = PowerMockito.spy(new SystemCommandService());
    when(PropertiesUtils.isWindowsHost()).thenReturn(true);
  }

  /**
   * Executes process successful test.
   */
  @Test
  public void execKameHouseSystemCommandTest() throws Exception {
    setupProcessStreamMocks(INPUT_STREAM_LIST.get(0), "");
    KameHouseSystemCommand kameHouseSystemCommand = new TestKameHouseSystemCommand();

    List<SystemCommand.Output> returnedList = systemCommandService.execute(kameHouseSystemCommand);

    testUtils.assertCommandExecutedMatch(kameHouseSystemCommand, returnedList);
    testUtils.assertSystemCommandOutputFields(0, -1, COMPLETED, INPUT_STREAM_LIST, EMPTY_LIST,
        returnedList.get(0));
    testUtils.assertSystemCommandOutputFields(0, -1, COMPLETED, EMPTY_LIST, EMPTY_LIST,
        returnedList.get(1));
    testUtils.assertSystemCommandOutputFields(0, -1, COMPLETED, EMPTY_LIST, EMPTY_LIST,
        returnedList.get(2));
  }

  /**
   * Executes process successful for linux test.
   */
  @Test
  public void execLinuxCommandTest() throws Exception {
    when(PropertiesUtils.isWindowsHost()).thenReturn(false);
    setupProcessStreamMocks(INPUT_STREAM_LIST.get(0), ""); 
    List<SystemCommand> systemCommands = Arrays.asList(new VncDoKeyPressSystemCommand("9"));

    List<SystemCommand.Output> returnedList = systemCommandService.execute(systemCommands);

    testUtils.assertCommandExecutedMatch(systemCommands, returnedList);
    testUtils.assertSystemCommandOutputFields(0, -1, COMPLETED, INPUT_STREAM_LIST, EMPTY_LIST,
        returnedList.get(0));
  }

  /**
   * Executes process with failing VncDo command test.
   */
  @Test
  public void execVncDoFailedTest() throws Exception {
    List<String> errorStream = Arrays.asList("no errors");
    setupProcessStreamMocks(INPUT_STREAM_LIST.get(0), errorStream.get(0));
    when(ProcessUtils.getExitValue(Mockito.any())).thenReturn(1);
    List<SystemCommand> systemCommands = Arrays.asList(new VncDoKeyPressSystemCommand("esc"));

    List<SystemCommand.Output> returnedList = systemCommandService.execute(systemCommands);

    testUtils.assertCommandExecutedMatch(systemCommands, returnedList);
    testUtils.assertSystemCommandOutputFields(1, -1, FAILED, INPUT_STREAM_LIST, errorStream,
        returnedList.get(0));
  }

  /**
   * Executes daemon process successful test.
   */
  @Test
  public void execDaemonTest() throws Exception {
    setupProcessStreamMocks("", "");
    SystemCommand systemCommand = new TestDaemonCommand("9");

    SystemCommand.Output returnedCommandOutput = systemCommandService.execute(systemCommand);

    testUtils.assertCommandExecutedMatch(systemCommand, returnedCommandOutput);
    testUtils.assertSystemCommandOutputFields(-1, -1, RUNNING, null, null, returnedCommandOutput);
  }

  /**
   * Executes process throwing an IOException test.
   */
  @Test
  public void execIOExceptionTest() throws Exception {
    when(ProcessUtils.getInputStream(Mockito.any())).thenThrow(IOException.class);
    List<String> errorStream = Arrays.asList("An error occurred executing the command. Message: " +
        "null");
    List<SystemCommand> systemCommands = Arrays.asList(new VncDoKeyPressSystemCommand("9"));

    List<SystemCommand.Output> returnedList = systemCommandService.execute(systemCommands);

    testUtils.assertCommandExecutedMatch(systemCommands, returnedList);
    testUtils.assertSystemCommandOutputFields(1, -1, FAILED, null, errorStream,
        returnedList.get(0));
  }

  /**
   * Setup mock input and error streams.
   */
  private void setupProcessStreamMocks(String inputStreamContent, String errorStreamContent) {
    InputStream processInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
    InputStream processErrorStream = new ByteArrayInputStream(errorStreamContent.getBytes());
    when(ProcessUtils.getInputStream(Mockito.any())).thenReturn(processInputStream);
    when(ProcessUtils.getErrorStream(Mockito.any())).thenReturn(processErrorStream);
  }

  /**
   * Test KameHouseSystemCommand to test the SystemCommandService.
   *
   */
  public static class TestKameHouseSystemCommand extends KameHouseSystemCommand {

    /**
     * Test KameHouseSystemCommand to test the SystemCommandService.
     */
    public TestKameHouseSystemCommand() {
      systemCommands.add(new VncDoMouseClickSystemCommand("1", "400", "400"));
      systemCommands.add(new VncDoKeyPressSystemCommand("1"));
      systemCommands.add(new VncDoTypeSystemCommand("22"));
    }
  }

  /**
   * Test Daemon command to test the SystemCommandService.
   */
  public static class TestDaemonCommand extends VncDoKeyPressSystemCommand {

    /**
     * Test Daemon command.
     */
    public TestDaemonCommand(String key) {
      super(key);
      isDaemon = true;
    }
  }
}
