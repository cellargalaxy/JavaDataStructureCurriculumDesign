<%--
  Created by IntelliJ IDEA.
  User: cellargalaxy
  Date: 2017/5/31
  Time: 9:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>文件列表</title>
	<style type="text/css">
		tr {
			text-align: center;
			background-color: rgba(255, 255, 255, 0.8)
		}
		
		td {
			border-radius: 10%;
		}
	</style>
</head>
<body>

<table border="0">
	
	<c:if test="${cd}">
		<tr>
			<td>文件名</td>
			<td>源文件大小</td>
			<td>压缩文件大小</td>
			<td>压缩率</td>
		</tr>
		<c:forEach var="file" items="${compressionFiles}">
			<tr>
				<td>${file.fileName}</td>
				<td>${file.originalSize}K</td>
				<td>${file.laterSize}K</td>
				<td>${file.ratio}%</td>
			</tr>
		</c:forEach>
	</c:if>
	
	<c:if test="${!cd}">
		<tr>
			<td>文件名</td>
			<td>压缩文件大小</td>
			<td>源文件大小</td>
			<td>解压率</td>
		</tr>
		<c:forEach var="file" items="${decompressionFiles}">
			<tr>
				<td>${file.fileName}</td>
				<td>${file.originalSize}K</td>
				<td>${file.laterSize}K</td>
				<td>${file.ratio}%</td>
			</tr>
		</c:forEach>
	</c:if>

</table>

</body>
</html>
