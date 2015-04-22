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
  function show_one_teacher_detail(id) {
    selected_teacher_id = id;
    $('#one_teacher_datagrid').dialog({
      href : '${pageContext.request.contextPath}/dispatch/student/one_teacher_detail',
      onClose : function() {
      },
      width : $(document.body).width() * 0.9,
      height : $(document.body).height() * 0.9,
      collapsible : true,
      modal : true,
      title : '教师详细信息',
      /*   buttons : [ {
          text : '增加',
          iconCls : 'icon-add',
          handler : function() {
            alert("hello");
            var d = $(this).closest('.window-body');
            $('#document_add_form').submit();
          }
        } ], */
      onClose : function() {
        $(this).dialog('destroy');
      }
    });
  }
  var all_teachers_datagrid = null;
  $(function() {
    all_teachers_datagrid = $('#all_teachers_datagrid').datagrid(
        {
          url : '${pageContext.request.contextPath}/student/getAllTeachers',
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
                field : 'studentName',
                title : '学生姓名',
                width : 40,
                formatter : function(value, rowData) {
                  return "<a href='javascript:void(0);' onclick='show_one_teacher_detail(" + rowData.id + ")'" + "'>"
                      + value + "</a>";
                }
              }, {
                field : 'studentAccount',
                title : '学号',
                width : 40,
              }, {
                field : 'teacherName',
                title : '教师姓名',
                width : 20,
                sortable : true,
              }, {
                field : 'teacherAccount',
                title : '工号',
                width : 35,
                sortable : true,
              }, {
                field : 'matchLevel',
                title : '志愿匹配等级',
                width : 55,
                sortable : true
              } ] ],
          toolbar : [ '-', {
            text : '刷新',
            iconCls : 'icon-reload',
            handler : function() {
              all_teachers_datagrid.datagrid('reload');
            }
          }, '-', {
            text : '系统分配',
            iconCls : 'icon-reload',
            handler : function() {
              all_teachers_datagrid.datagrid('reload');
            }
          } ]
        });
  });
</script>
</head>
<body>
	<!-- <div id="dd" title="My Dialog" style="width: 400px; height: 200px;">
	Dialog Content.</div> -->
	<div id="tab" data-options="region:'center'" style="height: 100%">
		<table id="all_teachers_datagrid"></table>
	</div>
	<div id="one_teacher_datagrid"></div>
</body>
