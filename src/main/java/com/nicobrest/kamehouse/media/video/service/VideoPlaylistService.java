package com.nicobrest.kamehouse.media.video.service;

import com.nicobrest.kamehouse.media.video.model.Playlist;
import com.nicobrest.kamehouse.utils.SystemPropertiesUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class VideoPlaylistService {

  private static final Logger logger = LoggerFactory.getLogger(VideoPlaylistService.class);

  /**
   * Get all video playlists.
   */
  public List<Playlist> getAllVideoPlaylists() {
    String userHome = SystemPropertiesUtils.getUserHome();
    String videoPlaylistsHome;
    if (SystemPropertiesUtils.IS_WINDOWS_HOST) {
      videoPlaylistsHome = userHome + "\\git\\texts\\video_playlists\\windows\\niko4tbusb";
    } else {
      videoPlaylistsHome = userHome + "/git/texts/video_playlists/linux/niko4tbusb";
    }
    List<Playlist> videoPlaylists = new ArrayList<Playlist>();
    try (Stream<Path> filePaths = Files.walk(Paths.get(videoPlaylistsHome))) {
      Iterator<Path> filePathsIterator = filePaths.iterator();
      while (filePathsIterator.hasNext()) {
        Path filePath = filePathsIterator.next();
        if (!filePath.toFile().isDirectory()) {
          Path fileName = filePath.getFileName();
          if (fileName != null) {
            Playlist playlist = new Playlist();
            playlist.setName(fileName.toString());
            playlist.setPath(filePath.toString());
            videoPlaylists.add(playlist);
          }
        }
      }
    } catch (IOException e) {
      logger.error("An exception occurred while getting all the video playlists. Message: " + e
          .getMessage());
      e.printStackTrace();
    }
    return videoPlaylists;
  }
}
