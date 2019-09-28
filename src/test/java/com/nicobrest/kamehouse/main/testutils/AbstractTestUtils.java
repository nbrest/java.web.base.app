package com.nicobrest.kamehouse.main.testutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.nicobrest.kamehouse.main.dao.Identifiable;

import java.io.InputStream;
import java.util.List;

/**
 * Abstract class to inherit by Test Utils classes.
 * 
 * @author nbrest
 *
 */
public abstract class AbstractTestUtils<T, D> implements TestUtils<T, D> {

  protected T singleTestData = null;
  protected List<T> testDataList = null;
  protected D testDataDto = null;

  /**
   * Get the input stream of the specified resource. Can be used to load files
   * such as vlcrc/vlc-rc-status.json on test cases.
   */
  public static InputStream getInputStreamFromResource(String resourceName) {
    ClassLoader classLoader = AbstractTestUtils.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(resourceName);
    return inputStream;
  }
  
  protected static void assertEqualsAsString(Object expected, Object returned) {
    assertEquals(expected.toString(), returned.toString());
  }

  @Override
  public T getSingleTestData() {
    return singleTestData;
  }

  @Override
  public List<T> getTestDataList() {
    return testDataList;
  }

  @Override
  public D getTestDataDto() {
    return testDataDto;
  }

  @Override
  public void setIds() {
    if (testDataDto != null) {
      Identifiable identifiableDto = (Identifiable) testDataDto;
      identifiableDto.setId(100L);
    }
    if (testDataList != null) {
      for (int i = 0; i < testDataList.size(); i++) {
        Identifiable identifiableTestDataEntry = (Identifiable) testDataList.get(i);
        Long id = 100L + i;
        identifiableTestDataEntry.setId(id);
      }
    }
  }

  @Override
  public void removeIds() {
    if (testDataDto != null) {
      Identifiable identifiableDto = (Identifiable) testDataDto;
      identifiableDto.setId(null);
    }
    if (testDataList != null) {
      for (int i = 0; i < testDataList.size(); i++) {
        Identifiable identifiableTestDataEntry = (Identifiable) testDataList.get(i);
        identifiableTestDataEntry.setId(null);
      }
    }
  }

  @Override
  public void assertEqualsAllAttributes(T expectedEntity, T returnedEntity) {
    fail("This method needs to be overriden in the concrete test utils to be used.");
  }

  @Override
  public void assertEqualsAllAttributesList(List<T> expectedList, List<T> returnedList) {
    if (expectedList == null || returnedList == null) {
      fail("Received a null list");
    }
    if (expectedList.size() != returnedList.size()) {
      fail("Lists have different sizes");
    }
    for (int i = 0; i < expectedList.size(); i++) {
      assertEqualsAllAttributes(expectedList.get(i), returnedList.get(i));
    }
  }
}
