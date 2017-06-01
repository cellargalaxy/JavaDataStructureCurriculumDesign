<%--
  Created by IntelliJ IDEA.
  User: cellargalaxy
  Date: 2017/5/31
  Time: 23:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title>公交路线</title>
	<script type="text/javascript" src="vis.js"></script>
	<link href="vis-network.min.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		#mynetwork {
			width: 600px;
			height: 400px;
			border: 1px solid lightgray;
		}
	</style>
	
</head>
<body>
<div id="mynetwork"></div>

<script type="text/javascript">
	// create an array with nodes
	var nodes = new vis.DataSet([
		{id: 1, label: 'Node 1'},
		{id: 2, label: 'Node 2'},
		{id: 3, label: 'Node 3'},
		{id: 4, label: 'Node 4'},
		{id: 5, label: 'Node 5'}
	]);
	
	// create an array with edges
	var edges = new vis.DataSet([
		{from: 1, to: 2, label: 'label1'},
		{from: 1, to: 3, label: 'label2'},
		{from: 2, to: 4, label: 'label3'},
		{from: 2, to: 5, label: 'label3'}
	]);
	
	// create a network
	var container = document.getElementById('mynetwork');
	var data = {
		nodes: nodes,
		edges: edges
	};
	var options = {};
	var network = new vis.Network(container, data, options);
</script>
</body>
</html>
