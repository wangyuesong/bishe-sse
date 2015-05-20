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
        decision : "接受",
        willId : willId
      },
      success : function(data, textStatus) {
        $.messager.show({
          title : '提示',
          msg : data.msg
        });
        $('#candidate_students_datagrid').datagrid('reload');
      }
    });
  }
  function reject_will(willId) {
    $.ajax({
      url : "${pageContext.request.contextPath}/teacher/student/changeWillStatus",
      type : "post",
      data : {
        decision : "拒绝",
        willId : willId
      },
      success : function(data, textStatus) {
        $.messager.show({
          title : '提示',
          msg : data.msg
        });
        $('#candidate_students_datagrid').datagrid('reload');
      }
    });
  }

  function load_one_student_detail(studentId) {
    $.ajax({
      url : "${pageContext.request.contextPath}/teacher/student/showOneStudentDetail?studentId=" + studentId,
      type : "get",
      success : function(data, textStatus) {
        $("#name").text(data.name);
        $("#email").text(data.email);
        $("#gender").text(data.gender);
        $("#phone").text(data.phone);
        $("#self_description").text(data.selfDescription);
        $("#detail_legend").text(data.name + "详细信息");
      }
    });
  }
  $(function() {

    $("fieldset").css("border", "1px #99BBE8 dashed").css("padding", "20px").attr("align", "left");
    $("legend").css("color", "#0099FF").attr("align", "left");
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
                      + 'data-options="plain:true" onclick="load_one_student_detail(' + rowData.studentId + ')">'
                      + value + '</a>';
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
	<fieldset>
		<legend id="legend" align="left" style="color: #0099FF">以下为第一志愿选择您的学生</legend>
		备注:待定默认为拒绝
		<table id="candidate_students_datagrid"></table>
	</fieldset>
	<div style="margin-top: 20px"></div>
	<fieldset>
		<legend id="detail_legend" align="left" style="color: #0099FF">学生详细信息</legend>
		<div>
			<table width="98%" border="0" cellpadding="2" class="tableForm"
				cellspacing="1" bgcolor="#D1DDAA" align="center"
				style="margin-top: 8px">
				<tr bgcolor="#FAFAF1">
					<td width="15%">姓名:</td>
					<td id="name" width="35%"></td>
					<td width="15%">邮箱:</td>
					<td id="email" width="35%"></td>
				</tr>
				<tr bgcolor="#FAFAF1">
					<td width="15%">性别:</td>
					<td id="gender" width="35%"></td>
					<td width="15%">电话:</td>
					<td id="phone" width="35%"></td>
				</tr>

			</table>
			<table width="98%" border="0" cellpadding="2" class="tableForm"
				cellspacing="1" bgcolor="#D1DDAA" align="center"
				style="margin-top: 8px">
				<tr bgcolor="#FAFAF1">
					<td width="30%">自我介绍:</td>
					<td id="self_description" width="70%"></td>
				</tr>
			</table>
		</div>
	</fieldset>
</body>

