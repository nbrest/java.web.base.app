<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*,java.text.*,java.io.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>KameHouse 405 Method Not Allowed</title>
<link rel="icon" type="img/ico" href="${pageContext.request.contextPath}/img/favicon.ico" />
<script src="/kame-house/lib/js/jquery-2.0.3.min.js"></script>
<script src="/kame-house/js/global.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/css/bootstrap.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header-footer/header.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header-footer/footer.css" />
</head>
<body>
    <%
      response.setStatus(405);
    %>
  <div id="headerContainer"></div> 
  <div class="default-layout main-body"> 
    <h2 class="txt-c-d-kh">KameHouse 405 Method Not Allowed</h2> 
    <%
      if (exception != null) {
    %>
    Message:
    <%=exception.getMessage()%>

    StackTrace:
    <%
      StringWriter stringWriter = new StringWriter();
    				PrintWriter printWriter = new PrintWriter(stringWriter);
    				exception.printStackTrace(printWriter);
    				out.println(stringWriter);
    				printWriter.close();
    				stringWriter.close();
    			}
    %>
  </div> 
  <div id="footerContainer"></div>
</body>
</html>