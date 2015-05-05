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
</head>
<body>
	<script>
    var exists = false;
    $(function() {
      $.ajax({
        url : "${pageContext.request.contextPath}/student/document/checkIfHasSuchDocument",
        type : "post",
        data : {
          "type" : "开题报告"
        },
        success : function(data, textStatus) {
          exists = data;
          if (!exists)

        }
      });
    })

    function append_container() {
      $('#kaitibaogao-container').prepend($('#kaitibaogao-template').html());
    }
    function project_list_addFun() {
      project_list_datagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
      $('<div class="temp_dialog"></div>').dialog({
        href : '${pageContext.request.contextPath}/dispatch/student/student_add_document',
        onClose : function() {
          $.ajax({
            url : "${pageContext.request.contextPath}/student/document/cancelCreateDocument",
            type : "post",
            success : function(data, textStatus) {
              $.each(data, function(n, value) {
                alert(value.listName);
              });
            }
          });
          $(this).dialog('destroy');
        },
        width : $(document.body).width() * 0.9,
        height : $(document.body).height() * 0.9,
        collapsible : true,
        modal : true,
        title : '添加新文档',
        buttons : [ {
          text : '增加',
          iconCls : 'icon-add',
          handler : function() {
            var d = $(this).closest('.window-body');
            $('#document_add_form').submit();
            $(".temp_dialog").datagrid('destroy');
          }
        } ]
      });
    }
  </script>
	<div style="height: 700px">
		<div class="easyui-accordion" style="width: 100%; height: 80%;">
			<div title="开题报告" style="padding: 10px">
				<div id="kaitibaogao-container"></div>
				<button onclick="append_container()">测试</button>
			</div>
		</div>
	</div>

	<div id="kaitibaogao-template" style="display: none;">
		<table width="98%" border="0" class="tableForm" cellpadding="2"
			cellspacing="1" bgcolor="#D1DDAA" align="center"
			style="margin-top: 8px">
			<tr bgcolor="#E7E7E7">
				<td height="24">新增项目</td>
			</tr>
		</table>
		<fieldset>
			<legend align="left">添加文档</legend>
			<table width="98%" border="0" cellpadding="2" class="tableForm"
				cellspacing="1" bgcolor="#D1DDAA" align="center"
				style="margin-top: 8px">
				<tr align="left" bgcolor="#FAFAF1">
					<td width="15%">文档名称:</td>
					<td width="35%"><input type="text" name="document_name"
						style="width: 150px;" class="easyui-validatebox"
						data-options="required:true" />&nbsp;</td>
					<td width="15%">文档类型:</td>
					<td width="35%"><select id="document_type_select"
						name="document_type" style="width: 250px;"></select></td>
				</tr>
				<tr align="left" bgcolor="#FAFAF1">
					<td width="15%">描述:</td>
					<td width="80%" colspan="3"><textarea rows="3"
							style="width: 600px; height: 300px" name="document_description"></textarea></td>
				</tr>
			</table>
		</fieldset>
	</div>

	<div id="create-kaitibaogao-template" style="display: none;">
	 <h3 style="color:#0099FF;">您尚未创建您的开题报告</h3>
	 <p>点击这里来创建您的开题报告</p>
	</div>
</body>

