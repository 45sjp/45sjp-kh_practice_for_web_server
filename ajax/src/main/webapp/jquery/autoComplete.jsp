<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>jquery - autoComplete</title>
	<%-- jquery ui --%>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.13.1/themes/base/jquery-ui.css">
	<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
	<script src="https://code.jquery.com/ui/1.13.1/jquery-ui.js"></script>
</head>
<body>
	<h1>autoComplete</h1>
	<input id="names" autofocus>
	<script>
		$("#names").autocomplete({
			source(request, response) {
				console.log(request, response); // {term : 'a'}, function
	    	  
	    	  	$.ajax({
					url : "<%= request.getContextPath() %>/jquery/autoComplete",
	    		  	method : "GET",
	    		  	data : {
	    				search : request.term
	    		  	},
	    		  	dataType : "text",
	    		  	success(resp) {
						console.log(resp);
	    			  	const names = resp.split(",");
	    			  	response(
		    				names.map((name) => ({
		    			  		label : name,
		    			  		value : name
		 					}))
		 				);
	    			},
	    		  	error : console.log
	    	  	})
	      	},
	      	minLength : 1,
	      	delay : 500,
	      	focus(e, item) {
	      		console.log(e, item);
	      		return false; // 기본 작동하지 않음
	      	},
	      	select(e, {item}) {
	      		// 선택했을 때 처리 코드
	      		alert(item.value);
	      	}
	    });
	</script>
</body>
</html>