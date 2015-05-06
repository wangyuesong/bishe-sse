<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
  var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/resources/ckeditor/';
</script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jquery.uploadify.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/ckeditor/ckeditor.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/ckeditor/config.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/ckeditor/lang/zh-cn.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/ckeditor/styles.js"></script>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/ckeditor/skins/moono/editor.css" />
<script type="text/javascript">
  $(function() {
    //初始化Attachment的列表
    $('#attachment_list_grid').datagrid(
        {
          url : '${pageContext.request.contextPath}/student/document/getAllTempAttachments',
          type : 'get',
          fitColumns : true,
          border : false,
          nowrap : false,
          /* pagination : true, */
          pageSize : 10,
          frozenColumns : [ [ {
            field : 'id',
            title : 'Id',
            width : 10,
            hidden : true
          }, {
            field : 'listName',
            title : '名称',
            width : 100,
          } ] ],
          columns : [ [
              {
                field : 'uploadTime',
                title : '上传时间',
                width : 100,
              },
              {
                field : 'opt',
                title : '操作',
                width : 100,
                formatter : function(value, rowData, index) {
                  var remove = '<a href="javascript:void(0)" class="easyui-linkbutton" id="btnCancelUpload"'
                      + 'data-options="plain:true" onclick="delete_one_attachment(' + rowData.id + ')">删除</a>';
                  return remove;
                }

              }

          ] ],
          toolbar : [ '-', {
            text : '刷新',
            iconCls : 'icon-reload',
            handler : function() {
              $attachment_list_grid.datagrid('reload');
            }
          }, '-' ]
        });

    //确认创建这个Document后，数据库增加Document记录，Attachment变为永久
    $("#document_add_form").form({
      url : '${pageContext.request.contextPath}/student/document/confirmCreateDocument',
      type : "post",
      success : function(result) {
        var r = $.parseJSON(result);
        //关闭模态框
        $('.temp_dialog').dialog('close');
        //重新载入List
        $('#project_list_datagrid').datagrid('reload');
        $.messager.show({
          title : "成功",
          msg : "成功"
        });

      }
    });
  })

  function delete_one_attachment(id) {
    $.ajax({
      url : "${pageContext.request.contextPath}/student/document/deleteOneTempAttachmentByAttachmentId?attachmentId="
          + id,
      type : "get",
      success : function(data, textStatus) {
        $("#attachment_list_grid").datagrid('reload');
      },
      error : function() {
        $.messager.alert("错误", "删除失败，请联系管理员");
      }
    });
  }

  function show_up_files() {
    $.ajax({
      url : "${pageContext.request.contextPath}/student/document/getAllTempAttachments",
      type : "get",
      success : function(data, textStatus) {
        $("#attachment_list_grid").datagrid('reload');
      }
    });
  };

  $("#file_upload").uploadify({
    'swf' : '${pageContext.request.contextPath}/resources/uploadify.swf',
    'buttonText' : '浏览',
    'uploader' : '${pageContext.request.contextPath}/student/document/uploadAttachements',
    'removeCompleted' : true,
    'fileSizeLimit' : '3MB',
    'fileTypeExts' : '*.doc; *.pdf; *.docx;',
    'queueID' : 'fileQueue',
    'auto' : false,
    'multi' : true,
    'simUploadLimit' : 2,
    'onQueueComplete' : function(event, data) {
      show_up_files();
    },
    'onFallback' : function() {
      $.messager.alert("提示", "检测到您的浏览器不支持Flash，请安装Flash插件");
    },
    'onUploadError' : function(file, errorCode, errorMsg, errorString) {
      $.message.alert("错误", '上传出错，请联系管理员');
    }

  });
</script>
<div align="center">
	<form id="document_add_form" method="post">
		<table width="98%" border="0" class="tableForm" cellpadding="2"
			cellspacing="1" bgcolor="#D1DDAA" align="center"
			style="margin-top: 8px">
			<tr bgcolor="#E7E7E7">
				<td height="24" style="align: center">${sessionScope.USER.name}的开题报告</td>
			</tr>
		</table>
		<table width="98%" border="0" cellpadding="2" class="tableForm"
			cellspacing="1" bgcolor="#D1DDAA" align="center"
			style="margin-top: 8px">
			<tr align="left" bgcolor="#FAFAF1">
				<td width="15%">描述:</td>
				<td width="80%" colspan="3"><textarea rows="3"
						style="width: 600px; height: 300px" name="document_description"></textarea></td>
			</tr>
			<tr>
				<td><input type="hidden" name="document_type" value="开题报告"></input></td>
				<td><input type="hidden" name="document_name"
					value="${sessionScope.USER.name}的开题报告"></input></td>
			</tr>
		</table>
	</form>
	<fieldset>
		<legend align="left">相关附件</legend>
		<div>
			<div style="float: left; width: 50%">
				<fieldset>
					<legend align="left">已上传附件</legend>
					<div data-options="region:'center',border:false">
						<table id="attachment_list_grid"></table>
					</div>
				</fieldset>
			</div>
			<div style="margin-left: 50%; width: 50%">
				<table>
					<tr>
						<th><label for="Attachment_GUID">附件上传：</label></th>
						<td>
							<div>
								<input class="easyui-validatebox" type="hidden"
									id="Attachment_GUID" name="Attachment_GUID" /> <input
									id="file_upload" type="file" multiple="multiple"> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									id="btnUpload" data-options="plain:true,iconCls:'icon-save'"
									onclick="javascript: $('#file_upload').uploadify('upload', '*')">上传</a>

								<a href="javascript:void(0)" class="easyui-linkbutton"
									id="btnCancelUpload"
									data-options="plain:true,iconCls:'icon-cancel'"
									onclick="javascript: $('#file_upload').uploadify('cancel', '*')">取消</a>
								<div id="fileQueue" class="fileQueue"></div>
								<div id="div_files"></div>
								<br />
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div style="clear: both"></div>
		</div>
	</fieldset>

</div>
</body>

