<%@ page language = "java" contentType = "text/html; charset=UTF-8" pageEncoding = "UTF-8" isErrorPage = "true" %>

<html>
<head>
    <meta http-equiv = "Content-Type" content = "text/html; charset=UTF-8">
    <title>error</title>
    <style>
        <%@include file="errors-css/error-style.css"%>
    </style>
</head>
<body class="body">
<pre class="main"><%
    String error = request.getParameter("error");
    if (error.equals("400")) {
    %>400
    Bad Request<%
        } else {
            if(error.equals("404")){
    %>404
    Bad Request<%
            }
            if(error.equals("500")) {
    %>500
    Internal
    Server
    Error<%
            }
    %>Some
    Error<%
        }%></pre>

</body>
</html>