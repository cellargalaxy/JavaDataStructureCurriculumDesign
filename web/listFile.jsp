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
</head>
<body>

<table border="1">
	<tr><td>文件名</td><td>源文件大小</td><td>压缩文件大小</td><td>压缩率</td></tr>
	
	<c:if test="${cd}">
		<c:forEach var="file" items="${compressionFiles}">
			<tr>
				<td>${file.fileName}</td>
				<td>${file.originalSize}</td>
				<td>${file.laterSize}</td>
				<td>${file.ratio}</td>
			</tr>
		</c:forEach>
	</c:if>
	
	<c:if test="${!cd}">
		<c:forEach var="file" items="${decompressionFiles}">
			<tr>
				<td>${file.fileName}</td>
				<td>${file.originalSize}</td>
				<td>${file.laterSize}</td>
				<td>${file.ratio}</td>
			</tr>
		</c:forEach>
	</c:if>
	
</table>

</body>
</html>
