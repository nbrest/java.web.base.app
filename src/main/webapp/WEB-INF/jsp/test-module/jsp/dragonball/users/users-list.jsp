<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width">
<meta name="author" content="nbrest">
<meta name="mobile-web-app-capable" content="yes">

<title>DragonBallUsers List</title>
<link rel="icon" type="img/ico" href="${pageContext.request.contextPath}/img/favicon.ico" />
<script src="/kame-house/lib/js/jquery-2.0.3.min.js"></script>
<script src="/kame-house/js/global.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/css/bootstrap.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/test-module/jsp/app.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css" />
<link rel="stylesheet" href="/kame-house/css/test-module/tm-global.css" />
</head>
<body> 
  <div class="main-body">
  <div class="default-layout">
    <h3 class="h3-kh txt-l-d-kh txt-l-m-kh">List of DragonBall Users</h3>
    <c:set var="dragonBallUsers" scope="page"
      value="${dragonBallUserService.readAll()}" />
    <%
      Enumeration<String> paramNames = request.getParameterNames();

    			while (paramNames.hasMoreElements()) {
    				String paramName = (String) paramNames.nextElement();
    				System.out.print(paramName + " : ");
    				String paramValue = request.getParameter(paramName);
    				System.out.println(paramValue);
    			}
    %>
      <table class="table table-dragonball-users">
        <caption class="hidden-kh">DragonBall Users</caption>
        <thead>
          <tr>
            <th scope="row">Id</th>
            <th scope="row">Name</th>
            <th scope="row">Email</th>
            <th scope="row">Age</th>
            <th scope="row">Power Level</th>
            <th scope="row">Stamina</th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${dragonBallUsers}" var="dragonBallUser">
            <tr>
              <td>${dragonBallUser.getId()}</td>
              <td>${dragonBallUser.getUsername()}</td>
              <td>${dragonBallUser.getEmail()}</td>
              <td>${dragonBallUser.getAge()}</td>
              <td>${dragonBallUser.getPowerLevel()}</td>
              <td>${dragonBallUser.getStamina()}</td>
              <td><input type="button" value="edit"
                  class="btn btn-outline-success btn-borderless"
                  onclick="window.location.href='users-edit?username=${dragonBallUser.getUsername()}'">
                <form action="users-delete-action" method="post">
                  <input type="hidden" name="id" value="${dragonBallUser.getId()}" />
                  <input type="submit" value="delete" class="btn btn-outline-danger btn-borderless" />
                </form></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    <input type="button" value="Add DragonBall User" class="btn btn-outline-info"
      onclick="window.location.href='users-add'">
  </div>
  </div>
</body>
</html>
