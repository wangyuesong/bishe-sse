<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/inc.jsp"></jsp:include>
<script>
  
</script>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
	<div
		data-options="region:'north',href:'${pageContext.request.contextPath}/dispatch/student/layout_north'"
		style="height: 60px; overflow: hidden;"></div>
	<div
		data-options="region:'west',title:'功能导航',href:'${pageContext.request.contextPath}/dispatch/student/layout_west_menu'"
		style="width: 200px; overflow: hidden;"></div>
	<div
		data-options="region:'east',title:'日程事件',href:'${pageContext.request.contextPath}/dispatch/student/layout_east'"
		style="width: 300px;"></div>
	<div
		data-options="region:'center',title:'欢迎使用本系统',href:'${pageContext.request.contextPath}/dispatch/student/layout_center'"
		style="overflow: hidden;"></div>
	<div
		data-options="region:'south',href:'${pageContext.request.contextPath}/dispatch/student/layout_south'"
		style="height: 25px; overflow: hidden;"></div>
</body>
</html>