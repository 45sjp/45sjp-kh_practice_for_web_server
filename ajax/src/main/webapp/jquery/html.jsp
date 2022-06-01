<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>jquery - html</title>
	<script src="<%= request.getContextPath() %>/js/jquery-3.6.0.js"></script>
	<style>
		table {border : 1px solid #000; border-collapse: collapse; margin: 10px 0;}
		th, td {border : 1px solid #000; text-align: center; padding: 3px;}
		table img {width: 100px;}
	</style>
</head>
<body>
	<h1>html</h1>
	<button id="btn1">html</button>
	<table id="tbl-celeb">
		<thead>
			<tr>
				<th>No</th>
				<th>이름</th>
				<th>타입</th> <!-- select태그 하위 해당타입이 selected 처리 -->
				<th>프로필 이미지</th> <!-- img태그 처리 -->
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<script>
		btn1.addEventListener('click', () => {
			$.ajax({
				url : "<%= request.getContextPath() %>/jquery/html",
				method : "GET",
				dataType : "html",
				success(response) {
					console.log(response);
					document.querySelector("#tbl-celeb tbody").innerHTML = response;
				},
				error : console.log // 에러에 전달할 매개인자를 고스란히 출력함
			});
		});
	</script>
</body>
</html>