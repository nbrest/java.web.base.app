package com.nicobrest.kamehouse.vlcrc.testutils;

import com.nicobrest.kamehouse.main.testutils.AbstractTestUtils;
import com.nicobrest.kamehouse.main.testutils.TestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test data and common test methods to test DragonBallUsers in all layers of
 * the application.
 * 
 * @author nbrest
 *
 */
public class VlcRcPlaylistTestUtils extends AbstractTestUtils<List<Map<String, Object>>, Object>
    implements TestUtils<List<Map<String, Object>>, Object> {
  
  @Override
  public void initTestData() {
    initSingleTestData();
  }

  private void initSingleTestData() {
    singleTestData = new ArrayList<>();
    Map<String, Object> playlistItem1 = new HashMap<>();
    playlistItem1.put("id", 1);
    playlistItem1.put("name", "Lleyton Hewitt- Brash teenager to Aussie great.mp4");
    playlistItem1.put("uri", "file:///home/nbrest/Videos/Lleyton%20"
        + "Hewitt-%20Brash%20teenager%20to%20Aussie%20great.mp4");
    playlistItem1.put("duration", 281);
    singleTestData.add(playlistItem1);
    Map<String, Object> playlistItem2 = new HashMap<>();
    playlistItem2.put("id", 2);
    playlistItem2.put("name", "Lleyton Hewitt Special.mp4");
    playlistItem2.put("uri", "file:///home/nbrest/Videos/Lleyton%20Hewitt%20Special.mp4");
    playlistItem2.put("duration", 325);
    singleTestData.add(playlistItem2);
  }
}
