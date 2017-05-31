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
	<script type="text/javascript">
		$(function(){
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
</head>
<body>
	<form action="" enctype="multipart/form-data" method="POST" id="uploadFile">
		<input type="file" name="file" />
		<input type="submit" value="上传" id="uploadButton">
	</form>
	<button>刷新文件列表</button>
	
	<div id="fileList"></div>
</body>
</html>
