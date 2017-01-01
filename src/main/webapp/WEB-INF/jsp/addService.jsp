<%-- 
    Document   : addService
    Created on : Dec 31, 2016, 5:19:34 PM
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
        <title>Đăng kí thông tin </title>
    </head>
    <body>
        <h1></h1>
        <form class="form-horizontal" action ="" method = "POST">
<fieldset>

<!-- Form Name -->
<legend>Bảng đăng kí</legend>

<!-- Text input-->
<div class="form-group">
  <label class="col-md-4 control-label" for="service_name">Tên</label>  
  <div class="col-md-4">
  <input id="fn" name="service_name" type="text" placeholder="" class="form-control input-md" required="">
    
  </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="service_account">Tài khoản ngân hàng</label>  
  <div class="col-md-4">
  <input id="cmpny" name="service_account" type="text" placeholder="" class="form-control input-md" required="">
    
  </div>
</div>


</div>
<!-- Text input-->
<div class="form-group">
  <label class="col-md-4 control-label" for="M">Mật khẩu</label>  
  <div class="col-md-4">
  <input id="cmpny" name="M" type="password" placeholder="" class="form-control input-md" required="">
    
  </div>
</div>

<!--

<div class="form-group">
  <label class="col-md-4 control-label" for="add2">Address 2</label>  
  <div class="col-md-4">
  <input id="add2" name="add2" type="text" placeholder="" class="form-control input-md">
    
  </div>
</div>


<div class="form-group">
  <label class="col-md-4 control-label" for="city">City</label>  
  <div class="col-md-4">
  <input id="city" name="city" type="text" placeholder="city" class="form-control input-md" required="">
    
  </div>
</div>


<div class="form-group">
  <label class="col-md-4 control-label" for="zip">Zip Code</label>  
  <div class="col-md-4">
  <input id="zip" name="zip" type="text" placeholder="Zip Code" class="form-control input-md" required="">
    
  </div>
</div>


<div class="form-group">
  <label class="col-md-4 control-label" for="ctry">Country</label>  
  <div class="col-md-4">
  <input id="ctry" name="ctry" type="text" placeholder="Country" class="form-control input-md" required="">
    
  </div>
</div>


<div class="form-group">
  <label class="col-md-4 control-label" for="phone">Text InputPhone</label>  
  <div class="col-md-4">
  <input id="phone" name="phone" type="text" placeholder="Phone#" class="form-control input-md" required="">
    
  </div>
</div>


<div class="form-group">
  <label class="col-md-4 control-label" for="Training">Would you like to attend our Networking Reception on September 3, 2015?</label>
  <div class="col-md-4"> 
    <label class="radio-inline" for="Training-0">
      <input type="radio" name="Training" id="Training-0" value="yes" checked="checked">
      Yes
    </label> 
    <label class="radio-inline" for="Training-1">
      <input type="radio" name="Training" id="Training-1" value="no">
      No
    </label>
  </div>
</div>


<div class="form-group">
  <label class="col-md-4 control-label" for="Networking_Reception">Would you like to attend our Technical Product Update Session on September 4, 2015?</label>
  <div class="col-md-4"> 
    <label class="radio-inline" for="Networking_Reception-0">
      <input type="radio" name="Networking_Reception" id="Networking_Reception-0" value="meet_yes" checked="checked">
      Yes
    </label> 
    <label class="radio-inline" for="Networking_Reception-1">
      <input type="radio" name="Networking_Reception" id="Networking_Reception-1" value="meet_no">
      No
    </label>
  </div>
</div>


<div class="form-group">
  <label class="col-md-4 control-label" for="selectbasic">Select Basic</label>
  <div class="col-md-4">
    <select id="selectbasic" name="selectbasic" class="form-control input-md">
      <option>Option one</option>
      <option>Option two</option>
    </select>
  </div>
</div>



<div class="form-group">
  <label class="col-md-4 control-label" for="Dinner">Would you like to attend our Networking Dinner on September 4, 2015?</label>
  <div class="col-md-4"> 
    <label class="radio-inline" for="Dinner-0">
      <input type="radio" name="Dinner" id="Dinner-0" value="dinner_yes" checked="checked">
      Yes
    </label> 
    <label class="radio-inline" for="Dinner-1">
      <input type="radio" name="Dinner" id="Dinner-1" value="dinner_no">
      No
    </label>
  </div>
</div>
-->
<!-- Button -->
<div class="form-group">
  <label class="col-md-4 control-label" for="submit"></label>
  <div class="col-md-4">
    <button id="submit" name="submit" class="btn btn-primary">SUBMIT</button>
  </div>
</div>

</fieldset>
</form>
    </body>
</html>
