<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="/inc.jsp"></jsp:include>
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/col.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/2cols.css"
	rel="stylesheet">

<script>
  $(function() {
    var firstWill;
    var secondWill;
    var thirdWill;

    //Data initialization
    $.ajax({
      url : "${pageContext.request.contextPath}/student/will/getPreviousSelection",
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
      url : '${pageContext.request.contextPath}/student/will/saveSelection',
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
      idField : 'id',
      textField : 'name',
      onChange : function(newValue, oldValue) {
        $.ajax({
          url : "${pageContext.request.contextPath}/student/will/showOneTeacherDetail?teacherId=" + newValue,
          type : "get",
          success : function(data, textStatus) {
            $.each(data, function(key, value) {
              $('#' + key + "1").html(value);
            });
          }
        });
      },
      url : '${pageContext.request.contextPath}/student/will/getAllTeachersForSelect',
      columns : [ [ {
        field : 'id',
        title : 'id',
        hidden : true,
        width : 60
      }, {
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
      idField : 'id',
      textField : 'name',
      url : '${pageContext.request.contextPath}/student/will/getAllTeachersForSelect',
      onChange : function(newValue, oldValue) {
        $.ajax({
          url : "${pageContext.request.contextPath}/student/will/showOneTeacherDetail?teacherId=" + newValue,
          type : "get",
          success : function(data, textStatus) {
            $.each(data, function(key, value) {
              $('#' + key + "2").html(value);
            });
          }
        });
      },
      columns : [ [ {
        field : 'id',
        title : 'id',
        hidden : true,
        width : 60
      }, {
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
      idField : 'id',
      textField : 'name',
      onChange : function(newValue, oldValue) {
        $.ajax({
          url : "${pageContext.request.contextPath}/student/will/showOneTeacherDetail?teacherId=" + newValue,
          type : "get",
          success : function(data, textStatus) {
            $.each(data, function(key, value) {
              $('#' + key + "3").html(value);
            });
          }
        });
      },
      url : '${pageContext.request.contextPath}/student/will/getAllTeachersForSelect',
      columns : [ [ {
        field : 'id',
        title : 'id',
        hidden : true,
        width : 60
      }, {
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
		<!-- <div class="container"> -->
		<!-- <div class="grid-4">第一志愿</div>
			<div class="grid-8"> -->
		<div class="section group">
			<div class="col span_1_of_2">
				第一志愿 <select id="firstWill" name="firstWill" style="width: 250px;"></select>
				<p style="font-size: 2px">备注：第一志愿较重要，如果您已经和教师取得联系，请将教师填入第一志愿，教师将有权力直接将您录取</p>
			</div>
			<div class="col span_1_of_2" id="firstWillDetail">
				<table width="98%" border="0" cellpadding="2" class="tableForm"
					cellspacing="1" bgcolor="#D1DDAA" align="center"
					style="margin-top: 8px">
					<tr bgcolor="#FAFAF1">
						<td width="15%">姓名:</td>
						<td id="name1" width="35%"></td>
						<td width="15%">职称:</td>
						<td id="title1" width="35%"></td>
					</tr>
					<tr bgcolor="#FAFAF1">
						<td width="15%">邮箱:</td>
						<td id="email1" width="35%"></td>
						<td width="15%">电话:</td>
						<td id="phone1" width="35%"></td>
					</tr>
					<tr bgcolor="#FAFAF1">
						<td width="15%">学历:</td>
						<td id="degree1" width="35%"></td>
						<td width="15%">计划指导人数:</td>
						<td id="capacity1" width="35%"></td>
					</tr>
					<tr bgcolor="#FAFAF1">
						<td width="15%">方向:</td>
						<td id="direction1" width="35%"></td>
						<td width="15%">备选题目:</td>
						<td id="candidateTopics1" width="35%"></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="section group">
			<div class="col span_1_of_2">
				第二志愿 <select id="secondWill" name="secondWill" style="width: 250px;"></select>
			</div>
			<div class="col span_1_of_2" id="secondWillDetail">
				<table width="98%" border="0" cellpadding="2" class="tableForm"
					cellspacing="1" bgcolor="#D1DDAA" align="center"
					style="margin-top: 8px">
					<tr bgcolor="#FAFAF1">
						<td width="15%">姓名:</td>
						<td id="name2" width="35%"></td>
						<td width="15%">职称:</td>
						<td id="title2" width="35%"></td>
					</tr>
					<tr bgcolor="#FAFAF1">
						<td width="15%">邮箱:</td>
						<td id="email2" width="35%"></td>
						<td width="15%">电话:</td>
						<td id="phone2" width="35%"></td>
					</tr>
					<tr bgcolor="#FAFAF1">
						<td width="15%">学历:</td>
						<td id="degree2" width="35%"></td>
						<td width="15%">计划指导人数:</td>
						<td id="capacity2" width="35%"></td>
					</tr>
					<tr bgcolor="#FAFAF1">
						<td width="15%">方向:</td>
						<td id="direction2" width="35%"></td>
						<td width="15%">备选题目:</td>
						<td id="candidateTopics2" width="35%"></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="section group">
			<div class="col span_1_of_2">
				第三志愿 <select id="thirdWill" name="thirdWill" style="width: 250px;"></select>
			</div>
			<div class="col span_1_of_2" id="thirdWillDetail">
				<table width="98%" border="0" cellpadding="2" class="tableForm"
					cellspacing="1" bgcolor="#D1DDAA" align="center"
					style="margin-top: 8px">
					<tr bgcolor="#FAFAF1">
						<td width="15%">姓名:</td>
						<td id="name3" width="35%"></td>
						<td width="15%">职称:</td>
						<td id="title3" width="35%"></td>
					</tr>
					<tr bgcolor="#FAFAF1">
						<td width="15%">邮箱:</td>
						<td id="email3" width="35%"></td>
						<td width="15%">电话:</td>
						<td id="phone3" width="35%"></td>
					</tr>
					<tr bgcolor="#FAFAF1">
						<td width="15%">学历:</td>
						<td id="degree3" width="35%"></td>
						<td width="15%">计划指导人数:</td>
						<td id="capacity3" width="35%"></td>
					</tr>
					<tr bgcolor="#FAFAF1">
						<td width="15%">方向:</td>
						<td id="direction3" width="35%"></td>
						<td width="15%">备选题目:</td>
						<td id="candidateTopics3" width="35%"></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="section group">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				data-options="iconCls:'icon-search',plain:true"
				id="updateWillButton">更新</a>
		</div>
		<!-- </div> -->
		<!-- <th>第二志愿</th>
			<td><select id="secondWill" name="secondWill"
				style="width: 250px;"></select></td>
			<th>第三志愿</th>
			<td><select id="thirdWill" name="thirdWill"
				style="width: 250px;"></select></td>
			<td><a href="javascript:void(0);" class="easyui-linkbutton"
				data-options="iconCls:'icon-search',plain:true"
				id="updateWillButton">更新</a></td> -->
		<!-- </div> -->
	</form>
</fieldset>

<script>
  $("fieldset").css("border", "1px #99BBE8 dashed").css("padding", "20px").attr("align", "left").css("margin-top",
      "20px");
  $("legend").css("color", "#0099FF").attr("align", "left");
</script>
</body>
