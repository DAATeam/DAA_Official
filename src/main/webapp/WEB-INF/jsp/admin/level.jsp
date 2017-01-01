<%-- 
    Document   : service
    Created on : Dec 31, 2016, 10:18:19 PM
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
        <spring:url value = "/resources/js/myjs.js" var="myJs"></spring:url>
        <link href="${mainCss}" rel="stylesheet" />
     <script type="text/JavaScript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js" ></script>
     
        <script src="${mainJs}"></script> 
        <script src="${myJs}"></script> 
        
        <title>Danh sách phân quyền</title>
    </head>
    <body>
    <p class="h2">Danh sách các loại thành viên</p>
        <button type="button" class="btn btn-success btn-lg" data-toggle="modal" data-target="#addTypeModal" >Thêm mới</button>
                <!-- Modal -->
<div id="addTypeModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Modal Header</h4>
      </div>
      <div class="modal-body">
          <div class="form-group">
        <label class="col-md-4 control-label" for="M">Prefix</label>  
        <div class="col-md-4">
        <input id="prefix" name="prefix" type="text" placeholder="" class="form-control input-md" required="">
    
  </div>
</div>

      </div>
      <div class="modal-footer">
          <button type="button" onclick="addNewType()" class="btn btn-success" data-dismiss="modal">Save</button>  
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

    </div>
    </div>
    
        <table class="table-bordered">
            <thead>ID</thead>
            <thead>Prefix</thead>
            <c:forEach items="${allType}" var="t">
                <tr> 
                    <td>${t.getId()}</td>
                    <td>${t.getPrefix()}</td>
                </tr>
            </c:forEach>
        </table>
        <br>
        
        <table>
            <thead>Phân quyền</thead>
            <thead>Bên gửi</thead>
            <thead>Bên nhận</thead>
            <thead>Các nội dung</thead>
            <c:forEach items="${allLevel}" var="level">
                <tr>      
                    <td>${level.getLevel_name()}</td>
                    <td>${level.getMsender().getPrefix()}</td>
                    <td>${level.getMreceiver().getPrefix()}</td>
                    <td>${level.getLevel_permission()}</td>
                    
                    
                </tr>
            </c:forEach>    
             
        </table>
    </body>
</html>
