<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원탈퇴</title>
<style>
	div#delete-check-container{text-align:center; padding-top:50px;}
</style>
</head>
<body>
	<div id="delete-check-container">
		<p>회원탈퇴를 계속 진행하시겠습니까?</p>
		<div id="deleteYN">
			<input type="submit" value="확인" onclick="deleteYPopup();" />
			<button type="button" onclick="deleteNPopup();">취소</button>
		</div>
	</div>
	<form name="memberDeleteFrm" method="POST" action="<%= request.getContextPath() %>/member/memberDelete">
		<input type="hidden" name="deleteMember" />
	</form>
	<script>
		const deleteYPopup = () => {
			const frm = document.memberDeleteFrm;
			frm.submit();
		};
		
		const deleteNPopup = () => {
			self.close();
		};
	</script>
	
</body>
</html>
