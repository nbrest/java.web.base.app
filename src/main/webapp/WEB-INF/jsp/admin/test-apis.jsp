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

<title>kame House - JSP App</title>
<link rel="icon" type="img/ico" href="${pageContext.request.contextPath}/img/favicon.ico" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/css/bootstrap.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/general.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css" />
</head>
<body>
  <div id="headerContainer"></div>
  <div class="container main">
    <div>
      <h3 id="ehcache-header">Test APIs</h3>
    </div>
    <hr>
    <br><br><h5>Request Output</h5> <br>
    <div id="api-call-output"></div>
    <br><h5>/api/v1/admin/vlc Requests</h5><br>
    <input type="button" onclick="executeGet('/kame-house/api/v1/admin/vlc')"
      value="/kame-house/api/v1/admin/vlc GET"
      class="btn btn-outline-success" />
    <br><br> 
    <input type="button" onclick="executeAdminVlcPost('/kame-house/api/v1/admin/vlc', 'start', 'D:\\Series\\game_of_thrones\\GameOfThrones.m3u')"
      value="/kame-house/api/v1/admin/vlc POST Win"
      class="btn btn-outline-primary" />
    <br><br>
    <input type="button" onclick="executeAdminVlcPost('/kame-house/api/v1/admin/vlc', 'start', '/home/nbrest/Videos/lleyton.hewitt.m3u')"
      value="/kame-house/api/v1/admin/vlc POST Linux"
      class="btn btn-outline-primary" />
    <br><br>
    <input type="button" onclick="executeDelete('/kame-house/api/v1/admin/vlc', null)"
      value="/kame-house/api/v1/admin/vlc DELETE"
      class="btn btn-outline-danger" />
    <br><br><h5>/api/v1/dragonball Requests</h5><br>
    <input type="button" onclick="executeGet('/kame-house/api/v1/dragonball/users')"
      value="/kame-house/api/v1/dragonball/users GET"
      class="btn btn-outline-success" />
    <br><br>
    <input type="button" onclick="executeGet('/kame-house/api/v1/dragonball/users/username/goku')"
      value="/kame-house/api/v1/dragonball/users/username/goku GET"
      class="btn btn-outline-success" />
    <br><br>
    
    <hr>
    <div id="api-call-output"></div>
  </div>
  <div id="footerContainer"></div>
  <script src="${pageContext.request.contextPath}/lib/js/jquery-2.0.3.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/importHeaderFooter.js"></script>
  <script src="${pageContext.request.contextPath}/js/admin.test.apis.js"></script>
</body>
</html>
