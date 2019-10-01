package com.nicobrest.kamehouse.admin.testutils;

import static org.junit.Assert.assertEquals;

import com.nicobrest.kamehouse.admin.model.SessionStatus;
import com.nicobrest.kamehouse.main.testutils.AbstractTestUtils;
import com.nicobrest.kamehouse.main.testutils.TestUtils;

/**
 * Test data and common test methods to test SessionStatus in all layers of
 * the application.
 * 
 * @author nbrest
 *
 */
public class SessionStatusTestUtils extends AbstractTestUtils<SessionStatus, Object>
    implements TestUtils<SessionStatus, Object> {

  @Override
  public void initTestData() {
    initSingleTestData();
  }

  @Override
  public void assertEqualsAllAttributes(SessionStatus expected, SessionStatus returned) {
    assertEquals(expected.getUsername(), returned.getUsername());
    assertEquals(expected.getFirstName(), returned.getFirstName());
    assertEquals(expected.getLastName(), returned.getLastName());
  }

  private void initSingleTestData() {
    singleTestData = new SessionStatus();
    singleTestData.setUsername("anonymousUser");
  }
}
