<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*,java.text.*"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width">
<meta name="author" content="nbrest">

<title>KameHouse - Test Module</title>
<link rel="icon" type="img/ico" href="/kame-house/img/favicon.ico" />
<link rel="stylesheet" href="/kame-house/lib/css/bootstrap.min.css" />
<link rel="stylesheet" href="/kame-house/css/general.css" />
<link rel="stylesheet" href="/kame-house/css/header.css" />
<link rel="stylesheet" href="/kame-house/css/footer.css" />
</head>
<body>
  <div id="headerContainer"></div>
  <section id="banner">
  <div class="container">
    <h1>KameHouse - Test Module</h1>
    <p>Test Module of KameHouse</p>
  </div>
  </section>
  <div class="container home-links">
    <br>
    <input type="button" value="Angular-1 App" class="btn btn-block btn-outline-secondary custom-width"
      onclick="window.location.href='test-module/angular-1/'">
    <br>
    <input type="button" value="JSP App" class="btn btn-block btn-outline-secondary custom-width"
      onclick="window.location.href='test-module/jsp/'">
  </div>
  <div id="footerContainer"></div>
  <script src="/kame-house/lib/js/jquery-2.0.3.min.js"></script>
  <script src="/kame-house/js/importHeaderFooter.js"></script>
</body>
</html>
