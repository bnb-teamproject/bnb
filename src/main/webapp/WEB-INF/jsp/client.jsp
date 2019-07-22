<%-- 
    Document   : client
    Created on : Jul 22, 2019, 2:18:35 PM
    Author     : Haris
--%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Client Page</title>
    </head>
    <body>
        <h1>Hello ${user.username}!</h1><br>

        <h1>Start your Reservation!</h1><br>

        <h3>Choose City</h3>
        <a href="${pageContext.request.contextPath}/client/1">Athens</a>
        <a href="${pageContext.request.contextPath}/client/3">Thessaloniki</a>
    </body>
</html>
