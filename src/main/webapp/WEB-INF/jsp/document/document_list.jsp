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
    var project_list_datagrid;
    $(function() {
      project_list_datagrid = $('#project_list_datagrid')
          .datagrid(
              {
                url : '${pageContext.request.contextPath}/document/getAllDocuments',
                type : 'get',
                fit : true,
                fitColumns : true,
                border : false,
                pagination : true,
                idField : 'sprojcode',
                pageSize : 20,
                pageList : [ 10, 20, 30, 40, 50 ],
                sortName : 'sprojcode',
                sortOrder : 'asc',
                checkOnSelect : false,
                selectOnCheck : false,
                nowrap : false,
                frozenColumns : [ [ {
                  field : 'name',
                  title : '文档名称',
                  width : 200
                }, {
                  title : '文档类别',
                  field : 'documentType',
                  width : 100,
                }, {
                  field : 'creator',
                  title : '创建者',
                  width : 120
                }, {
                  field : 'lastModifiedBy',
                  title : '最后修改人',
                  width : 120
                } ] ],
                columns : [ [ {
                  field : 'action',
                  title : '动作',
                  width : 50,
                  formatter : function(value, row, index) {
                    return formatString(
                        '<img onclick="project_list_editFun(\'{0}\');" src="{1}"/>&nbsp;<img onclick="project_list_deleteFun(\'{2}\');" src="{3}"/>',
                        row.sprojcode, '${pageContext.request.contextPath}/js/icons/pencil.png', row.sprojcode,
                        '${pageContext.request.contextPath}/js/icons/cancel.png');
                  }
                } ] ],
                toolbar : [ {
                  text : '增加',
                  iconCls : 'icon-add',
                  handler : function() {
                    project_list_addFun();
                  }
                }, '-', {
                  text : '批量删除',
                  iconCls : 'icon-cancel',
                  handler : function() {
                    project_list_removeFun();
                  }
                }, '-', {
                  text : '刷新',
                  iconCls : 'icon-reload',
                  handler : function() {
                    project_list_datagrid.datagrid('reload');
                  }
                }, '-' ]
              });
    });

    function project_list_addFun() {
      project_list_datagrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
      $('<div/>').dialog({
        href : '${pageContext.request.contextPath}/dispatch/document/add_document',
        onClose: function()
        {
          
        },
        width : 900,
        height : 500,
        collapsible : true,
        modal : true,
        title : '添加新文档',
        buttons : [ {
          text : '增加',
          iconCls : 'icon-add',
          handler : function() {
            var d = $(this).closest('.window-body');
            $('#project_add_addForm').form('submit', {
              url : '${pageContext.request.contextPath}/addprojectAction.html',
              success : function(result) {
                try {
                  var r = $.parseJSON(result);
                  if (r.success) {
                    project_list_datagrid.datagrid('insertRow', {
                      index : 0,
                      row : r.obj
                    });
                    d.dialog('destroy');
                    //使用ajax在添加过项目后自动解析数据库表
                    $.ajax({
                      url : '${pageContext.request.contextPath}/structsDbAction.html',
                      data : 'project.sprojcode=' + r.obj.sprojcode,
                      timeout : 1000 * 60 * 20, //超时时间20分钟
                      success : function(result) {
                        var rs = $.parseJSON(result);
                        if (rs.success) {
                          $.messager.show({
                            title : '提示',
                            msg : rs.msg
                          });
                        } else {
                          $.messager.show({
                            title : '错误',
                            msg : rs.msg
                          });
                        }
                      },
                      error : function(result) {
                        var rs = $.parseJSON(result);
                        $.messager.alter('错误', rs);
                      }
                    });
                  }
                  $.messager.show({
                    title : '提示',
                    msg : r.msg
                  });
                } catch (e) {
                  $.messager.alert('提示', result);
                }
              }
            });
          }
        } ],
        onClose : function() {
          $(this).dialog('destroy');
        }
      });
    }
  </script>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'north',title:'查询条件',border:false"
			style="height: 185px; overflow: hidden;" align="center">
			<form id="project_list_searchForm">
				<table class="tableForm">
					<tr>
						<th style="width: 170px;">项目英文名称</th>
						<td><input name="sprojname" /></td>
						<th style="width: 170px;">项目中文名称</th>
						<td><input name="sprojchename" /></td>
					</tr>
					<tr>
						<th>创建人</th>
						<td><input name="susercode" /></td>
						<th>数据库类型</th>
						<td><input name="sdbtype" /></td>
					</tr>
					<tr>
						<th>添加日期</th>
						<td colspan="2"><input name="startDate"
							class="easyui-datebox" data-options="editable:false" />至<input
							name="endDate" class="easyui-datebox"
							data-options="editable:false" /></td>
					</tr>
				</table>
				<a href="javascript:void(0);" class="easyui-linkbutton"
					data-options="iconCls:'icon-search',plain:true"
					onclick="project_list_searchFun();">查询</a> <a
					href="javascript:void(0);" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel',plain:true"
					onclick="project_list_cleanFun();">清空查询条件</a>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="project_list_datagrid"></table>
		</div>
	</div>
</body>