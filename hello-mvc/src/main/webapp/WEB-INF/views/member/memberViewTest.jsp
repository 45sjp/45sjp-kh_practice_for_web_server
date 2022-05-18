<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.List"%>
<%@ page import="java.sql.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%
	String memberId = loginMember.getMemberId();
	String password = loginMember.getPassword();
	String memberName = loginMember.getMemberName();
	Date birthday = loginMember.getBirthday(); // null값이어도 input:datetime에서 무시함
	String email = loginMember.getEmail() != null ? loginMember.getEmail() : "";
	String phone = loginMember.getPhone();
	String address = loginMember.getAddress() != null ? loginMember.getAddress() : "";
	String gender = loginMember.getGender() != null ? loginMember.getGender() : "";
	String hobby = loginMember.getHobby(); // 등산,게임
	
	List<String> hobbyList = null;

	if(hobby != null) {
		String[] arr = hobby.split(",");
		hobbyList = Arrays.asList(arr); // String[] -> List<String>
	}
%>
<!-- 
<script>
	window.onload = () => {
		<%-- gender --%>
		const gender = "<%= loginMember.getGender() %>";
		
		if(gender === "M") {
			document.querySelector("#gender0").checked = "true";
		}
		else if(gender === "F") {
			document.querySelecotr("#gender1").checked = "true";
		}
		
		<%-- hobby --%>
		const hobbyArr = "<%= loginMember.getHobby() %>".split(",");
		const checkbox = document.getElementsByName("hobby");
		
		for(let hobby of hobbyArr) {
            for(let ckb of checkbox) {
            	if(hobby === ckb.value) {
            		ckb.checked = "true";
            	}
            }
        }
	};
</script>
-->
<section id=enroll-container>
	<h2>회원 정보</h2>
	<form name="memberUpdateFrm" method="POST" action="<%= request.getContextPath() %>/member/memberUpdate">
		<table>
			<tr>
				<th>아이디<sup>*</sup></th>
				<td>
					<input type="text" name="memberId" id="memberId" value="<%= memberId %>" readonly>
				</td>
			</tr>
			<tr>
				<th>패스워드<sup>*</sup></th>
				<td>
					<input type="password" name="password" id="password" value="<%= password %>" required>
				</td>
			</tr>
			<tr>
				<th>패스워드확인<sup>*</sup></th>
				<td>	
					<input type="password" id="passwordCheck" value="<%= password %>" required><br>
				</td>
			</tr> <%-- 블럭잡고 주석처리 : Shift + Ctrl + / --%>
			<tr>
				<th>이름<sup>*</sup></th>
				<td>	
				<input type="text"  name="memberName" id="memberName" value="<%= memberName %>" required><br>
				</td>
			</tr>
			<tr>
				<th>생년월일</th>
				<td>
				<%-- 
					null값이어도 input:datetime에서 무시함!
					value값이 sql문
				--%>
				<input type="date" name="birthday" id="birthday" value="<%= birthday %>"><br>
				</td>
			</tr> 
			<tr>
				<th>이메일</th>
				<td>	
					<input type="email" placeholder="abc@xyz.com" name="email" id="email" value="<%= email %>"><br>
				</td>
			</tr>
			<tr>
				<th>휴대폰<sup>*</sup></th>
				<td>	
					<input type="tel" placeholder="(-없이)01012345678" name="phone" id="phone" maxlength="11" value="<%= phone %>" required><br>
				</td>
			</tr>
			<tr>
				<th>주소</th>
				<td>	
					<input type="text" placeholder="" name="address" id="address" value="<%= address %>"><br>
				</td>
			</tr>
			<tr>
				<th>성별</th>
				<td>
			       	<input type="radio" name="gender" id="gender0" value="M" <%= "M".equals(gender) ? "checked" : "" %>>
					<label for="gender0">남</label>
					<input type="radio" name="gender" id="gender1" value="F" <%= "F".equals(gender) ? "checked" : "" %>>
					<label for="gender1">여</label>
				</td>
			</tr>
			<tr>
				<th>취미</th>
				<td>
					<input type="checkbox" name="hobby" id="hobby0" value="운동" <%= hobbyList != null && hobbyList.contains("운동") ? "checked" : "" %>><label for="hobby0">운동</label>
					<input type="checkbox" name="hobby" id="hobby1" value="등산" <%= hobbyList != null && hobbyList.contains("등산") ? "checked" : "" %>><label for="hobby1">등산</label>
					<input type="checkbox" name="hobby" id="hobby2" value="독서" <%= hobbyList != null && hobbyList.contains("독서") ? "checked" : "" %>><label for="hobby2">독서</label><br />
					<input type="checkbox" name="hobby" id="hobby3" value="게임" <%= hobbyList != null && hobbyList.contains("게임") ? "checked" : "" %>><label for="hobby3">게임</label>
					<input type="checkbox" name="hobby" id="hobby4" value="여행" <%= hobbyList != null && hobbyList.contains("여행") ? "checked" : "" %>><label for="hobby4">여행</label><br />
				</td>
			</tr>
		</table>
        <input type="submit" value="정보수정"/>
        <input type="button" onclick="deleteMember();" value="탈퇴"/>
	</form>
</section>
<script>
	const url = 'https://developer.mozilla.org/en-US/docs/Web/API/HTMLFormElement/submit_event';
	/* const updateMember = () => {
		document.memberUpdateFrm.submit(); // form.submit()하는 경우 handler 호출 불가!
	}; */
	
	/**
	 * 회원수정폼 유효성 검사
	 */
	document.memberUpdateFrm.onsubmit = () => {
		// memberName
		if(!/^[가-힣]{2,}$/.test(memberName.value)) {
			alert("이름은 한글 2글자이상 입력해주세요.");
			return false;
		}
		
		// phone
		if(!/^010\d{8}$/.test(phone.value)) {
			alert("유효한 전화번호를 입력하세요.");
			return false;
		}
	};
	
	/**
	 * 회원탈퇴
	 */
	const deleteMember = () => {
		const title = "memberDeletePopup";
		const spec = "width=300px, height=200px";
		const popup = open("<%= request.getContextPath() %>/member/memberDelete", title, spec);
	};
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
