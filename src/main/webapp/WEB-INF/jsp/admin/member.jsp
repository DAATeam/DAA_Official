<%-- 
    Document   : user
    Created on : Dec 31, 2016, 10:18:11 PM
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
        <title>Danh sách thành viên</title>
    </head>
    <body>
        <h2>Loại User</h2>
        <a href = "addUser" class="btn btn-primary">Thêm User mới</a>
        <br>
        <table class="table-bordered">
            <thead class="thead-default">MemberId</thead>
            <thead class="thead-default">Tên</thead>
            <thead class="thead-default">Nghề nghiệp</thead>
            <thead class="thead-default">Thời hạn giấy phép</thead>
            <thead class="thead-default">Tài khoản ngân hàng</thead>
            <c:forEach items="${allUser}" var="user">
                <tr>      
                    <td>${user.getMember().getId()}</td>
                    <td>${user.getInfo("user_name")}</td>
                    <td>${user.getInfo("user_job")}</td>
                    <td>${user.getInfo("user_drive_expire")}</td>
                    <td>${user.getInfo("user_account")}</td>
                    
                </tr>
            </c:forEach>    
        </table>
        <h2>Loại Service</h2>
        <br>
        <a href="addService" class="btn btn-primary" >Thêm Service mới</a>
        <table class="table-bordered">
            <thead class="thead-default">MemberId</thead>
            <thead class="thead-default">Tên dịch vụ</thead>
            <thead class="thead-default">Tài khoản ngân hàng</thead>
            <c:forEach items="${allService}" var="service">
                <tr>      
                    <td>${service.getMember().getId()}</td>
                    <td>${service.getInfo("service_name")}</td>
                    <td>${service.getInfo("service_account")}</td>
                    
                </tr>
            </c:forEach>    
        </table>
        
            
    </body>
</html>
