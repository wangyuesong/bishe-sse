<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
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
    //通用刷新附件列表函数，根据prefix来确定更新哪个template中的附件列表
    function show_up_files(studentId, type, prefix) {
      $.ajax({
        url : "${pageContext.request.contextPath}/document/getAllForeverAttachmentsByUserIdAndDocumentType",
        data : {
          'type' : type,
          'userId' : studentId
        },
        type : "post",
        success : function(data, textStatus) {
          $("#" + prefix + "_attachment_list_grid").datagrid('reload');
        }
      });
    };
    //通用删除某附件函数，根据prefix来确定删除后更新哪个template中的附件列表
    function delete_one_attachment(prefix, id) {
      $.ajax({
        url : "${pageContext.request.contextPath}/document/deleteOneAttachmentByAttachmentId?attachmentId=" + id,
        type : "post",
        success : function(data, textStatus) {
          $.messager.show({
            title : '提示',
            msg : data.msg
          });
          $("#" + prefix + "_attachment_list_grid").datagrid('reload');
        },
        error : function() {
          $.messager.alert("错误", "删除失败，请联系管理员");
        }
      });
    };

    //Modal dialog 创建回复
    function add_comment(studentId, type, $feedback_datagrid) {
      $('<div class="temp_dialog"></div>').dialog({
        href : '${pageContext.request.contextPath}/dispatch/student/student_make_comment',
        onClose : function() {
          $(this).dialog('destroy');
        },
        width : $(document.body).width() * 0.5,
        height : $(document.body).height() * 0.5,
        collapsible : true,
        modal : true,
        title : '回复',
        buttons : [ {
          text : '回复',
          iconCls : 'icon-add',
          handler : function() {
            $.ajax({
              url : "${pageContext.request.contextPath}/document/makeComment",
              type : "post",
              data : {
                studentId : studentId,
                commentorId : '${sessionScope.USER.id}',
                type : type,
                content : $('#document_comment_content').val()
              },
              success : function(data, textStatus) {
                $(".temp_dialog").dialog('destroy');
                $feedback_datagrid.datagrid("reload");
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

    function reload_one_student_detail(studentId) {
      load_student_document_detail("任务书", studentId);
      load_student_document_detail("开题报告", studentId);
      load_student_document_detail("最终论文", studentId);
    }

    $(function() {
      //设置宽高
      $("#student_detail_grid").height($(document.body).height());

      //学生列表
      $('#student_list_grid').datagrid(
          {
            url : '${pageContext.request.contextPath}/teacher/student/getMyStudentsInDatagrid',
            type : 'get',
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
                        + 'data-options="plain:true" onclick="reload_one_student_detail(' + rowData.id + ')">' + value
                        + '</a>';
                    return reload;
                  }
                }, {
                  field : 'opt',
                  title : '邮箱',
                  width : 100,
                }, {
                  field : 'phone',
                  title : '电话',
                  width : 100,
                }

            ] ]
          });
      $('#student_list_grid').datagrid('getPager').pagination({
        pageSize : 10,
        pageList : [ 5, 10, 15 ],
        beforePageText : "第",
        afterPageText : "页,共{pages}页"
      });
    });
    //该方法用于加载学生的三个不同文档
    function load_student_document_detail(type, studentId) {
      var query_prefix = "";
      if (type == "开题报告")
        query_prefix = "kaitibaogao";
      else if (type == "任务书")
        query_prefix = "renwushu";
      else if (type == "最终论文")
        query_prefix = "zuizhonglunwen";
      var $renwushu_title = $('#renwushu_title');
      var $kaitibaogao_title = $('#kaitibaogao_title');
      var $zuizhonglunwen_title = $('#zuizhonglunwen_title');
      var $container = $('#' + query_prefix + '_container');
      var $create_template = $('#' + query_prefix + '_create_template');
      var $document_description = $('#' + query_prefix + '_document_description');
      var $create_time = $('#' + query_prefix + '_create_time');
      var $update_time = $('#' + query_prefix + '_update_time');
      var $template = $('#' + query_prefix + '_template');
      var $feedback_datagrid = $("#" + query_prefix + "_feedback_datagrid");
      var $file_upload = $("#" + query_prefix + "_file_upload");
      var $attachment_list_grid = $('#' + query_prefix + '_attachment_list_grid');

      //禁用描述框
      $document_description.attr("disabled", true);

      var exists = false;
      //查看该用户是否已经创建该文档，如果没有则加载引导文档创建的界面，如果有的话则将已有文档载入
      $.ajax({
        url : "${pageContext.request.contextPath}/teacher/student/getOneStudentDocument",
        type : "post",
        async : false,
        data : {
          'studentId' : studentId,
          "type" : type
        },
        success : function(data, textStatus) {
          if (!(data == ""))
            exists = true;
          if (!exists) {
            $container.css("display", "none");
            $create_template.css("display", "block");
          } else {
            $container.css("display", "block");
            $create_template.css("display", "none");
            $document_description.text(data.content);
            $create_time.html(data.create_time);
            $update_time.html(data.update_time);
          }
        }
      });

      //如果文档存在，尝试加载评论
      if (exists)
        $feedback_datagrid.datagrid({
          url : '${pageContext.request.contextPath}/document/getDocumentCommentsByStudentIdAndDocumentType',
          queryParams : {
            "type" : type,
            "studentId" : studentId
          },
          type : 'post',
          fitColumns : true,
          border : false,
          nowrap : false,
          singleSelect : true,
          frozenColumns : [ [ {
            field : 'id',
            title : 'Id',
            width : 10,
            hidden : true
          } ] ],
          columns : [ [ {
            field : 'content',
            title : '内容',
            width : 200,
          }, {
            field : 'createTime',
            title : '时间',
            width : 100,
          }, {
            field : 'commentor',
            title : '留言人',
            width : 100,
          } ] ],
          toolbar : [ '-', {
            text : '回复',
            iconCls : 'icon-reload',
            handler : function() {
              add_comment(studentId, type, $feedback_datagrid);
            }
          }, '-' ]
        });

      var form_data = {
        'creatorId' : '${sessionScope.USER.id}',
        'ownerId' : studentId,
        'documentType' : type
      };

      $file_upload.uploadify({
        'swf' : '${pageContext.request.contextPath}/resources/uploadify.swf',
        'buttonText' : '浏览',
        'uploader' : '${pageContext.request.contextPath}/document/uploadForeverAttachments',
        'formData' : form_data,
        'removeCompleted' : true,
        'fileSizeLimit' : '3MB',
        'fileTypeExts' : '*.doc; *.pdf; *.docx;',
        'queueID' : 'fileQueue',
        'auto' : false,
        'multi' : true,
        'simUploadLimit' : 2,
        'onQueueComplete' : function(event, data) {
          show_up_files(studentId, type, query_prefix);
        },
        'onFallback' : function() {
          $.messager.alert("提示", "检测到您的浏览器不支持Flash，请安装Flash插件");
        },
        'onUploadError' : function(file, errorCode, errorMsg, errorString) {
          $.message.alert("错误", '上传出错，请联系管理员');
        }

      });

      $attachment_list_grid
          .datagrid({
            url : '${pageContext.request.contextPath}/teacher/student/getAllForeverAttachments?studentId=' + studentId
                + '&type=' + query_prefix,
            type : 'post',
            fitColumns : true,
            border : false,
            nowrap : false,
            frozenColumns : [ [ {
              field : 'id',
              title : 'Id',
              width : 10,
              hidden : true
            } ] ],
            columns : [ [
                {
                  field : 'listName',
                  title : '名称',
                  width : 100,
                },
                {
                  field : 'creatorName',
                  title : '上传人',
                  width : 100,
                },
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
                    var remove = '<a href="javascript:void(0)" class="easyui-linkbutton" id="btnDelete"'
                        + 'data-options="plain:true" onclick="delete_one_attachment(\'' + query_prefix + '\','
                        + rowData.id + ')">删除</a>';
                    var download = '<a href="${pageContext.request.contextPath}/document/downloadAttachmentByAttachmentId?attachmentId='
                        + rowData.id + '">下载</a>';
                    remove += " " + download;
                    return remove;
                  }
                } ] ],
            toolbar : [ '-', {
              text : '刷新',
              iconCls : 'icon-reload',
              handler : function() {
                $attachment_list_grid.datagrid('reload');
              }
            }, '-' ]
          });
    }
  </script>



	<!-- 总页面模版 -->
	<!-- 学生列表 -->
	<fieldset style="color: #0099FF">
		<legend id="legend" align="left" style="color: #0099FF">学生列表</legend>
		<div id="student_list_grid"></div>
	</fieldset>
	<div style="margin-top: 20px; clear: both"></div>
	<div id="student_detail_grid" style="height: 800px">
		<!-- 学生信息模版 -->
		<div class="easyui-accordion" data-options="multiple:true"
			style="width: 100%; height: 80%;">
			<div id="renwushu_title" title="任务书" style="padding: 10px">
				<div id="renwushu_container" style="display: none">
					<div class="section group">
						<div class="col span_6_of_6" bgcolor="#D1DDAA">${sessionScope.USER.name}的开题报告</div>
					</div>
					<div class="section group">
						<div class="col span_1_of_6">创建时间:</div>
						<div class="col span_2_of_6" id="renwushu_create_time"></div>
						<div class="col span_1_of_6">修改时间:</div>
						<div class="col span_2_of_6" id="renwushu_update_time"></div>
					</div>
					<div class="section group">
						<div class="col span_1_of_6">
							<p>描述:</p>
						</div>
						<div class="col span_4_of_6">
							<textarea id="renwushu_document_description"
								class="easyui-validatebox" style="width: 500px; height: 300px"
								name="renwushu_document_description"></textarea>
						</div>
					</div>
					<div class="section group">
						<div class="col span_1_of_6">反馈:</div>
						<div class="col span_5_of_6">
							<table id="renwushu_feedback_datagrid"></table>
						</div>
					</div>
					<div class="section group">
						<div class="col span_1_of_6">附件:</div>
						<div class="col span_5_of_6">
							<div class="col span_3_of_5">
								<fieldset>
									<legend align="left">已上传附件</legend>
									<div>
										<table id="renwushu_attachment_list_grid"></table>
									</div>
								</fieldset>
							</div>
							<div class="col span_1_of_5">
								<table>
									<tr>
										<th><label for="renwushu_Attachment_GUID">附件上传：</label></th>
										<td>
											<div>
												<input class="easyui-validatebox" type="hidden"
													id="renwushu_Attachment_GUID" name="Attachment_GUID" /> <input
													id="renwushu_file_upload" type="file" multiple="multiple">
												<a href="javascript:void(0)" class="easyui-linkbutton"
													id="btnUpload"
													data-options="plain:true,iconCls:'icon-save'"
													onclick="javascript: $('#renwushu_file_upload').uploadify('upload', '*')">上传</a>

												<a href="javascript:void(0)" class="easyui-linkbutton"
													id="btnCancelUpload"
													data-options="plain:true,iconCls:'icon-cancel'"
													onclick="javascript: $('#renwushu_file_upload').uploadify('cancel', '*')">取消</a>
												<div id="renwushu_fileQueue" class="fileQueue"></div>
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
				<div id="renwushu_create_template" style="display: none;">
					<h3 style="color: #0099FF;">学生尚未创建任务书</h3>
				</div>
			</div>
			<div id="kaitibaogao_title" title="开题报告" style="padding: 10px">
				<div id="kaitibaogao_container" style="display: none">
					<div class="section group">
						<div class="col span_6_of_6" bgcolor="#D1DDAA">${sessionScope.USER.name}的开题报告</div>
					</div>
					<div class="section group">
						<div class="col span_1_of_6">创建时间:</div>
						<div class="col span_2_of_6" id="kaitibaogao_create_time"></div>
						<div class="col span_1_of_6">修改时间:</div>
						<div class="col span_2_of_6" id="kaitibaogao_update_time"></div>
					</div>
					<div class="section group">
						<div class="col span_1_of_6">
							<p>描述:</p>
						</div>
						<div class="col span_4_of_6">
							<textarea id="kaitibaogao_document_description"
								class="easyui-validatebox" style="width: 500px; height: 300px"
								name="kaitibaogao_document_description"></textarea>
						</div>
					</div>
					<div class="section group">
						<div class="col span_1_of_6">反馈:</div>
						<div class="col span_5_of_6">
							<table id="kaitibaogao_feedback_datagrid"></table>
						</div>
					</div>
					<div class="section group">
						<div class="col span_1_of_6">附件:</div>
						<div class="col span_5_of_6">
							<div class="col span_3_of_5">
								<fieldset>
									<legend align="left">已上传附件</legend>
									<div>
										<table id="kaitibaogao_attachment_list_grid"></table>
									</div>
								</fieldset>
							</div>
							<div class="col span_1_of_5">
								<table>
									<tr>
										<th><label for="kaitibaogao_Attachment_GUID">附件上传：</label></th>
										<td>
											<div>
												<input class="easyui-validatebox" type="hidden"
													id="kaitibaogao_Attachment_GUID" name="Attachment_GUID" />
												<input id="kaitibaogao_file_upload" type="file"
													multiple="multiple"> <a href="javascript:void(0)"
													class="easyui-linkbutton" id="btnUpload"
													data-options="plain:true,iconCls:'icon-save'"
													onclick="javascript: $('#kaitibaogao_file_upload').uploadify('upload', '*')">上传</a>

												<a href="javascript:void(0)" class="easyui-linkbutton"
													id="btnCancelUpload"
													data-options="plain:true,iconCls:'icon-cancel'"
													onclick="javascript: $('##kaitibaogao_file_upload').uploadify('cancel', '*')">取消</a>
												<div id="kaitibaogao_fileQueue" class="fileQueue"></div>
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
				<div id="kaitibaogao_create_template" style="display: none;">
					<h3 style="color: #0099FF;">学生尚未创建开题报告</h3>
				</div>
			</div>
			<div id="zuizhonglunwen_title" title="最终论文" style="padding: 10px">
				<div id="zuizhonglunwen_container" style="display: none">
					<div class="section group">
						<div class="col span_6_of_6" bgcolor="#D1DDAA">${sessionScope.USER.name}的开题报告</div>
					</div>
					<div class="section group">
						<div class="col span_1_of_6">创建时间:</div>
						<div class="col span_2_of_6" id="zuizhonglunwen_create_time"></div>
						<div class="col span_1_of_6">修改时间:</div>
						<div class="col span_2_of_6" id="zuizhonglunwen_update_time"></div>
					</div>
					<div class="section group">
						<div class="col span_1_of_6">
							<p>描述:</p>
						</div>
						<div class="col span_4_of_6">
							<textarea id="zuizhonglunwen_document_description"
								class="easyui-validatebox" style="width: 500px; height: 300px"
								name="zuizhonglunwen_document_description"></textarea>
						</div>
					</div>
					<div class="section group">
						<div class="col span_1_of_6">反馈:</div>
						<div class="col span_5_of_6">
							<table id="zuizhonglunwen_feedback_datagrid"></table>
						</div>
					</div>
					<div class="section group">
						<div class="col span_1_of_6">附件:</div>
						<div class="col span_5_of_6">
							<div class="col span_3_of_5">
								<fieldset>
									<legend align="left">已上传附件</legend>
									<div>
										<table id="zuizhonglunwen_attachment_list_grid"></table>
									</div>
								</fieldset>
							</div>
							<div class="col span_1_of_5">
								<table>
									<tr>
										<th><label for="zuizhonglunwen_Attachment_GUID">附件上传：</label></th>
										<td>
											<div>
												<input class="easyui-validatebox" type="hidden"
													id="zuizhonglunwen_Attachment_GUID" name="Attachment_GUID" />
												<input id="zuizhonglunwen_file_upload" type="file"
													multiple="multiple"> <a href="javascript:void(0)"
													class="easyui-linkbutton" id="btnUpload"
													data-options="plain:true,iconCls:'icon-save'"
													onclick="javascript: $('#zuizhonglunwen_file_upload').uploadify('upload', '*')">上传</a>

												<a href="javascript:void(0)" class="easyui-linkbutton"
													id="btnCancelUpload"
													data-options="plain:true,iconCls:'icon-cancel'"
													onclick="javascript: $('#zuizhonglunwen_file_upload').uploadify('cancel', '*')">取消</a>
												<div id="zuizhonglunwen_fileQueue" class="fileQueue"></div>
												<div id="div_files"></div>
												<br />
											</div>
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
					<div id="zuizhonglunwen_create_template" style="display: none;">
						<h3 style="color: #0099FF;">学生尚未创建论文</h3>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 总页面模版 -->
</body>

