<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="./resources/js/jquery-3.4.1.min.js"></script>
</head>
<script>
$(document).ready(function(){
	$.ajax({
		url: "papago",
		method: "post",
// 		data: {boardnum: ${board.boardnum}},
		success: function( data ) {
			console.log(data);
		},
		dataType: "json"
	});
})
</script>
<body>

</body>
</html>