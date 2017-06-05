<%--
  Created by IntelliJ IDEA.
  User: cellargalaxy
  Date: 2017/5/28
  Time: 15:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title>${title}</title>
	<script type="application/javascript" src="jquery-3.2.1.min.js"></script>
	<style type="text/css">
		body {
			background-image: url("background.jpg");
			text-align: center;
		}
		
		.list {
			position: relative;
			width: auto;
			height: 50%;
			margin-right: 10%;
			float: right;
			text-align: center;
			background-color: rgba(255, 255, 255, 0.5);
		}
		
		.button {
			background-color: rgba(255, 255, 255, 0.5);
			border-radius: 1em;
		}
	</style>
</head>
<body>
<div class="list">
	<form action="" enctype="multipart/form-data" method="POST" id="uploadFile">
		<input type="file" name="file" required/>
		<input type="submit" value="上传" id="uploadButton" class="button">
	</form>
	<button class="button">刷新文件列表</button>
	
	<div id="fileList" style="width: 100%"></div>
</div>

</body>
</html>

<script type="text/javascript">
	$(function () {
		$("button").click(
			function () {
				listFile();
			}
		);
		$("#fileList").onload(
			listFile()
		);
	});
	
	function listFile() {
		$.ajax({
			url: '${pageContext.request.contextPath}/listFile',
			type: 'get',
			
			error: function () {
				alert("网络错误!");
			},
			success: function (data) {
				$("#fileList").html(data);
			}
		})
	}
</script>