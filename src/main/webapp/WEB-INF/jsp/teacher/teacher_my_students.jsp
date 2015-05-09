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
    $(function() {
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
      var p = $('#student_list_grid').datagrid('getPager');
      $(p).pagination({
        pageSize : 10,
        pageList : [ 5, 10, 15 ],
        beforePageText : "第",
        afterPageText : "页,共{pages}页"
      });

    });

    //载入具体学生信息
    function reload_one_student_detail(studentId) {
      //该type文档是否存在
      var exists = false;
      //查看该用户是否已经创建该文档，如果没有则加载引导文档创建的界面，如果有的话则将已有文档载入
      $.ajax({
        url : "${pageContext.request.contextPath}/teacher/student/getOneStudentDocument",
        type : "post",
        async : false,
        data : {
          'studentId' : studentId,
          "type" : "开题报告"
        },
        success : function(data, textStatus) {
          if (!(data == ""))
            exists = true;
          if (!exists) {
            $('#kaitibaogao-container').html("");
            $('#kaitibaogao-container').prepend($('#create-kaitibaogao-template').html());
          } else {
            $('#document_description').text(data.content);
            $('#create_time').html(data.create_time);
            $('#update_time').html(data.update_time);
            $('#kaitibaogao-container').html("");
            $('#kaitibaogao-container').prepend($('#kaitibaogao-template').html());
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
            "type" : "开题报告"
          },
          success : function(data, textStatus) {
            $.each(data, function(n, value) {
              $('#feedback-header').text(
                  (n + 1) + ". " + value.commentor + ":" + value.content + " " + value.createTime);
              $('#feedback-area').prepend($("#feedback-template").html());
            });
          }
        });

    }
  </script>
	<div id="student_list_grid"></div>
	<div id="student_detail_grid" style="height: 800px">
		<div class="easyui-accordion" data-options="multiple:true"
			style="width: 100%; height: 80%;">
			<div title="任务书" style="padding: 10px">
				<div id="renwushu-container"></div>
			</div>
			<div title="开题报告" style="padding: 10px">
				<div id="kaitibaogao-container"></div>
			</div>
			<div title="最终论文" style="padding: 10px">
				<div id="zuizhonglunwen-container"></div>
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
		<h3 style="color: #0099FF;">学生尚未创建开题报告</h3>
	</div>

	<div id="feedback-template" style="display: none">
		<div
			style="background: #E6E6E6; color: #FFF; border: 1px solid #F7F7F7;">
			<p style="display: inline;" id="feedback-header"></p>
		</div>
		<div id="feedback-content"></div>
	</div>
</body>

