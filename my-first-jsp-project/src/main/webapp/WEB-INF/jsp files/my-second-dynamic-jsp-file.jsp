<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.LocalDate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Second JSP file</title>
</head>
<body>

	<h1>This is a dynamic resource which generates dynamic resource</h1>

	<%
	LocalDateTime ldt = LocalDateTime.now();
	String localdate = ldt.toString();
	%>

	<h3>The Local date and time is <%= localdate %></h3>

</body>
</html>