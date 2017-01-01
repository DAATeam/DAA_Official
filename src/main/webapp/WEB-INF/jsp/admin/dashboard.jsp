<%-- 
    Document   : dashboard
    Created on : Dec 31, 2016, 9:44:00 PM
    Author     : nguyenduyy
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <spring:url value="/resources/css/bootstrap.min.css" var="mainCss"></spring:url>
        <spring:url value = "/resources/js/bootstrap.min.js" var="mainJs"></spring:url>
        <link href="${mainCss}" rel="stylesheet" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script src="${mainJs}"></script> 
        <title>Admin Page</title>
    </head>
    <body>
       <nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Issuer</a>
    </div>
    <ul class="nav navbar-nav">
      <li class="active"><a href="#">Home</a></li>
      <li><a href="./member">Member</a></li>
      <li><a href="./level">Level</a></li>
      <li><a href="./addApp">AddApp</a></li>
    </ul>
  </div>
    </nav>
        
    </body>
</html>
