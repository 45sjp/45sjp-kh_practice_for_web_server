<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ajax</title>
</head>
<body>
	<h1>Ajax</h1>
	
	<h2>XMLHttpRequest</h2>
	<ul>
		<li><a href="<%= request.getContextPath() %>/vanilla-js/xmlHttpRequest.jsp">순수 js로 XMLHttpRequest 제어하기</a></li>
	</ul>
	
	<h2>jQuery.ajax</h2>
	<ul>
		<li><a href="<%= request.getContextPath() %>/jquery/text.jsp">text</a></li>
		<li><a href="<%= request.getContextPath() %>/jquery/html.jsp">html</a></li>
		<li><a href="<%= request.getContextPath() %>/jquery/autoComplete.jsp">autoComplete</a></li>
		<li><a href="<%= request.getContextPath() %>/jquery/xml.jsp">xml</a></li>
	</ul>
</body>
</html>