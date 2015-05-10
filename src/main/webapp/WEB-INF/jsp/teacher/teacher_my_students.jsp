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

            ] ],
            toolbar : [ '-', {
              text : '刷新',
              iconCls : 'icon-reload',
              handler : function() {
                $('#attachment_list_grid').datagrid('reload');
              }
            }, '-' ]
          });
      $('#student_list_grid').datagrid('getPager').pagination({
        pageSize : 10,
        pageList : [ 5, 10, 15 ],
        beforePageText : "第",
        afterPageText : "页,共{pages}页"
      });
    });
    function reload_one_student_detail(studentId) {
      load_student_document_detail("任务书", studentId);
      load_student_document_detail("开题报告", studentId);
      load_student_document_detail("最终论文", studentId);
    }
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
      var $feedback_header = $('#' + query_prefix + '_feedback_header');
      var $feedback_area = $('#' + query_prefix + '_feedback_area');
      var $feedback_template = $("#" + query_prefix + "_feedback_template");
      var $file_upload = $("#" + query_prefix + "_file_upload");
      var $attachment_list_grid = $('#' + query_prefix + '_attachment_list_grid');

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
            $container.html("");
            $container.prepend($create_template.html());
          } else {
            $document_description.text(data.content);
            $create_time.html(data.create_time);
            $update_time.html(data.update_time);
            $container.html("");
            $container.prepend($template.html());
          }
        }
      });

      //如果文档存在，尝试加载评论
      if (exists)
        $.ajax({
          url : "${pageContext.request.contextPath}/teacher/student/getDocumentComments",
          type : "post",
          data : {
            'studentId' : studentId,
            "type" : type
          },
          success : function(data, textStatus) {
            $.each(data, function(n, value) {
              $feedback_header.text((n + 1) + ". " + value.commentor + ":" + value.content + " " + value.createTime);
              $feedback_area.prepend($feedback_template.html());
            });
          }
        });

      $file_upload.uploadify({
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

      $attachment_list_grid
          .datagrid({
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
            } ] ],
            columns : [ [
                {
                  field : 'listName',
                  title : '名称',
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
                        + 'data-options="plain:true" onclick="delete_one_attachment(' + rowData.id + ')">删除</a>';
                    var download = '<a href="${pageContext.request.contextPath}/student/document/downloadAttachment?attachmentId='
                        + rowData.id + '">下载</a>';
                    remove += " " + download;
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
    }
  </script>



	<!-- 总页面模版 -->
	<div id="student_list_grid"></div>
	<div id="student_detail_grid" style="height: 800px">
		<div class="easyui-accordion" data-options="multiple:true"
			style="width: 100%; height: 80%;">
			<div id="renwushu_title" title="任务书" style="padding: 10px">
				<div id="renwushu_container"></div>
			</div>
			<div id="kaitibaogao_title" title="开题报告" style="padding: 10px">
				<div id="kaitibaogao_container"></div>
			</div>
			<div id="zuizhonglunwen_title" title="最终论文" style="padding: 10px">
				<div id="zuizhonglunwen_container"></div>
			</div>
		</div>
	</div>
	<!-- 总页面模版 -->

	<!-- 下面为ajax载入模版 -->
	<!-- 开题报告模版 -->
	<div id="kaitibaogao_template" style="display: none;">
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
				<div id="kaitibaogao_feedback_area"></div>
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
							<th><label for="Attachment_GUID">附件上传：</label></th>
							<td>
								<div>
									<input class="easyui-validatebox" type="hidden"
										id="Attachment_GUID" name="Attachment_GUID" /> <input
										id="kaitibaogao_file_upload" type="file" multiple="multiple">
									<a href="javascript:void(0)" class="easyui-linkbutton"
										id="btnUpload" data-options="plain:true,iconCls:'icon-save'"
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

	<div id="kaitibaogao_feedback_template" style="display: none">
		<div
			style="background: #E6E6E6; color: #FFF; border: 1px solid #F7F7F7;">
			<p style="display: inline;" id="kaitibaogao_feedback_header"></p>
		</div>
		<div id="kaitibaogao_feedback_content"></div>
	</div>
	<!-- 开题报告模版 -->


	<!-- 任务书模版 -->
	<div id="renwushu_template" style="display: none;">
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
				<div id="renwushu_feedback_area"></div>
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
							<th><label for="Attachment_GUID">附件上传：</label></th>
							<td>
								<div>
									<input class="easyui-validatebox" type="hidden"
										id="Attachment_GUID" name="Attachment_GUID" /> <input
										id="renwushu_file_upload" type="file" multiple="multiple">
									<a href="javascript:void(0)" class="easyui-linkbutton"
										id="btnUpload" data-options="plain:true,iconCls:'icon-save'"
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

	<div id="renwushu_feedback_template" style="display: none">
		<div
			style="background: #E6E6E6; color: #FFF; border: 1px solid #F7F7F7;">
			<p style="display: inline;" id="renwushu_feedback_header"></p>
		</div>
		<div id="renwushu_feedback_content"></div>
	</div>
	<!-- 任务书模版 -->



	<!-- 最终论文模版 -->
	<div id="zuizhonglunwen_template" style="display: none;">
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
				<div id="renwushu_feedback_area"></div>
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
							<th><label for="Attachment_GUID">附件上传：</label></th>
							<td>
								<div>
									<input class="easyui-validatebox" type="hidden"
										id="Attachment_GUID" name="Attachment_GUID" /> <input
										id="zuizhonglunwen_file_upload" type="file"
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
	</div>
	<div id="zuizhonglunwen_create_template" style="display: none;">
		<h3 style="color: #0099FF;">学生尚未创建任务书</h3>
	</div>

	<div id="zuizhonglunwen_feedback_template" style="display: none">
		<div
			style="background: #E6E6E6; color: #FFF; border: 1px solid #F7F7F7;">
			<p style="display: inline;" id="zuizhonglunwen_feedback_header"></p>
		</div>
		<div id="zuizhonglunwen_feedback_content"></div>
	</div>
	<!-- 最终论文模版 -->
</body>

