<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="jquery.js"></script>
<title>Insert title here</title>

<script type="text/javascript">

	function sengData(){
		
		
	$.ajax({
			url : "demo",
			context : document.body,
			success : function(data) {
				if(data == 'true'){
					$('#result').text('发送成功');
				}else{
					$('#result').text('发送失败');
				}
				
			}
		});
	}
	
	function heart(){
		//http://127.0.0.1:8080/OtherSystemJoinGCCP_demo/gccp/heartbeat?action=online
		var url = "http://59.215.85.3:80//gccp/receiveData";
		$.ajax({
				url : "http://59.215.85.3:80/gccp/receiveData",
				type: 'POST',
				success : function(data) {
					$('#heartvalue').text(JSON.stringify(data));
				}
			});
		}
</script>
</head>
<body>

	<!-- <input type="button" value="发送数据到办公厅" onclick="sengData()"/>
	<div id="result"></div> -->
	
	<div>心跳</div>
	<input type="button" value="提交" onclick="heart()" />
	<div id="heartvalue"></div>
</body>
</html>