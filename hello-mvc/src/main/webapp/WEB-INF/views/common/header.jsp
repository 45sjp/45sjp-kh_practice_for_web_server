<%@ page import="member.model.dto.MemberRole"%>
<%@ page import="member.model.dto.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	Member loginMember = (Member) session.getAttribute("loginMember");
	// System.out.println("loginMember@header.jsp = " + loginMember);
	
	String msg = (String) session.getAttribute("msg");
	if(msg != null) {
		session.removeAttribute("msg");
	}
	
	String saveId = null; // 아이디 저장을 체크한 경우, memberId값이 담길 변수
	Cookie[] cookies = request.getCookies();
	if(cookies != null) {
		for(Cookie cookie : cookies) {
			// System.out.println("Cookie{" + cookie.getName() + "=" + cookie.getValue() + "}");
			
			if("saveId".equals(cookie.getName())) {
				saveId = cookie.getValue();
			}
		}
	}
	else {
		// System.out.println("> 이번 요청에 전달된 cookie가 없습니다.");
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hello MVC</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/style.css" />
<script>
	window.onload = () => {
	 <% if(msg != null) { %>
			alert("<%= msg %>");
	 <% } %>
		// html문서는 위에서부터 아래로 읽음. 아직 loginFrm이 등록되어 있지 않은 상태이기 때문에 이벤트 발생 불가
		// document.loginFrm.onsubmit = () => {};
	 <% if(loginMember == null) { %>
			document.loginFrm.onsubmit = (e) => {
				const memberIdVal = memberId.value;
				const passwordVal = password.value;
				
				if(!/^.{4,}$/.test(memberIdVal)) {
					alert("유효한 아이디를 입력해주세요.");
					memberId.select();
					return false;
				}
				if(!/^.{4,}$/.test(passwordVal)) {
					alert("유효한 비밀번호를 입력해주세요.");
					password.select();
					return false;
				}
			};
	 <% } %>
	};
</script>
</head>
<body>
	<div id="container">
		<header>
			<h1>Hello MVC</h1>
			<div class="login-container">
			<% if(loginMember == null){ %>
				<%-- 로그인 전 --%>
				<!-- 로그인폼 시작 -->
				<form id="loginFrm" name="loginFrm" method="POST" action="<%= request.getContextPath() %>/member/login">
					<table>
						<tr>
							<!-- tableindex : tab시 focus 순서 지정 -->
							<td><input type="text" name="memberId" id="memberId" placeholder="아이디" tabindex="1" value="<%= saveId != null ? saveId : "" %>"></td>
							<td><input type="submit" value="로그인" tabindex="3"></td>
						</tr>
						<tr>
							<td><input type="password" name="password" id="password" placeholder="비밀번호" tabindex="2"></td>
							<td></td>
						</tr>
						<tr>
							<td colspan="2">
								<input type="checkbox" name="saveId" id="saveId" <%= saveId != null ? "checked" : "" %> />
								<label for="saveId">아이디저장</label>&nbsp;&nbsp;
								<input type="button" value="회원가입" onclick="location.href='<%= request.getContextPath() %>/member/memberEnroll';">
							</td>
						</tr>
					</table>
				</form>
				<!-- 로그인폼 끝-->
			<% } else { %>
				<%-- 로그인 성공시 --%>
				<table id="login">
					<tbody>
						<tr>
							<td><%= loginMember.getMemberName() %>님, 안녕하세요.</td>
						</tr>
						<tr>
							<td>
								<input type="button" value="내정보보기" onclick="location.href='<%= request.getContextPath() %>/member/memberView';" />
								<input type="button" value="로그아웃" onclick="location.href='<%= request.getContextPath() %>/member/logout';"/>							
							</td>
						</tr>
					</tbody>
				</table>
			<% } %>
			</div>
			<!-- 메인메뉴 시작 -->
			<nav>
				<ul class="main-nav">
					<li class="home"><a href="<%= request.getContextPath() %>">Home</a></li>
					<li class="notice"><a href="#">공지사항</a></li>
					<li class="board"><a href="#">게시판</a></li>
				<% if(loginMember != null && loginMember.getMemberRole() == MemberRole.A) { %>
					<li class="admin"><a href="<%= request.getContextPath() %>/admin/memberList">회원관리</a></li>
				<% } %>
				</ul>
			</nav>
			<!-- 메인메뉴 끝-->					
		</header>
			
		<section id="content">