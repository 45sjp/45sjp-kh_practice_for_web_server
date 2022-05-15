<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
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
<section id=enroll-container>
	<h2>회원 정보</h2>
	<form name="memberUpdateFrm" method="post">
		<table>
			<tr>
				<th>아이디<sup>*</sup></th>
				<td>
					<input type="text" name="memberId" id="memberId" value="<%= loginMember.getMemberId() %>" readonly>
				</td>
			</tr>
			<tr>
				<th>패스워드<sup>*</sup></th>
				<td>
					<input type="password" name="password" id="password" value="<%= loginMember.getPassword() %>" required>
				</td>
			</tr>
			<tr>
				<th>패스워드확인<sup>*</sup></th>
				<td>	
					<input type="password" id="passwordCheck" value="" required><br>
				</td>
			</tr> 
			<tr>
				<th>이름<sup>*</sup></th>
				<td>	
				<input type="text"  name="memberName" id="memberName" value="<%= loginMember.getMemberName() %>" required><br>
				</td>
			</tr>
			<tr>
				<th>생년월일</th>
				<td>	
				<input type="date" name="birthday" id="birthday" value="<%= loginMember.getBirthday() %>"><br>
				</td>
			</tr> 
			<tr>
				<th>이메일</th>
				<td>	
					<input type="email" placeholder="abc@xyz.com" name="email" id="email" value="<%= loginMember.getEmail() %>"><br>
				</td>
			</tr>
			<tr>
				<th>휴대폰<sup>*</sup></th>
				<td>	
					<input type="tel" placeholder="(-없이)01012345678" name="phone" id="phone" maxlength="11" value="<%= loginMember.getPhone() %>" required><br>
				</td>
			</tr>
			<tr>
				<th>주소</th>
				<td>	
					<input type="text" placeholder="" name="address" id="address" value="<%= loginMember.getAddress() %>"><br>
				</td>
			</tr>
			<tr>
				<th>성별</th>
				<td>
			       		 <input type="radio" name="gender" id="gender0" value="M">
						 <label for="gender0">남</label>
						 <input type="radio" name="gender" id="gender1" value="F">
						 <label for="gender1">여</label>
				</td>
			</tr>
			<tr>
				<th>취미</th>
				<td>
					<input type="checkbox" name="hobby" id="hobby0" value="운동" ><label for="hobby0">운동</label>
					<input type="checkbox" name="hobby" id="hobby1" value="등산" ><label for="hobby1">등산</label>
					<input type="checkbox" name="hobby" id="hobby2" value="독서" ><label for="hobby2">독서</label><br />
					<input type="checkbox" name="hobby" id="hobby3" value="게임" ><label for="hobby3">게임</label>
					<input type="checkbox" name="hobby" id="hobby4" value="여행" ><label for="hobby4">여행</label><br />


				</td>
			</tr>
		</table>
        <input type="button" onclick="updateMember();" value="정보수정"/>
        <input type="button" onclick="deleteMember();" value="탈퇴"/>
	</form>
</section>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
