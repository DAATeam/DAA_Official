<%-- 
    Document   : test
    Created on : Nov 16, 2016, 5:53:59 PM
    Author     : nguyenduyy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Test Protocol Locally</title>
    </head>
    <body>
        <h1>Hello Guys, This how We Work</h1>
        <h4> User info </h4>
        <p> user_name : ${user_name}</p>
        <p>user_job : ${user_job}</p>
        <p>appId : ${user_app_id}</p>
        <p>member id : ${user_member_id}</p>
               <p>esk : ${user_esk}</p>
       <p>epk : ${user_epk}</p>
       <p>user_name_cert : ${user_name_cert}</p>
       <p>user_job_cert : ${user_job_cert}</p>
       <h4> Service info </h4>
        <p> service_name : ${service_name}</p>
       <p>service_permission : ${service_permission}</p>
       <p>appId : ${service_app_id}</p>
       <p>member id : ${service_member_id}</p>
       <p>esk : ${service_esk}</p>
       <p>epk : ${service_epk}</p>
       <p>service_name_cert : ${service_name_cert}</p>
       <p>service_permission_cert : ${service_permission_cert}</p>
       <h4>User Verify service </h4>
        <p>result : ${verify_service}</p>
       <h4>Service verify user </h4>
        <p>result : ${verify_user}</p>
   </body>
</html>
