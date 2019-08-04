<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*,java.text.*"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width">
<meta name="author" content="nbrest">

<title>KameHouse - Admin</title>
<link rel="icon" type="img/ico" href="/kame-house/img/favicon.ico" />
<link rel="stylesheet" href="/kame-house/lib/css/bootstrap.min.css" />
<link rel="stylesheet" href="/kame-house/css/global.css" />
</head>
<body>
  <div id="headerContainer"></div>
  <div id="banner" class="banner-sunset">
  <div class="default-layout banner-text">
    <h1>Admin</h1>
    <p>Perform administrative tasks on KameHouse</p>
  </div>
  </div>
  <div class="default-layout home-links">
    <br>
    <input type="button" value="Server Management" class="btn btn-block btn-outline-secondary"
      onclick="window.location.href='server-management'">
    <br>
    <input type="button" value="EhCache" class="btn btn-block btn-outline-secondary"
      onclick="window.location.href='ehcache'">
    <br>
    <input type="button" value="Test APIs" class="btn btn-block btn-outline-secondary"
      onclick="window.location.href='test-apis'">
  </div>
  <div id="footerContainer"></div>
  <script src="/kame-house/lib/js/jquery-2.0.3.min.js"></script>
  <script src="/kame-house/js/header-footer/headerFooter.js"></script>
</body>
</html>
