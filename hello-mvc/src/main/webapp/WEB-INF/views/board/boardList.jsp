<%@ page import="java.util.List" %>
<%@ page import="board.model.dto.BoardExt"%>
<%@ page import="board.model.dto.Attachment"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%
	List<BoardExt> boardList = (List<BoardExt>) request.getAttribute("boardList");
	String pagebar = (String) request.getAttribute("pagebar");

	// Board, Attachment join 처리!
	// List<Attachment> attachmentList = (List<Attachment>) request.getAttribute("attachmentList");
%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/board.css" />
<section id="board-container">
	<h2>게시판 </h2>
<% if(loginMember != null) { %> <%-- header.jsp의 loginMember를 가져옴 --%>
	<input type="button" value="글쓰기" id="btn-add" onclick="location.href='<%= request.getContextPath() %>/board/boardEnroll';" />
<% } %>
	<table id="tbl-board">
		<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>작성자</th>
				<th>작성일</th>
				<th>첨부파일</th> <%-- 첨부파일이 있는 경우 /images/file.png 표시 width:16px --%>
				<th>조회수</th>
			</tr>
		</thead>
		<tbody>
			<%
				if(boardList != null && !boardList.isEmpty()) {
					for(BoardExt board : boardList) {
			%>
					<tr>
						<td><%= board.getNo() %></td>
						<td>
							<a href="<%= request.getContextPath() %>/board/boardView?no=<%= board.getNo() %>"><%= board.getTitle() %></a>
						</td>
						<td><%= board.getMemberId() %></td>
						<td><%= board.getRegDate() %></td>
						<%-- 첨부파일 --%>
					<% if(board.getAttachCount() > 0) { %>
						<td>
							<img src="<%= request.getContextPath() %>/images/file.png" alt="첨부파일 표시 아이콘" />
						</td>
					<% } else { %>
						<td>
							<%-- 첨부파일이 없다면 비어있는 셀 처리 --%>
						</td>
					<% } %>
						<td><%= board.getReadCount() %></td>
					</tr>
			<%		
				 	}
				}
				else {
			%>
					<tr>
						<td colspan="10">조회된 게시글이 없습니다.</td>
					</tr>
			<% 
				}
			%>
		</tbody>
	</table>

	<div id='pageBar'>
		<%= pagebar %>
	</div>
</section>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
