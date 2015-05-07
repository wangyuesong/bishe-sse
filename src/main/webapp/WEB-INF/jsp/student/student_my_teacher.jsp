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
    
    teacher_actions_datagrid = $('#teacher_actions_datagrid').datagrid(
        {
          url : '${pageContext.request.contextPath}/student/will/getAllTeachersForList',
          fitColumns : true,
          border : false,
          nowrap : false,
          pagination : true,
          pageSize : 10,
          frozenColumns : [ [ {
            field : 'id',
            title : '用户编号',
            width : 50,
            hidden : true
          } ] ],
          columns : [ [
              {
                field : 'name',
                title : '姓名',
                width : 40,
                formatter : function(value, rowData) {
                  return "<a href='javascript:void(0);' onclick='show_one_teacher_detail(" + rowData.id + ")'" + "'>"
                      + value + "</a>";
                }
              }, {
                field : 'capacity',
                title : '可接受学生数目',
                width : 40,
              }, {
                field : 'gender',
                title : '性别',
                width : 20,
                sortable : true,
              }, {
                field : 'email',
                title : '邮箱',
                width : 35,
                sortable : true,
              }, {
                field : 'phone',
                title : '电话',
                width : 55,
                sortable : true
              } ] ],
          toolbar : [ '-', {
            text : '刷新',
            iconCls : 'icon-reload',
            handler : function() {
              all_teachers_datagrid.datagrid('reload');
            }
          }, '-' ]
        });
  });
</script>
</head>
<body>
	<div class="section group">
		<div class="col span_1_of_8" style="color: #0099FF;">教师基本信息</div>
		<div class="col span_7_of_8">
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
		</div>
	</div>
	<div class="section group">
		<div class="col span_1_of_8">教师动态</div>
		<div class="col span_7_of_8">
			<div>
				<div id="teacher_actions_datagrid"></div>
			</div>
		</div>
	</div>

</body>
