<%-- 
    Document   : addUser
    Created on : Dec 31, 2016, 5:19:34 PM
    Author     : nguyenduyy
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="admin/defaultHeader.jsp"></jsp:include>
        <title>Đăng kí thông tin </title>
    </head>
        <body>
           
            <div class="wrapper">
            <section class ="content">
                <h1></h1>
        <form class="form-horizontal" action ="" method = "POST">
<fieldset>

<!-- Form Name -->
<legend>Bảng đăng kí</legend>

<!-- Text input-->
<div class="form-group">
  <label class="col-md-4 control-label" for="fn">Tên</label>  
  <div class="col-md-4">
  <input id="fn" name="user_name" type="text" placeholder="" class="form-control input-md" required="">
    
  </div>
</div>

<!-- Text input-->
<div class="form-group">
  <label class="col-md-4 control-label" for="user_job">Nghề nghiệp</label>  
  <div class="col-md-4">
  <input id="cmpny" name="user_job" type="text" placeholder="" class="form-control input-md" required="">
    
  </div>
</div>

<!-- Text input-->
<div class="form-group">
  <label class="col-md-4 control-label" for="user_drive_expire">Thời hạn giấy phép</label>  
  <div class="col-md-4">
  <input id="email" name="user_drive_expire" type="text" placeholder="" class="form-control input-md" required="">
    
  </div>
</div>

<!-- Text input-->
<div class="form-group">
  <label class="col-md-4 control-label" for="add1">Tài khoản ngân hàng</label>  
  <div class="col-md-4">
  <input id="add1" name="user_account" type="text" placeholder="" class="form-control input-md" required="">
    
  </div>
</div>
<!-- Text input-->
<div class="form-group">
  <label class="col-md-4 control-label" for="M">Mật khẩu</label>  
  <div class="col-md-4">
  <input id="cmpny" name="M" type="password" placeholder="" class="form-control input-md" required="">
    
  </div>
</div>


<!-- Button -->
<div class="form-group">
  <label class="col-md-4 control-label" for="submit"></label>
  <div class="col-md-4">
    <button id="submit" name="submit" class="btn btn-primary">SUBMIT</button>
  </div>
</div>

</fieldset>
</form>
            </section>
            </div>

    </body>
</html>
