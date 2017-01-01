<%-- 
    Document   : registerSuccessful
    Created on : Dec 31, 2016, 6:58:23 PM
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
    
        <script src="${mainJs}"></script> 
        <title>Đăng kí thành công </title>
    </head>
    <body>
        <p> ${msg} </p>
        <div class =" container">
        <p class=""> Số ID : </p> <p>${memberId} </p>
        <a href="addUser">Thêm mới</a>
        <a href="member">Danh sách</a>
            
        </div>
        
    </body>
</html>
