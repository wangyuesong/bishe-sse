<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
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
</head>
<body>
	<script>
    var exists = false;
    $(function() {
      var exits = false;
      //查看该用户是否已经创建该文档，如果没有则加载引导文档创建的界面，如果有的话则将已有文档载入
      $.ajax({
        url : "${pageContext.request.contextPath}/student/document/checkIfHasSuchDocument",
        type : "post",
        async : false,
        data : {
          "type" : "开题报告"
        },
        success : function(data, textStatus) {
          if (!(data == ""))
            exists = true;
          if (!exists)
            $('#kaitibaogao-container').prepend($('#create-kaitibaogao-template').html());
          else {
            $('#document_description').text(data.content);
            $('#create_time').html(data.create_time);
            $('#update_time').html(data.update_time);
            $('#kaitibaogao-container').prepend($('#kaitibaogao-template').html());
          }
        }
      });
      //如果文档存在，尝试加载评论
      if (exists)
        $.ajax({
          url : "${pageContext.request.contextPath}/student/document/getDocumentComments",
          type : "post",
          data : {
            "type" : "开题报告"
          },
          success : function(data, textStatus) {
            $.each(data, function(n, value) {
              $('#feedback-header').text(
                  (n + 1) + ". " + value.commentor + ":" + value.content + " " + value.createTime);
              $('#feedback-area').prepend($("#feedback-template").html());
              alert(n);
            });
          }
        });

      disable_edit_document_description();

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
                $('#attachment_list_grid').datagrid('reload');
              }
            }, '-' ]
          });
    })

    function enable_edit_document_description() {
      $("#document_description").attr("disabled", false);
    }
    function disable_edit_document_description() {
      $("#document_description").attr("disabled", true);
    }

    function submit_document_description() {
      $.ajax({
        url : "${pageContext.request.contextPath}/student/document/updateDocumentDescription",
        type : "post",
        data : {
          'document_type' : '开题报告',
          'document_description' : $("#document_description").val()
        },
        success : function(data, textStatus) {
          $.messager.show({
            title : '成功',
            msg : data.msg
          });
          $("#document_description").attr("disabled", true);
        }
      });
    }

    function add_document() {
      $('<div class="temp_dialog"></div>').dialog({
        href : '${pageContext.request.contextPath}/dispatch/student/student_add_document',
        onClose : function() {
          $.ajax({
            url : "${pageContext.request.contextPath}/student/document/cancelCreateDocument",
            type : "post",
            success : function(data, textStatus) {
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
          text : '创建',
          iconCls : 'icon-add',
          handler : function() {
            var d = $(this).closest('.window-body');
            $('#document_add_form').submit();
            $(".temp_dialog").datagrid('destroy');

          }
        } ]
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
  </script>
	<div style="height: 800px">
		<div class="easyui-accordion" style="width: 100%; height: 80%;">
			<div title="开题报告" style="padding: 10px">
				<div id="kaitibaogao-container"></div>
			</div>
		</div>
	</div>
	<div id="kaitibaogao-template" style="display: none;">
		<div class="section group">
			<div class="col span_6_of_6" bgcolor="#D1DDAA">${sessionScope.USER.name}的开题报告</div>
		</div>
		<div class="section group">
			<div class="col span_1_of_6">创建时间:</div>
			<div class="col span_2_of_6" id="create_time"></div>
			<div class="col span_1_of_6">修改时间:</div>
			<div class="col span_2_of_6" id="update_time"></div>
		</div>
		<div class="section group">
			<div class="col span_1_of_6">
				<p>描述:</p>
				<a href="javascript:void(0);"
					onclick='enable_edit_document_description()'>编辑</a> <a
					href="javascript:void(0);" onclick='submit_document_description()'>保存</a>
			</div>
			<div class="col span_4_of_6">
				<textarea id="document_description" class="easyui-validatebox"
					style="width: 500px; height: 300px" name="document_description"></textarea>
			</div>
		</div>
		<div class="section group">
			<div class="col span_1_of_6">反馈:</div>
			<div class="col span_5_of_6">
				<div id="feedback-area"></div>
			</div>
		</div>
		<div class="section group">
			<div class="col span_1_of_6">附件:</div>
			<div class="col span_5_of_6">
				<div class="col span_3_of_5">
					<fieldset>
						<legend align="left">已上传附件</legend>
						<div>
							<table id="attachment_list_grid"></table>
						</div>
					</fieldset>
				</div>
				<div class="col span_1_of_5">
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
			</div>
		</div>
	</div>
	<div id="create-kaitibaogao-template" style="display: none;">
		<h3 style="color: #0099FF;">您尚未创建您的开题报告</h3>
		<p>
			点击<a href="javascript:void(0);" onclick='add_document()'>这里</a>来创建您的开题报告
		</p>
	</div>

	<div id="feedback-template" style="display: none">
		<div
			style="background: #E6E6E6; color: #FFF; border: 1px solid #F7F7F7;">
			<p style="display: inline;" id="feedback-header"></p>
		</div>
		<div id="feedback-content"></div>
	</div>
</body>

