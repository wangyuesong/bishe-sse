<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/inc.jsp"></jsp:include>
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/col.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/2cols.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/8cols.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/5cols.css"
	rel="stylesheet">
<style>
.input {
	width: 170px;
}
</style>
<script>
  $(function() {
    $
        .ajax({
          url : "${pageContext.request.contextPath}/student/will/showOneTeacherDetail?teacherId=${sessionScope.USER.teacher.id}",
          type : "get",
          success : function(data, textStatus) {
            $.each(data, function(key, value) {
              $('#' + key).html(value);
            });
          }
        });

    teacher_actions_datagrid = $('#teacher_actions_datagrid')
        .datagrid(
            {
              url : '${pageContext.request.contextPath}/student/will/getTeacherActionEventsInDatagrid?teacherId=${sessionScope.USER.teacher.id}',
              fitColumns : true,
              border : false,
              //这里sortname采用的是Entity中的名字，和后端耦合了
              sortName : 'createTime',
              sortOrder : 'ASC',
              nowrap : false,
              pagination : true,
              pageSize : 10,
              frozenColumns : [ [ {
                field : 'id',
                title : 'id',
                width : 50,
                hidden : true
              } ] ],
              columns : [ [ {
                field : 'create_time',
                title : '时间',
                width : 40,
              }, {
                field : 'description',
                title : '操作',
                width : 200,
                sortable : true,
              } ] ]
            });

    var p = $('#teacher_actions_datagrid').datagrid('getPager');
    $(p).pagination({
      pageSize : 10,
      pageList : [ 5, 10, 15 ],
      beforePageText : "第",
      afterPageText : "页,共{pages}页"
    });
  });
</script>
</head>
<body>
	<div class="section group">
		<div class="col span_8_of_8">
			<fieldset>
				<legend>教师详细信息</legend>
				<div>
					<table width="98%" border="0" cellpadding="2" class="tableForm"
						cellspacing="1" bgcolor="#D1DDAA" align="center"
						style="margin-top: 8px">
						<tr bgcolor="#FAFAF1">
							<td width="15%">姓名:</td>
							<td id="name" width="35%"></td>
							<td width="15%">职称:</td>
							<td id="title" width="35%"></td>
						</tr>
						<tr bgcolor="#FAFAF1">
							<td width="15%">邮箱:</td>
							<td id="email" width="35%"></td>
							<td width="15%">电话:</td>
							<td id="phone" width="35%"></td>
						</tr>
						<tr bgcolor="#FAFAF1">
							<td width="15%">学历:</td>
							<td id="degree" width="35%"></td>
							<td width="15%">计划指导人数:</td>
							<td id="capacity" width="35%"></td>
						</tr>
						<tr bgcolor="#FAFAF1">
							<td width="15%">方向:</td>
							<td id="direction" width="35%"></td>
							<td width="15%">备选题目:</td>
							<td id="candidateTopics" width="35%"></td>
						</tr>
					</table>
				</div>
			</fieldset>
		</div>
	</div>
	<div class="section group">
		<fieldset>
			<legend>教师动态</legend>
			<div class="col span_8_of_8">
				<div>
					<div id="teacher_actions_datagrid"></div>
				</div>
			</div>
		</fieldset>
	</div>
	<script>
    $("fieldset").css("border", "1px #99BBE8 dashed").css("padding", "20px").attr("align", "left");
    $("legend").css("color", "#0099FF").attr("align", "left");
  </script>

</body>
