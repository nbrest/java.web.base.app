/** 
 * Represents the playlist browser component in vlc-player page.
 * It doesn't control the currently active playlist.
 * 
 * Dependencies: logger, apiCallTable
 * 
 * @author nbrest
 */
function PlaylistBrowser(vlcPlayer) {

  let self = this;
  this.vlcPlayer = vlcPlayer;
  this.videoPlaylists = [];
  this.videoPlaylistCategories = [];
  this.currentPlaylist = {};
  const mediaVideoAllPlaylistsUrl = '/kame-house/api/v1/media/video/playlists';
  const mediaVideoPlaylistUrl = '/kame-house/api/v1/media/video/playlists/file';

  /** Returns the selected playlist from the dropdowns. */
  this.getSelectedPlaylist = function getSelectedPlaylist() {
    logger.debugFunctionCall();
    let playlistSelected = document.getElementById("playlist-dropdown").value;
    logger.debug("Playlist selected: " + playlistSelected);
    return playlistSelected;
  }

  /** Populate playlist categories dropdown. */
  this.populateVideoPlaylistCategories = function populateVideoPlaylistCategories() {
    logger.debugFunctionCall();
    let playlistDropdown = $('#playlist-dropdown');
    playlistDropdown.empty();
    playlistDropdown.append('<option selected="true" disabled>Playlist</option>');
    playlistDropdown.prop('selectedIndex', 0);
    let playlistCategoryDropdown = $('#playlist-category-dropdown');
    playlistCategoryDropdown.empty();
    playlistCategoryDropdown.append('<option selected="true" disabled>Playlist Category</option>');
    playlistCategoryDropdown.prop('selectedIndex', 0);
    apiCallTable.get(mediaVideoAllPlaylistsUrl,
      function (responseBody, responseCode, responseDescription) {
        self.videoPlaylists = responseBody;
        self.videoPlaylistCategories = [...new Set(self.videoPlaylists.map(playlist => playlist.category))];
        logger.trace("Playlists: " + JSON.stringify(self.videoPlaylists));
        logger.trace("Playlist categories: " + self.videoPlaylistCategories);
        $.each(self.videoPlaylistCategories, function (key, entry) {
          let category = entry;
          let categoryFormatted = category.replace(/\\/g, ' | ').replace(/\//g, ' | ');
          playlistCategoryDropdown.append($('<option></option>').attr('value', entry).text(categoryFormatted));
        });
      },
      function (responseBody, responseCode, responseDescription) {
        apiCallTable.displayResponseData("Error populating video playlist categories", responseCode);
      });
  }

  /** Populate video playlists dropdown when a playlist category is selected. */
  this.populateVideoPlaylists = function populateVideoPlaylists() {
    logger.debugFunctionCall();
    let playlistCategoriesList = document.getElementById('playlist-category-dropdown');
    let selectedPlaylistCategory = playlistCategoriesList.options[playlistCategoriesList.selectedIndex].value;
    let playlistDropdown = $('#playlist-dropdown');
    playlistDropdown.empty();
    playlistDropdown.append('<option selected="true" disabled>Playlist</option>');
    playlistDropdown.prop('selectedIndex', 0);
    logger.debug("Selected Playlist Category: " + selectedPlaylistCategory);
    $.each(self.videoPlaylists, function (key, entry) {
      if (entry.category === selectedPlaylistCategory) {
        let playlistName = entry.name;
        playlistName = playlistName.replace(/.m3u+$/, "");
        playlistDropdown.append($('<option></option>').attr('value', entry.path).text(playlistName));
      }
    });
  }

  /** Load the selected playlist's content in the view */
  this.loadPlaylistContent = function loadPlaylistContent() {
    let playlistFilename = self.getSelectedPlaylist();
    logger.debug("Getting content for " + playlistFilename);
    let requestParam = "path=" + playlistFilename;
    apiCallTable.getUrlEncoded(mediaVideoPlaylistUrl, requestParam,
      function (responseBody, responseCode, responseDescription) {
        self.currentPlaylist = responseBody;
        self.populatePlaylistBrowserTable();
      },
      function (responseBody, responseCode, responseDescription) {
        apiCallTable.displayResponseData("Error getting playlist content", responseCode);
      });
  }

  /** Play selected file in the specified VlcPlayer. */
  this.playSelectedPlaylist = function playSelectedPlaylist() {
    logger.debugFunctionCall();
    let playlist = self.getSelectedPlaylist();
    self.vlcPlayer.playFile(playlist);
  }

  /** Populate the playlist table for browsing. */
  this.populatePlaylistBrowserTable = function populatePlaylistBrowserTable() {
    logger.traceFunctionCall();
    // Clear playlist browser table content. 
    $("#playlist-browser-table-body").empty();
    // Add the new playlist browser items received from the server.
    let $playlistTableBody = $('#playlist-browser-table-body');
    let playlistTableRow;
    if (isEmpty(self.currentPlaylist)) {
      playlistTableRow = $('<tr>').append($('<td>').text("No playlist to browse loaded yet or unable to sync. Mada mada dane :)"));
      $playlistTableBody.append(playlistTableRow);
    } else {
      for (let i = 0; i < self.currentPlaylist.files.length; i++) {
        let playlistElementButton = $('<button>');
        playlistElementButton.addClass("btn btn-outline-danger btn-borderless btn-playlist");
        playlistElementButton.text(self.currentPlaylist.files[i]);
        playlistElementButton.click({
          name: self.currentPlaylist.files[i]
        }, self.clickEventOnPlaylistBrowserRow);
        playlistTableRow = $('<tr id=playlist-browser-entry-' + [i] + '>').append($('<td>').append(playlistElementButton));
        $playlistTableBody.append(playlistTableRow);
      }
    }
  }

  /** Play the clicked element from the playlist. */
  this.clickEventOnPlaylistBrowserRow = function clickEventOnPlaylistBrowserRow(event) {
    let filename = event.data.name;
    logger.info("Play selected playlist browser file : " + filename);
    self.vlcPlayer.playFile(filename);
  }
}
