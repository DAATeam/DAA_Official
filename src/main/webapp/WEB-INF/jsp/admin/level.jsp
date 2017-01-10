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
        <jsp:include page="defaultHeader.jsp"></jsp:include>
        <spring:url value = "/resources/js/myjs.js" var="myJs"></spring:url>
       
        <script src="${myJs}"></script> 
        
        <title>Danh sách phân quyền</title>
    </head>
    <body class="hold-transition skin-blue layout-top-nav">
        
        <div class="wrapper">
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
                <!-- end ò modal -->
            <jsp:include page="navigator.jsp"></jsp:include>
        
            <!-- Table -->
          <section class ="content">
            <div class="row">
         
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">Các lọai thành viên</h3>
                 <button type="button" class="btn btn-success btn-lg" data-toggle="modal" data-target="#addTypeModal" >Thêm mới</button>
              
            </div>
          
            <!-- /.box-header -->
            <div class="box-body">
              <table id="example2" class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Loại</th>
                    
                </tr>
                </thead>
                 <c:forEach items="${allType}" var="t">
                <tr> 
                    <td>${t.getId()}</td>
                    <td>${t.getPrefix()}</td>
                </tr>
            </c:forEach>
            </table>
            </div>
          </div>
        </div>
      </div>
              
             <div class="row">
         
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">Danh sách phân quyền</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <table id="example2" class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>Tên</th>
                    <th>Bên gửi</th>
                    <th>Bên nhận</th>
                    <th>Các thông tin được đọc</th>
                    
                </tr>
                </thead>
                <c:forEach items="${allLevel}" var="level">
                <tr>      
                    <td>${level.getLevel_name()}</td>
                    <td>${level.getMsender().getPrefix()}</td>
                    <td>${level.getMreceiver().getPrefix()}</td>
                    <td>${level.getLevel_permission()}</td>
                    
                    
                </tr>
            </c:forEach>    
            </table>
            </div>
          </div>
        </div>
      </div>     

</section> 
</div>
</body>
</html>
            
    