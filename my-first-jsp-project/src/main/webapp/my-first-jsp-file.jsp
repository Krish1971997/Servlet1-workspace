<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dynamic resource</title>
</head>
<body>
	<h1>This is a dynamic resource which generates dynamic resource</h1>
	<%
		Date d=new Date();
		String currentdate=d.toString();
	%>
	
	<h2>Current Date and time is <%= currentdate  %></h2>
	
</body>
</html>