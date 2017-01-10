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
        <jsp:include page="defaultHeader.jsp"></jsp:include>
        <title>Danh sách thành viên</title>
    </head>
    <body class="hold-transition skin-blue layout-top-nav">
        <div class="wrapper">
            <jsp:include page="navigator.jsp"></jsp:include>
            <!-- Table -->
             <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">Thành viên loại User</h3>
              <a class ="btn btn-success" href="addUser">Them User</a>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <table id="example2" class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Tên</th>
                    <th>Nghề nghiệp</th>
                    <th>Thời hạn giấy phép</th>
                    <th>Tài khoản</th>
                </tr>
                </thead>
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
            </div>
          </div>
        </div>
      </div>
       <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">Thành viên loại Service</h3>
              <a class ="btn btn-success" href="addService">Them Service</a>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <table id="example2" class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Tên dịch vụ</th>
                    <th>Tài khoản ngân hàng</th>
                </tr>
                </thead>
                  <c:forEach items="${allService}" var="service">
                <tr>      
                    <td>${service.getMember().getId()}</td>
                    <td>${service.getInfo("service_name")}</td>
                    <td>${service.getInfo("service_account")}</td>
                    
                </tr>
            </c:forEach>    
              </table>
                
            </div>
          </div>
        </div>
                </div>
            
            </div>
          </div>       
        </div>
        </div>
        </section>
    </div>    

    </body>
</html>
