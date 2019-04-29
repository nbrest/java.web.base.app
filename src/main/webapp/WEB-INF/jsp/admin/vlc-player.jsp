<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*,java.text.*"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html>
<head>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width">
<meta name="author" content="nbrest">

<title>kame House - VLC Player test</title>
<link rel="icon" type="img/ico" href="${pageContext.request.contextPath}/img/favicon.ico" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/css/bootstrap.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/general.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/test-general.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css" />
</head>
<body>
  <div id="headerContainer"></div>
  <div class="container main">
    <div>
      <h3 id="ehcache-header">VLC Player test page</h3>
    </div>
    <hr>
    <br><h5>Request Output</h5>
    <div id="api-call-output"></div>
    
    <br><h5>VlcRc Commands</h5>
    
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'pl_previous')"
      value="prev"
      class="btn btn-outline-success btn-margins" />
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'seek', '-1m')"
      value="&lt; 1m"
      class="btn btn-outline-success btn-margins" />
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'pl_pause')"
      value="play/pause"
      class="btn btn-outline-success btn-margins" />
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'seek', '+1m')"
      value="1m &gt;"
      class="btn btn-outline-success btn-margins" />
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'pl_next')"
      value="next"
      class="btn btn-outline-success btn-margins" />
      
    <br>
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'pl_stop')"
      value="stop"
      class="btn btn-outline-danger btn-margins" />
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'volume', '-15')"
      value="vol down"
      class="btn btn-outline-primary btn-margins" />
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'volume', '+15')"
      value="vol up"
      class="btn btn-outline-primary btn-margins" />
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'volume', '0')"
      value="mute"
      class="btn btn-outline-primary btn-margins" />
      
    <br>
    <input type="button" onclick="executeGet('/kame-house/api/v1/vlc-rc/players/localhost/status')"
      value="status"
      class="btn btn-outline-warning btn-margins" />
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'pl_random')"
      value="random"
      class="btn btn-outline-info btn-margins" />
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'pl_loop')"
      value="loop"
      class="btn btn-outline-info btn-margins" />
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'pl_repeat')"
      value="repeat"
      class="btn btn-outline-info btn-margins" />
      
    <br>
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'fullscreen')"
      value="fullscreen"
      class="btn btn-outline-secondary btn-margins" />
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'aspectratio', '16:9')"
      value="16:9"
      class="btn btn-outline-secondary btn-margins" />
    <input type="button" onclick="executeVlcRcCommandPost('/kame-house/api/v1/vlc-rc/players/localhost/commands', 'aspectratio', '4:3')"
      value="4:3"
      class="btn btn-outline-secondary btn-margins" />
      
    <br><h5>System Commands</h5>
    
    <select class="custom-select sources btn-margins" id="playlist-category-dropdown" name="playlist-category" onchange="populateVideoPlaylists()"></select>  
    <select class="custom-select sources btn-margins" id="playlist-dropdown" name="playlist"></select>  
    <input type="button" onclick="executeAdminVlcPostWithSelectedPlaylist('/kame-house/api/v1/admin/vlc', 'start')"
      value="open"
      class="btn btn-outline-primary btn-margins" />
    <br>
    <input type="button" onclick="executeAdminVlcPost('/kame-house/api/v1/admin/vlc', 'start', 'D:\\Series\\game_of_thrones\\GameOfThrones.m3u')"
      value="open GoT win"
      class="btn btn-outline-primary btn-margins" /> 
    <input type="button" onclick="executeAdminVlcPost('/kame-house/api/v1/admin/vlc', 'start', '/home/nbrest/Videos/lleyton.hewitt.m3u')"
      value="open LH lx"
      class="btn btn-outline-primary btn-margins" />
    <br>
    <input type="button" onclick="executeGet('/kame-house/api/v1/admin/vlc')"
      value="status"
      class="btn btn-outline-warning btn-margins" /> 
    <input type="button" onclick="executeDelete('/kame-house/api/v1/admin/vlc', null)"
      value="close vlc"
      class="btn btn-outline-danger btn-margins" />
     
    <br><h5>Server shutdown</h5>
    
    <input type="button" onclick="executeAdminShutdownPost('/kame-house/api/v1/admin/shutdown', 'set', 5400)"
      value="Shutdown in 90 min"
      class="btn btn-outline-danger btn-margins" />  
    <input type="button" onclick="executeAdminShutdownPost('/kame-house/api/v1/admin/shutdown', 'set', 60)"
      value="Shutdown in 1 min"
      class="btn btn-outline-danger btn-margins" /> 
    <input type="button" onclick="executeDelete('/kame-house/api/v1/admin/shutdown', null)"
      value="Cancel Shutdown"
      class="btn btn-outline-success btn-margins" />

  </div>
  <div id="footerContainer"></div>
  <script src="${pageContext.request.contextPath}/lib/js/jquery-2.0.3.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/importHeaderFooter.js"></script>
  <script src="${pageContext.request.contextPath}/js/admin.vlc.player.js"></script>
</body>
</html>
