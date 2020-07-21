/**
 * VLC Player page functions.
 * 
 * Dependencies: logger, vlcPlayer, playlistBrowser
 * 
 * @author nbrest
 */

/** ----- Global variables ---------------------------------------------------------------- */
var vlcPlayer;
var playlistBrowser;

/** Main function. */
var main = () => {
  loadVlcPlayer();
  loadPlaylistBrowser(); 
  moduleUtils.waitForModules(["logger", "vlcPlayer", "playlistBrowser"], () => {
    logger.info("Started initializing VLC Player");
    playlistBrowser.populateVideoPlaylistCategories();
    vlcPlayer.init();
  });
};

function loadVlcPlayer() {
  moduleUtils.loadWebSocketKameHouse();
  $.getScript("/kame-house/js/vlc-player/vlc-player.js", (data, textStatus, jqxhr) => {
    moduleUtils.waitForModules(["logger", "apiCallTable", "webSocketKameHouse"], () => {
      vlcPlayer = new VlcPlayer("localhost");
      moduleUtils.setModuleLoaded("vlcPlayer");
    });
  });
}

function loadPlaylistBrowser() {
  $.getScript("/kame-house/js/media/video/playlist-browser.js", (data, textStatus, jqxhr) => {
    moduleUtils.waitForModules(["logger", "apiCallTable", "vlcPlayer"], () => {
      playlistBrowser = new PlaylistBrowser(vlcPlayer);
      moduleUtils.setModuleLoaded("playlistBrowser");
    });
  });
}

/** Call main. */
$(document).ready(main);