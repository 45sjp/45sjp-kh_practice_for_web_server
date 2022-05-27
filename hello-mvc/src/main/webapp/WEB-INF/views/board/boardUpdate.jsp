<%@ page import="board.model.dto.Attachment"%>
<%@ page import="java.util.List"%>
<%@ page import="board.model.dto.BoardExt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>    
<%
	BoardExt board = (BoardExt) request.getAttribute("board");
%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/board.css" />

<section id="board-container">
<h2>게시판 수정</h2>
<form 
	name="boardUpdateFrm" 
	action="<%=request.getContextPath() %>/board/boardUpdate" 
	method="post" 
	enctype="multipart/form-data">
	<table id="tbl-board-view">
	<tr>
		<th>제 목</th>
		<td><input type="text" name="title" value="<%= board.getTitle() %>" required></td>
	</tr>
	<tr>
		<th>작성자</th>
		<td>
			<input type="text" name="memberId" value="<%= board.getMemberId() %>" readonly/>
		</td>
	</tr>
	
	<tr>
		<th>첨부파일</th>
		<td>
	<% 
		List<Attachment> attachments = board.getAttachments();
		if(attachments != null && !attachments.isEmpty()) {
			for(int i = 0; i < attachments.size(); i++) {
				// foreach인 경우, 비어 있으면 실행 안 됨. NullPointerException 유발 가능
				Attachment attach = attachments.get(i);
	%>
			<%-- 
				input:file은 보안상의 이유로 value속성 등을 임의로 제어할 수 없음
			--%>
			<img src="<%= request.getContextPath() %>/images/file.png" width="16px">
			<%= attach.getOriginalFilename() %>
			<input type="checkbox" name="delFile" id="deFile<%= i + 1 %>" value="<%= attach.getNo() %>" />
			<label for="delFile<%= i + 1 %>">삭제</label>
			<br />
	<% 
			}
		} 
	%>			
			<input type="file" name="upFile1" value="">
			<input type="file" name="upFile2" value="">
		</td>
	</tr>
	<tr>
		<th>내 용</th>
		<%-- textarea태그 사용시 유의사항 : 시작과 끝태그를 한 줄로 작성! --%>
		<td>
			<textarea rows="5" cols="40" name="content"><%= board.getContent() %></textarea>
		</td>
	</tr>
	<tr>
		<th colspan="2">
			<input type="submit" value="수정하기"/>
			<input type="hidden" name="no" value="<%= board.getNo() %>" />
			<input type="button" value="취소" onclick="history.go(-1);"/>
		</th>
	</tr>
</table>
</form>
</section>
<script>
	/**
	 * 업로드 가능한 첨부파일 수 제한하기
	 * 	=> 2개 초과일 경우 첨부파일 체크박스 감춤 처리
	 */
	const len = document.querySelectorAll("[name=delFile]").length;
	for(let i = 0; i < len; i++)
		document.querySelectorAll("input[type=file]")[i].style.display = "none";

	/**
	 * [name=delFile] 체크/체크해제시 input[type=file] 노출/감춤 처리
	 */
	document.querySelectorAll("[name=delFile]").forEach((delFile) => {
		delFile.onchange = (e) => {
			const {id, checked} = e.target;
			// console.log(id, checked);
			const n = id.replaceAll(/[^0-9]/g, "");
			const file = document.querySelector(`[name=upFile\${n}]`);
			document.querySelector(`[name=upFile\${n}]`).style.display = checked ? "inline" : "none";
			
			// 삭제 체크박스 선택 후 첨부 파일 첨부 -> 다시 삭제 취소하고자 할 때 지정한 파일(첨부된 파일) 제거
			checked || (file.value = ""); // 지정한 파일을 제거
		};
	});
	
	document.boardUpdateFrm.onsubmit = (e) => {
	    const frm = e.target;
	    // 제목을 작성하지 않은 경우 폼 제출할 수 없음
	    const titleVal = frm.title.value.trim(); // 좌우공백
	    if(!/^.+$/.test(titleVal)){
	        alert("제목을 작성해주세요.");
	        frm.title.select();
	        return false;
	    }        
	                       
	    // 내용을 작성하지 않은 경우 폼 제출할 수 없음
	    const contentVal = frm.content.value.trim();
	    if(!/^(.|\n)+$/.test(contentVal)){
	        alert("내용을 작성해주세요.");
	        frm.content.select();
	        return false;
	    }
	}
	<%--
		$(document.boardUpdateFrm).submit(function (){
			var $content = $("[name=content]");
			if(/^(.|\n)+$/.test($content.val()) == false){
				alert("내용을 입력하세요");
				return false;
			}
			return true;
		});
	--%>
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
