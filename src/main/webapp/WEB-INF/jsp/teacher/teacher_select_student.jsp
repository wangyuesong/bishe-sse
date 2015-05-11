<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/inc.jsp"></jsp:include>
<!-- Uploadify -->
<link
	href="${pageContext.request.contextPath}/resources/style/uploadify.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jquery.uploadify.js"></script>
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/col.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/2cols.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/6cols.css"
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
  function accept_will(willId) {
    $.ajax({
      url : "${pageContext.request.contextPath}/teacher/student/changeWillStatus",
      type : "post",
      data : {
        decision : "accept",
        willId : willId
      },
      success : function(data, textStatus) {
        $.messager.show({
          title : '提示',
          msg : data.msg
        });
      }
    });
  }
  function reject_will(willId) {
    $.ajax({
      url : "${pageContext.request.contextPath}/teacher/student/changeWillStatus",
      type : "post",
      data : {
        decision : "reject",
        willId : willId
      },
      success : function(data, textStatus) {
        $.messager.show({
          title : '提示',
          msg : data.msg
        });
      }
    });
  }
  $(function() {
    $('#candidate_students_datagrid').datagrid(
        {
          url : '${pageContext.request.contextPath}/teacher/student/getCandidateStudents',
          queryParams : {
            "teacherId" : "${sessionScope.USER.id}"
          },
          type : 'post',
          fitColumns : true,
          border : false,
          nowrap : false,
          pagination : true,
          pageSize : 10,
          singleSelect : true,
          frozenColumns : [ [ {
            field : 'willId',
            title : 'Id',
            width : 10,
            hidden : true
          }, {
            field : 'studentId',
            title : 'StudentId',
            width : 10,
            hidden : true
          } ] ],
          columns : [ [
              {
                field : 'account',
                title : '学号',
                width : 100,
              },
              {
                field : 'name',
                title : '姓名',
                width : 100,
                formatter : function(value, rowData, index) {
                  var reload = '<a href="javascript:void(0)" class="easyui-linkbutton" id="btnChange"'
                      + 'data-options="plain:true" onclick="load_one_student_detail(' + rowData.id + ')">' + value
                      + '</a>';
                  return reload;
                }
              },
              {
                field : 'email',
                title : '邮箱',
                width : 100,
              },
              {
                field : 'phone',
                title : '电话',
                width : 100,
              },
              {
                field : 'status',
                title : '是否接受',
                width : 100,
              },
              {
                field : 'opt',
                title : '操作',
                width : 100,
                formatter : function(value, rowData, index) {
                  var accept = '<a href="javascript:void(0)" class="easyui-linkbutton"'
                      + 'data-options="plain:true" onclick="accept_will(' + rowData.willId + ')">接受</a>';
                  var reject = '<a href="javascript:void(0)" class="easyui-linkbutton"'
                      + 'data-options="plain:true" onclick="reject_will(' + rowData.willId + ')">拒绝</a>';
                  accept += " " + reject;
                  return accept;
                }
              }

          ] ],
          toolbar : [ '-', {
            text : '刷新',
            iconCls : 'icon-reload',
            handler : function() {
              $('#candidate_students_datagrid').datagrid('reload');
            }
          }, '-' ]
        });

    $('#candidate_students_datagrid').datagrid('getPager').pagination({
      pageSize : 10,
      pageList : [ 5, 10, 15 ],
      beforePageText : "第",
      afterPageText : "页,共{pages}页"
    });

  });
</script>
<body>
	<table id="candidate_students_datagrid"></table>
</body>

