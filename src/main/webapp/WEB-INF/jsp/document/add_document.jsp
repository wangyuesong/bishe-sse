<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jquery.uploadify.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/style/bootstrap/bootstrap.min.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/style/bootstrap/bootstrap-theme.min.css" />
<script type="text/javascript">
  $("#file_upload").uploadify({
    'swf' : '${pageContext.request.contextPath}/resources/uploadify.swf',
    'buttonText' : '浏览',
    'uploader' : '${pageContext.request.contextPath}/document/uploadAttachements',
    'removeCompleted' : true,
    'fileSizeLimit' : '3MB',
    /* 'fileTypeExts' : '*.doc; *.pdf; *.docx;', */
    'queueID' : 'fileQueue',
    'auto' : false,
    'multi' : true,
    'simUploadLimit' : 2,
  });
</script>
<div align="center">
	<form id="project_add_addForm" method="post">
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
					<td width="35%"><input type="text" name="sprojname"
						style="width: 150px;" class="easyui-validatebox"
						data-options="required:true" />&nbsp;</td>
					<td width="15%">文档类型:</td>
					<td width="35%"><input type="text" name="sprojchename"
						style="width: 150px;" class="easyui-validatebox"
						data-options="required:true" />&nbsp;</td>
				</tr>
				<tr align="left" bgcolor="#FAFAF1">
					<td width="15%">描述:</td>
					<td width="80%" colspan="3"><textarea rows="3"
							style="width: 400px;" name="sdesc"></textarea>&nbsp;</td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend align="left">相关文档</legend>
			</table>
			<div>
				<div style="float: left; width: 70%"></div>
				<div style="margin-left: 70%; width: 30%">
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
	</form>
</div>
</body>

