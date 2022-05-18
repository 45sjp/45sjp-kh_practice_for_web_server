<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
	<section id=enroll-container>
		<h2>비밀번호 변경</h2>
		<form 
			name="passwordUpdateFrm" 
			action="<%=request.getContextPath()%>/member/passwordUpdate" 
			method="post" >
			<table>
				<tr>
					<th>현재 비밀번호</th>
					<td><input type="password" name="oldPassword" id="oldPassword" required></td>
				</tr>
				<tr>
					<th>변경할 비밀번호</th>
					<td>
						<input type="password" name="newPassword" id="newPassword" required>
					</td>
				</tr>
				<tr>
					<th>비밀번호 확인</th>
					<td>	
						<input type="password" id="newPasswordCheck" required><br>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center;">
						<input type="submit"  value="변경" />
					</td>
				</tr>
			</table>
			<input type="hidden" name="memberId" value="<%= loginMember.getMemberId() %>" />
		</form>
	</section>
	<script>
		newPasswordCheck.onblur = () => {
			if(newPassword.value !== newPasswordCheck.value) {
				alert("두 비밀번호가 일치하지 않습니다.");
				return false; // 폼 유효성 검사에 사용
			}
			return true;
		};
		
		document.passwordUpdateFrm.onsubmit = () => {
			// password 영문자/숫자/특수문자[!@#$%^&*()] 4글자 이상
			if(!/^[A-Za-z0-9!@#$%^&*()]{4,}$/.test(oldPassword.value)) {
				alert("기존 비밀번호는 영문자/숫자/특수문자!@#$%^&*()로 4글자 이상이어야 합니다.");
				return false;
			}
			
			if(!/^[A-Za-z0-9!@#$%^&*()]{4,}$/.test(newPassword.value)) {
				alert("새 비밀번호는 영문자/숫자/특수문자!@#$%^&*()로 4글자 이상이어야 합니다.");
				return false;
			}
			
			if(!newPasswordCheck.onblur()) {
				return false; // 폼 제출을 방지
			}
		};
	</script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
