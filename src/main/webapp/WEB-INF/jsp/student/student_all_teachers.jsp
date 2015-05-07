<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="/inc.jsp"></jsp:include>
<style>
.input {
	width: 170px;
}
</style>
<script>
  //Used to store the selected teacher id, will be used in one_teacher_detail.jsp
  var selected_teacher_id;

  function show_one_teacher_detail(id) {
    selected_teacher_id = id;
    $('#one_teacher_datagrid').dialog({
      href : '${pageContext.request.contextPath}/dispatch/student/student_one_teacher_detail',
      onClose : function() {
      },
      width : $(document.body).width() * 0.9,
      height : $(document.body).height() * 0.9,
      collapsible : true,
      modal : true,
      title : '教师详细信息',
      onClose : function() {
        $(this).dialog('destroy');
      }
    });
  }
  var all_teachers_datagrid = null;
  $(function() {
    all_teachers_datagrid = $('#all_teachers_datagrid').datagrid(
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
<body id="shit">
	<!-- <div id="dd" title="My Dialog" style="width: 400px; height: 200px;">
	Dialog Content.</div> -->
	<div id="tab" data-options="region:'center'" style="height: 100%">
		<table id="all_teachers_datagrid"></table>
	</div>
	<div id="one_teacher_datagrid"></div>
</body>
