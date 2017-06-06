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
	<script type="application/javascript" src="jquery-3.2.1.min.js"></script>
	<script src="cytoscape.min.js"></script>
	<script src="dagre.min.js"></script>
	<script src="cytoscape-dagre.js"></script>
	<style type="text/css">
		body {
			background-image: url("background.jpg");
			background-repeat: no-repeat;
			background-attachment: fixed;
			background-size: cover;
		}
		
		#main {
			position: absolute;
			left: 1%;
			top: 1%;
			width: 69%;
			height: 300%;
			/*border: 1px solid lightgray;*/
			/*background-color: #FFCC66;*/
			background-color: rgba(255, 255, 255, 0.6);
			border-radius: 1em;
		}
		
		#side {
			position: absolute;
			left: 70%;
			top: 1%;
			width: 29%;
			height: auto;
			/*border: 1px solid lightgray;*/
		}
		
		#inquire {
			position: fixed;
			top: 0.5em;
			right: 1%;
			width: 28%;
			height: auto;
			text-align: center;
			z-index: 1;
			/*border: 1px solid lightgray;*/
		}
		
		#goTop {
			position: fixed;
			top: 90%;
			right: 1%;
			width: 5%;
			height: 5%;
			min-width: 3em;
			min-height: 2em;
			text-align: center;
			z-index: 1;
			/*border: 1px solid lightgray;*/
			background-color: rgba(255, 255, 255, 0.9);
			border-radius: 1em;
		}
		
		.space {
			position: relative;
			height: 9em;
			width: 96%;
			margin: 2%;
			float: right;
			text-align: center;
			/*border: 1px solid lightgray;*/
		}
		
		#answers {
			position: relative;
			height: 100%;
			width: 100%;
			margin-top: 3%;
			float: right;
			text-align: center;
		}
		
		.answer {
			position: relative;
			height: auto;
			width: 96%;
			margin: 2%;
			float: right;
			text-align: center;
		}
		
		.cen {
			position: relative;
			height: auto;
			width: 100%;
			margin: 1% auto;
			/*background-color:#FFCC66;*/
			background-color: rgba(255, 255, 255, 0.9);
			border-radius: 1em;
		}
		
		button {
			background-color: rgba(255, 255, 255, 0.1);
			border-radius: 1em;
		}
	</style>
</head>
<body>
<div id="inquire" style="">
	<div class="cen">起点：<input type="text" name="start" style="width: 80%;max-width: 15em" readonly></div>
	<div class="cen">终点：<input type="text" name="end" style="width: 80%;max-width: 15em" readonly></div>
	<div class="cen">
		<button id="clearInquire">清空查询框</button>
		<button id="inquireButton">查询最短乘车路径</button>
	</div>
	<input type="hidden" name="startId">
	<input type="hidden" name="endId">
</div>
<div id="goTop">top</div>

<div id="main"></div>
<div id="side">
	<div class="space"></div>
	<div class="space" style="height: auto;background-color: rgba(255,255,255,0.6)">
		<h4>上传数据集</h4>
		<form action="" method="post" enctype="multipart/form-data">
			<input type="file" name="dataSet" multiple="multiple" style="width: 80%;max-width: 15em" required>
			<input type="submit" value="上传数据集" style="width: 80%;max-width: 15em">
		</form>
	</div>
	<div id="answers"></div>
</div>

</body>
</html>

<script type="application/javascript">
	
	$('#goTop').fadeOut(500);
	$(window).scroll(function (e) {
		if ($(window).scrollTop() > 100) {
			$('#goTop').fadeIn(500);
		} else {
			$('#goTop').fadeOut(500);
		}
	});
	
	$('#goTop').click(function (e) {
		$('body,html').animate({scrollTop: 0}, 500);
	});
	
	$('#inquireButton').click(function () {
		startId = $('input[name=startId]').val();
		endId = $('input[name=endId]').val();
		if (startId == '') {
			alert('请输入出发地点！');
			return;
		}
		if (endId == '') {
			alert('请输入目的地点！');
			return;
		}
		$.ajax({
			url: '${pageContext.request.contextPath}/busJson',
			type: 'post',
			data: {"startId": startId, "endId": endId},
			
			error: function () {
				alert("网络错误!");
			},
			success: function (data) {
				htm = '';
				if (data.length == 0) {
					htm += '<div class="answer"><div class="cen">无法到达目的地</div></div>';
				} else {
					$.each(data, function (key, val) {
						htm += '\n' + createRoute(key, val);
					});
				}
				$('#answers').html(htm);
			}
		});
		
		$('body,html').animate({scrollTop: 0}, 1000);
	});
	
	function createRoute(key, route) {
		htm = '<div class="answer"><p class="cen">线路' + (key + 1) + '</p>';
		$.each(route, function (key, val) {
			htm += '\n<p class="cen">到 "' + val.start + '" 乘坐 "' + val.bus + '" 车，到 "' + val.end + '" 下车</p>';
		});
		htm += '\n<p class="cen">到达目的地</p>\n</div>';
		return htm;
	}
	
	$('#clearInquire').click(function () {
		$('input[name=start]').val('');
		$('input[name=startId]').val('');
		$('input[name=end]').val('');
		$('input[name=endId]').val('');
	});
	
	$.ajax({
		url: '${pageContext.request.contextPath}/busJson',
		type: 'get',
		
		error: function () {
			alert("网络错误!");
		},
		success: function (data) {
			tu('main', data);
		}
	});
	
	function tu(idName, elem) {
		object = {
			container: document.getElementById(idName),
			
			boxSelectionEnabled: false,
			autounselectify: true,
			
			layout: {
				name: 'dagre'
			},
			
			style: [
				{
					selector: 'node',
					style: {
						'content': 'data(name)',
						'font-size': 25,
						'text-opacity': 1,
						'text-valign': 'center',
						'text-halign': 'right',
						'background-color': '#11479e'
					}
				},
				
				{
					selector: 'edge',
					style: {
						'content': 'data(len)',
						'font-size': 20,
						'target-arrow-shape': 'triangle',
						'line-color': '#9dbaea',
						'target-arrow-color': '#9dbaea'
					}
				}
			],
			
			elements: elem
		};
		
		var cy = cytoscape(object);
		cy.on('tap', 'node', function (evt) {
			var node = evt.target;
			addSite(node.data('id'), node.data('name'));
		});
		cy.fit();
//		cy.userPanningEnabled( false );
		cy.userZoomingEnabled(false);
		
		var layout = cy.layout({
			name: 'preset'
		});
		
		layout.run();
	}
	
	function addSite(id, name) {
		if ($('input[name=start]').val() == '') {
			$('input[name=start]').val(name);
			$('input[name=startId]').val(id);
		} else if ($('input[name=end]').val() == '') {
			$('input[name=end]').val(name);
			$('input[name=endId]').val(id);
		}
	}
</script>
