<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>jquery - text</title>
	<script src="<%= request.getContextPath() %>/js/jquery-3.6.0.js"></script>
	<style>
		table {border : 1px solid #000; border-collapse: collapse; margin: 10px 0;}
		th, td {border : 1px solid #000; text-align: center; padding: 3px;}
		table img {width: 100px;}
	</style>
</head>
<body>
	<h1>text</h1>
	<button id="btn1">text</button>
	<script>
		btn1.addEventListener('click', (e) => {
			// 이 안에 필요한 모든 것들이 객체로 저장되어 있음
			$.ajax({
				url : "<%= request.getContextPath() %>/jquery/text",
				method : "GET", // 전송방식 : GET(기본값) | POST | PUT | PATCH ...
				data : {
					q : "abcde",
					mode : 123,
					isFinal : true
				}, // 전달할 값(객체 형태). 사용자 입력값 직렬화 처리 후 GET방식이면 url에 추가, POST방식이면 body부분에 작성
				dataType : "text", // 응답데이터의 타입 지정 : text | html | script | json | xml
				beforeSend() {
					// 요청 전 호출 메소드
					console.log("beforeSend");
				},
				success(responseText) {
					// xhr.responseText를 후처리한 후 success 메소드에 전달!
					// readyState === 4 %% status === 200
					console.log("success : ", responseText); // 성공시 호출
				},
				error(xhr, textStatus, err) {
					// readyState === 4 %% status !== 200
					console.log("error : ", jqxhr, textStatus, err); // 실패시 호출
				},
				complete() {
					// 응답 후(성공, 실패) 반드시 실행하는 메소드
					// 성공여부에 상관 없이 반드시 실행됨. finally 같은 것!
					console.log("complete");
				}
			});
		});
	</script>
	
	<button id="btn2">csv</button>
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
		/*
		 * CSV : Comma Separated Values
		 */
		 btn2.addEventListener('click', (e) => {
			$.ajax({
				url : "<%= request.getContextPath() %>/jquery/csv",
				method : "GET",
				dataType : "text",
				success(response) {
					console.log(response);
					
					const celebStrs = response.split("\n");
					const tbody = document.querySelector("#tbl-celeb tbody");
					tbody.innerHTML = "";
					
					celebStrs.forEach((celebStr) => {
						if(celebStr === '') return; // 마지막 '' 
						
						const celeb = celebStr.split(", ");
						const tr = document.createElement("tr");
						const tdNo = document.createElement("td");
						tdNo.append(celeb[0]);
						const tdName = document.createElement("td");
						tdName.append(celeb[1]);
						const tdType = document.createElement("td");
						const select = document.createElement("select");
						
						// ACTOR, SINGER, MODEL, COMEDIAN, ENTERTAINER;
						const option1 = document.createElement("option")
						option1.value = "ACTOR";
						option1.innerHTML = "ACTOR";
						
						const option2 = document.createElement("option")
						option2.value = "SINGER";
						option2.innerHTML = "SINGER";
						
						const option3 = document.createElement("option")
						option3.value = "MODEL";
						option3.innerHTML = "MODEL";
						
						const option4 = document.createElement("option")
						option4.value = "COMEDIAN";
						option4.innerHTML = "COMEDIAN";
						
						const option5 = document.createElement("option")
						option5.value = "ENTERTAINER";
						option5.innerHTML = "ENTERTAINER";
						
						select.append(option1, option2, option3, option4, option5);
						select.value = celeb[2];
						
						// select.disabled = "disabled";
						// 값 변경 금지
						select.dataset.value = celeb[2];
						select.onchange = function() {
							this.value = this.dataset.value; // 값이 바뀌려고 하면 이전 값으로 돌려놓는 것
						}
						
						tdType.append(select);
						const tdProfile = document.createElement("td");
						const img = document.createElement("img");
						img.src = `<%= request.getContextPath() %>/images/\${celeb[3]}`
						tdProfile.append(img);
						
						tr.append(tdNo, tdName, tdType, tdProfile);
						tbody.append(tr);
					});
					
					<%-- 
					
					내 실습문제 풀이 미완성
										
					console.log("success : ", response);
					let temp = response.split('\r\n');
					temp = temp.slice(0, temp.length - 1);
					console.log(temp);
					/*
					 	[
					 		'1, daft punk, SINGER, daftpunk.jpg',
					 		'2, hwang, COMEDIAN, hwang.jpg',
					 		'3, 줄리아 로버츠, ACTOR, juliaRoberts.jpg',
					 		'4, 맷 데이먼, ACTOR, mattDamon.jpg',
					 		'5, 유재석, COMEDIAN, 유재석.jpg'
					 	]
					 */
					
					temp.forEach((tmp, index) => {
						const arr = tmp.split(', ');
		                console.log(tmp); // 1, daft punk, SINGER, daftpunk.jpg
		                console.log(arr); // ['1', ' daft punk', ' SINGER', ' daftpunk.jpg']
		                
		                const tr = `<tr>
	<td>\${arr[0]}</td>
	<td>\${arr[1]}</td>
	<td>\${arr[2]}</td>
	<td>
		<img src="<%= request.getContextPath() %>/images/\${arr[3]}" alt="\${arr[1]} 사진" />
	</td>
</tr>`;
						console.log(tr);
						document.querySelector("#tbl-celeb tbody").innerHTML += tr;
		            });
		            
		            --%>
				},
				error(xhr, textStatus, err) {
					console.log("error : ", xhr, textStatus, err);
				}
			});
		});
	</script>
</body>
</html>