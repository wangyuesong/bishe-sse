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

<fieldset>
	<legend>公告列表</legend>
	<table id="messages_datagrid">
	</table>
</fieldset>

<script type="text/javascript">
  $("fieldset").css("border", "1px #99BBE8 dashed").css("padding", "20px").attr("align", "left");
  $("legend").css("color", "#0099FF").attr("align", "left");

  $('#messages_datagrid').datagrid(
      {
        url : '${pageContext.request.contextPath}/admin/timenodemessage/getSystemMessagesInDatagrid',
        fitColumns : true,
        border : false,
        nowrap : false,
        pagination : true,
        pageSize : 10,
        singleSelect : true,
        frozenColumns : [ [ {
          field : 'id',
          title : 'Id',
          width : 10,
          hidden : true
        } ] ],
        columns : [ [
            {
              field : 'title',
              title : '标题',
              width : 300,
              formatter : function(value, rowData, index) {
                var reload = '<a href="javascript:void(0)" class="easyui-linkbutton" id="btnChange"'
                    + 'data-options="plain:true" onclick="reload_one_student_detail(' + rowData.id + ')">' + value
                    + '</a>';
                return reload;
              }
            }, {
              field : 'createTime',
              title : '日期',
              width : 100
            } ] ],
        toolbar : [ '-', {
          text : '刷新',
          iconCls : 'icon-reload',
          handler : function() {
            $('#messages_datagrid').datagrid('reload');
          }
        }, '-', '-', {
          text : '发布',
          iconCls : 'icon-add',
          handler : function() {
            add_message();
          }
        }, '-' ]
      });

  $('#messages_datagrid').datagrid('getPager').pagination({
    pageSize : 10,
    pageList : [ 5, 10, 15 ],
    beforePageText : "第",
    afterPageText : "页,共{pages}页"
  });

  //Modal dialog 创建回复
  function add_message() {
    $('<div class="temp_dialog"></div>').dialog({
      href : '${pageContext.request.contextPath}/dispatch/administrator/admin_add_message',
      onClose : function() {
        $(this).dialog('destroy');
      },
      width : $(document.body).width() * 0.5,
      height : $(document.body).height() > 400 ? $(document.body).height() : 400,
      collapsible : true,
      resizable : true,
      modal : true,
      title : '回复',
      buttons : [ {
        text : '回复',
        iconCls : 'icon-add',
        handler : function() {
          $.ajax({
            url : "${pageContext.request.contextPath}/admin/timenodemessage/createSystemMessage",
            type : "post",
            success : function(data, textStatus) {
              $(".temp_dialog").dialog('destroy');
              $('#feedback-datagrid').datagrid("reload");
              $.messager.show({
                title : '提示',
                msg : data.msg
              });
            }
          });
        }
      } ]
    });
  }
</script>
