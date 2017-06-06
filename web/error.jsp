<%--
  Created by IntelliJ IDEA.
  User: cellargalaxy
  Date: 2017/5/31
  Time: 10:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title>异常</title>
	<style type="text/css">
		body {
			background-image: url("background.jpg");
			background-repeat: no-repeat;
			background-attachment: fixed;
			background-size: cover;
		}
		
		div {
			position: relative;
			width: 60%;
			height: 60%;
			margin-right: 20%;
			float: right;
			text-align: center;
			background-color: rgba(255, 255, 255, 0.8);
		}
	</style>
</head>
<body>
<div>
	<img src="error.png">
	<h2>发生异常啦！</h2>
	<h2>${error}</h2>
</div>
</body>
</html>
