<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%
	List<Member> list = (List<Member>) request.getAttribute("list");
	String searchType = request.getParameter("searchType"); // member_id member_name gender
	String searchKeyword = request.getParameter("searchKeyword");
%>
<!-- 관리자용 admin.css link -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin.css" />
<style>
    div#search-container {width:100%; margin:0 0 10px 0; padding:3px; background-color: rgba(0, 188, 212, 0.3);}
    div#search-memberId {display:inline-block;}
    div#search-memberName {display:none;}
    div#search-gender {display:none;}
</style>
<section id="memberList-container">
	<h2>회원관리</h2>
	<div id="search-container">
	    <label for="searchType">검색타입 :</label> 
	    <select id="searchType">
	    	<%-- 검색타입 유지 --%>
	    	<option value="member_id" ${(param.searchType == "member_id") ? "selected" : ""}>아이디</option>
	        <option value="member_name" ${(param.searchType == "member_name") ? "selected" : ""}>회원명</option>
	        <option value="gender" ${(param.searchType == "gender") ? "selected" : ""}>성별</option>
	    </select>
	    <div id="search-memberId" class="search-type">
	        <form action="<%=request.getContextPath()%>/admin/memberFinder">
	            <input type="hidden" name="searchType" value="member_id"/>
	            <input type="text" name="searchKeyword" size="25" placeholder="검색할 아이디를 입력하세요."/>
	            <button type="submit">검색</button>
	        </form>    
	    </div>
	    <div id="search-memberName" class="search-type">
	        <form action="<%=request.getContextPath()%>/admin/memberFinder">
	            <input type="hidden" name="searchType" value="member_name"/>
	            <input type="text" name="searchKeyword" size="25" placeholder="검색할 이름을 입력하세요."/>
	            <button type="submit">검색</button>            
	        </form>
	    </div>
	    <div id="search-gender" class="search-type">
	        <form action="<%=request.getContextPath()%>/admin/memberFinder">
	            <input type="hidden" name="searchType" value="gender"/>
	            <input type="radio" name="searchKeyword" value="M" checked> 남
	            <input type="radio" name="searchKeyword" value="F"> 여
	            <button type="submit">검색</button>
	        </form>
	    </div>
	</div>
	<table id="tbl-member">
		<thead>
			<tr>
				<th>아이디</th>
				<th>이름</th>
				<th>회원권한</th>
				<th>성별</th>
				<th>생년월일</th>
				<th>이메일</th>
				<th>전화번호</th>
				<th>주소</th>
				<th>취미</th>
				<th>가입일</th>
			</tr>
		</thead>
		<tbody>
			<%
				if(list != null && !list.isEmpty() && searchKeyword != "") {
					for(Member member : list) {
			%>
					<tr>
						<td><%= member.getMemberId() %></td>
						<td><%= member.getMemberName() %></td>
						<td>
							<select class="member-role" data-member-id="<%= member.getMemberId() %>">
								<option value="<%= MemberRole.A %>" <%= member.getMemberRole() == MemberRole.A ? "selected" : "" %>>관리자</option>
								<option value="<%= MemberRole.U %>" <%= member.getMemberRole() == MemberRole.U ? "selected" : "" %>>일반회원</option>
							</select>
						</td>
						<td><%= member.getGender() != null ? member.getGender() : "" %></td>
						<td><%= member.getBirthday() != null ? member.getBirthday() : "" %></td>
						<td><%= member.getEmail() != null ? member.getEmail() : "" %></td>
						<td><%= member.getPhone() %></td>
						<td><%= member.getAddress()  != null ? member.getAddress() : "" %></td>
						<td><%= member.getHobby()  != null ? member.getHobby() : "" %></td>
						<td><%= member.getEnrollDate() %></td>
					</tr>
			<%		
				 	}
				}
				else {
			%>
					<tr>
						<td colspan="10">조회된 회원이 없습니다.</td>
					</tr>
			<% 
				}
			%>		
		</tbody>
	</table>
</section>
<form action="<%= request.getContextPath() %>/admin/memberRoleUpdate" name="updateMemberRoleFrm" method="POST">
	<input type="hidden" name="memberId" />
	<input type="hidden" name="memberRole" />
</form>
<script>
	/**
	 * 검색값 유지
	 */
	<% if(searchType != null && searchKeyword != null) { %>
		document.querySelectorAll(".search-type").forEach((div) => {
			div.style.display = "none";
		});
		
		const type = "<%= searchType %>";
		const keyword = "<%= searchKeyword %>";
		let id = "";

		switch (type) {
			case "member_id": id = "search-memberId"; break;
			case "member_name": id = "search-memberName"; break;
			case "gender": id = "search-gender"; break;
		}

		const searchVal = document.querySelector(`#\${id}`);
		searchVal.style.display = "inline-block";
		
		if(type === "gender") {
			if(keyword === "F") {
				searchVal.querySelector("input[value=F]").checked = true;
			}
			else if(keyword === "M") {
				searchVal.querySelector("input[value=M]").checked = true;
			}
		}
		else {
			searchVal.querySelector("input[name=searchKeyword]").value = keyword;
		}
	<% } %>
	
	/**
	 * 회원정보 검색 타입에 따른 분기
	 */
	searchType.addEventListener('change', (e) => {
		const {value} = e.target; // 구조분해할당
		console.log(value);
		
		document.querySelectorAll(".search-type").forEach((div) => {
			div.style.display = "none";
		});
		
		let id = "";
		switch (value) {
			case "member_id": id = "search-memberId"; break;
			case "member_name": id = "search-memberName"; break;
			case "gender": id = "search-gender"; break;
		}
		document.querySelector(`#\${id}`).style.display = "inline-block";
	});

	/**
	 * 회원권한 변경
	 */
	document.querySelectorAll(".member-role").forEach((select) => {
		select.addEventListener('change', (e) => {
			// 확인용
			// console.dir(e.target);
			// console.log(e.target);
			// console.log(e.target.dataset.memberId); // key값을 조회시에는 camelcasing으로 참조
			// console.log(e.target.value); // "U" / "A"
			
			const memberId = e.target.dataset.memberId;
			const memberRole = e.target.value;
			
			/**
			 * jsp에서 js의 Template String(ES6) 문법 사용시 반드시 escaping 처리할 것!
			 * 	=> jsp의 EL문법과 충돌하기 때문
			 */
			if(confirm(`[\${memberId}]의 권한을 [\${memberRole}]로 변경하시겠습니까?`)) {
				const frm = document.updateMemberRoleFrm;
				// frm.memberId : form 하위의 input태그의 memberId
				// memberId : script 함수 안에 선언한 변수
				frm.memberId.value = memberId;
				frm.memberRole.value = memberRole;
				frm.submit();
			}
			else {
				// 취소할 경우 변경 전 값으로 되돌아가게 처리
				// select태그 하위에서 selected 속성이 있는 option을 찾고 selected 속성을 true로 만드는 것!
				e.target.querySelector("[selected]").selected = true;
			}
		});
	});
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
