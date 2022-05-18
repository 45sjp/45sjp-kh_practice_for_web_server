<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %>
<%
	// isErrorPage="true" 속성 추가시 발생한 예외객체에 선언 없이 접근 가능
	String msg = exception.getMessage();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오류</title>
<style>
	body {
		text-align: center;
	}
	h1 {
		margin: 10px 0;		
		font-size: 65vw;
	}
	p {
		color: red;
	}
</style>
</head>
<body>
	<h1>헉</h1>
	<p><%= msg %></p>
	<p><a href="<%= request.getContextPath() %>/">홈으로</a></p>
</body>
</html>