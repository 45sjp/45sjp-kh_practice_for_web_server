<?xml version="1.0" encoding="UTF-8" ?>
<%--contexntType="text/xml"로 수정 --%>
<%@page import="com.kh.ajax.celeb.dto.Celeb"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/xml; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	List<Celeb> celebList = (List<Celeb>) request.getAttribute("celebList");
%>
<celebs len="<%= celebList.size() %>">
	<% for(Celeb celeb : celebList) { %>
	<celeb no="<%= celeb.getNo() %>">
		<name><%= celeb.getName() %></name>
		<type><%= celeb.getType() %></type>
		<profile><%= celeb.getProfile() %></profile>
	</celeb>
	<% } %>
</celebs>