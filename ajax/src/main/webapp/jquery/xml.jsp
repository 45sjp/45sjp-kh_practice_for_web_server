<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>xml</title>
<script src="<%= request.getContextPath() %>/js/jquery-3.6.0.js"></script>
<style>
	table {border : 1px solid #000; border-collapse: collapse; margin: 10px 0;}
	th, td {border : 1px solid #000; text-align: center; padding: 3px;}
	table img {width: 100px;}
</style>
</head>
<body>
	<h1>xml</h1>
	<button id="btn1">sample</button>
	<table id="tbl-books">
		<thead>
			<tr>
				<th>분류</th>
				<th>제목</th>
				<th>저자</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<script>
		btn1.addEventListener('click', () => {
			$.ajax({
				url : "<%= request.getContextPath() %>/jquery/sample.xml",
				method : "GET",
				dataType : "xml",
				success(doc) {
					// 응답 받은 xml을 jquery가 parsing처리 후 DOM으로 전달
					console.log(doc);
					console.dir(doc); // document
					
					const root = doc.querySelector(":root");
					console.dir(root);
					
					// 사용자 속성 가져오기
					// 객체에서 바로 관리되는 것이 아닌 NamedNodeMape으로 관리되고 있음
					console.log(root.myattr); // undefined => attributes(NamedNoteMap타입)속성에서 관리됨
					console.log(root.getAttribute("myattr")); // hello
					
					const tbody = document.querySelector("#tbl-books tbody");
					tbody.innerHTML = "";
					
					// const books = root.children; // forEach 사용 불가
					const books = [...root.children]; // Array.from(유사배열)
					console.log(books);
					
					books.forEach((book) => {
						const [subject, title, author] = book.children; // 배열구조분해할당
						// console.log(subject, title, author);
						
						// innerText속성 없음! innerHTML속성은 있음
						tbody.innerHTML += `<tr>
							<td>\${subject.textContent}</td>
							<td>\${title.textContent}</td>
							<td>\${author.innerHTML}</td>
						</tr>`;
					});
				},
				error : console.log
			});
		});
	</script>
	
	<button id="btn2">celeb</button>
	<table id="tbl-celeb">
		<thead>
			<tr>
				<th>No</th>
				<th>이름</th>
				<th>타입</th> <!-- select태그 하위 해당타입이 selected 처리 -->
				<th>프로필</th> <!-- img태그 처리 -->
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<script>
		btn2.addEventListener('click', () => {
			$.ajax({
				url : "<%= request.getContextPath() %>/jquery/xml",
				// dataType : "xml", // 응답데이터를 보고 자동으로 지정
				success(doc) {
					console.log(doc); // xml doc임
					const root = doc.querySelector(":root"); // celebs태그
					console.log(`총 \${root.getAttribute("len")}개의 데이터가 조회되었습니다.`);
					
					const celebs = [...root.children]; // 전개 연산자
					document.querySelector("#tbl-celeb tbody").innerHTML = 
						celebs.reduce((html, celeb) => {
							const [name, type, profile] = celeb.children; // iterator를 상속(진짜배열, 유사배열)
							const tr = `<tr>
								<td>\${celeb.getAttribute("no")}</td>
								<td>\${name.textContent}</td>
								<td>\${type.textContent}</td>
								<td>
									<img src="<%= request.getContextPath() %>/images/\${profile.textContent}"/>
								</td>
							</tr>`;
							return html + tr;
						}, "");
				},
				error : console.log
			});
		});
	</script>
	
	<hr />
	<h2>일일 박스오피스 조회</h2>
	<div><input type="date" name="targetDt" id="targetDt" /></div>
	<table id="tbl-daily-boxoffice">
		<thead>
			<tr>
				<th>순위</th> <%-- rank --%>
				<th>영화제목</th> <%-- movieNm --%>
				<th>누적관객수(만)</th> <%-- audiAcc --%>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<script>
		/**
		 * @실습문제 - 페이지 로딩이 완료되면 어제 날짜로 박스오피스 조회를 자동으로 처리
		 */
		window.onload = () => {
			
			let today = new Date();
			today.setHours(today.getHours() + 9);
			
			let yesterday = new Date(today.setDate(today.getDate() - 1));
			yesterday = yesterday.toISOString().replace('T', '').slice(0, 10);

			targetDt.value = yesterday;
			renderDailyBoxOffice();
		};
		
		targetDt.addEventListener('change', (e) => {
			renderDailyBoxOffice();
		});
		
		/**
		 * 페이지에 일일 박스오피스 정보 렌더링
		 */
		const renderDailyBoxOffice = () => {
			const val = document.querySelector("#targetDt").value.replace(/-/g, ""); // 2022-05-26 -> 20220526
			console.log(val);
			
			$.ajax({
				url : "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.xml",
				data : {
					key : 'b8c11d3f42bff204ab05818bd8bcb327',
					targetDt : val
				},
				success(doc) {
					console.log(doc);
					
					// doc -> html parsing
					const root = doc.querySelector(":root");
					console.log(root);
					
					const movies = [...root.lastChild.children];
					console.log(movies);
					
					const tbody = document.querySelector("#tbl-daily-boxoffice tbody");
					tbody.innerHTML = ""; // 초기화
					
					tbody.innerHTML = 
						movies.reduce((html, movie) => {
							const rank = movie.getElementsByTagName("rank")[0].textContent;
							const movieNm = movie.getElementsByTagName("movieNm")[0].textContent;
							let audiAcc = movie.getElementsByTagName("audiAcc")[0].textContent;
							audiAcc = Math.floor(Number(audiAcc / 10000) * 10) / 10;
							
							const tr = `<tr>
								<td>\${rank}</td>
								<td>\${movieNm}</td>
								<td>\${audiAcc}</td>
							</tr>`;
							
							console.log(rank, movieNm, audiAcc);
							return html + tr;
						}, "");
				},
				error : console.log
			});
		};
	</script>
</body>
</html>