<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/inc.jsp"></jsp:include>
<script>
  $(function() {
    var firstWill;
    var secondWill;
    var thirdWill;

    //Data initialization
    $.ajax({
      url : "${pageContext.request.contextPath}/student/getPreviousSelection",
      type : "get",
      async : false,
      success : function(data, textStatus) {
        for ( var key in data) {
          if (key == '1')
            firstWill = data[key];
          if (key == '2')
            secondWill = data[key];
          if (key == '3')
            thirdWill = data[key];
        }
      }
    });

    $("#updateWillButton").click(function() {
      $('#willForm').submit();
    });

    $("#willForm").form({
      url : '${pageContext.request.contextPath}/student/saveSelection',
      type : "post",
      onSubmit : function() {
        var first = $('#firstWill').combogrid('getValue');
        var second = $('#secondWill').combogrid('getValue');
        var third = $('#thirdWill').combogrid('getValue');
        if (first == second || second == third || first == third) {
          $.messager.show({
            title : '提示',
            msg : "不能填报相同的教师"
          })
          return false;
        }
        return true;
      },
      success : function(result) {
        var r = $.parseJSON(result);
        if (r.success) {
          $.messager.show({
            title : '成功',
            msg : r.msg
          })
        } else if (!r.success) {
          $.messager.show({
            title : '失败',
            msg : "发生意外错误"
          })
        }
      }
    });

    $('#firstWill').combogrid({
      panelWidth : 450,
      idField : 'account',
      textField : 'name',
      url : '${pageContext.request.contextPath}/student/getAllTeachers',
      columns : [ [ {
        field : 'account',
        title : '工号',
        width : 60
      }, {
        field : 'name',
        title : '姓名',
        width : 100
      }, ] ]
    });
    $('#secondWill').combogrid({
      panelWidth : 450,
      idField : 'account',
      textField : 'name',
      url : '${pageContext.request.contextPath}/student/getAllTeachers',
      columns : [ [ {
        field : 'account',
        title : '工号',
        width : 60
      }, {
        field : 'name',
        title : '姓名',
        width : 100
      }, ] ]
    });
    $('#thirdWill').combogrid({
      panelWidth : 450,
      idField : 'account',
      textField : 'name',
      url : '${pageContext.request.contextPath}/student/getAllTeachers',
      columns : [ [ {
        field : 'account',
        title : '工号',
        width : 60
      }, {
        field : 'name',
        title : '姓名',
        width : 100
      }, ] ]
    });
    $('#firstWill').combogrid('setValue', firstWill);
    $('#secondWill').combogrid('setValue', secondWill);
    $('#thirdWill').combogrid('setValue', thirdWill);
  });
</script>

</head>
<table width="98%" border="0" class="tableForm" cellpadding="2"
	cellspacing="1" bgcolor="#D1DDAA" align="center"
	style="margin-top: 8px">
	<tr bgcolor="#E7E7E7">
		<td height="24">${sessionScope.USER.name}的志愿填报</td>
	</tr>
</table>
<fieldset>
	<legend align="center">基本信息</legend>
	<table width="98%" border="0" cellpadding="2" class="tableForm"
		cellspacing="1" bgcolor="#D1DDAA" align="center"
		style="margin-top: 8px">
		<tr align="left" bgcolor="#FAFAF1">
			<th>学生</th>
			<td>${USER.name}</td>
			<th>学号</th>
			<td>${USER.account}</td>
		</tr>
	</table>
</fieldset>
<fieldset>
	<legend align="center">志愿填报</legend>
	<form method="post" id="willForm">
		<p align="center">
			<tr bgcolor="#FAFAF1">
				<th>第一志愿</th>
				<td><select id="firstWill" name="firstWill"
					style="width: 250px;"></select></td>
				<th>第二志愿</th>
				<td><select id="secondWill" name="secondWill"
					style="width: 250px;"></select></td>
				<th>第三志愿</th>
				<td><select id="thirdWill" name="thirdWill"
					style="width: 250px;"></select></td>
				<td><a href="javascript:void(0);" class="easyui-linkbutton"
					data-options="iconCls:'icon-search',plain:true"
					id="updateWillButton">更新</a></td>
			</tr>
		</p>
	</form>
</fieldset>
</body>
