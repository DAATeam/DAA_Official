<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<spring:url value="/resources/bootstrap/css/bootstrap.min.css" var="mainCss"></spring:url>
     <spring:url value = "/resources/bootstrap/js/bootstrap.min.js" var="mainJs"></spring:url>
     <spring:url value = "/resources/dist/css/AdminLTE.min.css" var="adminCss"></spring:url>
     <spring:url value = "/resources/dist/css/skins/_all-skins.min.css" var="skinCss"></spring:url>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.6 -->
  <link rel="stylesheet" href=${mainCss}>
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href=${adminCss}>
  <!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
  <link rel="stylesheet" href=${skinCss}>

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
  
  <spring:url value="/resources/plugins/fastclick/fastclick.js" var = "fastClick"></spring:url>
<!-- FastClick -->
<script src=${fastClick}></script>
<!-- AdminLTE App -->
<spring:url value="/resources/dist/js/app.min.js" var = "LTEapp"></spring:url>
<script src=${LTEapp}></script>
<!-- AdminLTE for demo purposes -->
<spring:url value= "/resources/dist/js/demo.js" var = "demo"></spring:url>
<script src=${demo}></script>