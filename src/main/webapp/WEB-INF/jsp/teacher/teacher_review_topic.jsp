<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
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
<script>
  var selected_topic_id = undefined;
  //保存动作
  //展示一个Topic的Detail
  function one_topic_detail(topic_id) {
    selected_topic_id = topic_id;
    $temp_dialog = $('<div class="temp_dialog"></div>').dialog({
      href : '${pageContext.request.contextPath}/dispatch/teacher/teacher_one_topic_detail',
      onClose : function() {
        $(this).dialog('destroy');
      },
      width : $(document.body).width() * 0.8,
      height : $(document.body).height() * 0.8,
      collapsible : true,
      modal : true,
      title : '学生选题',
      buttons : [ {
        text : '保存',
        iconCls : 'icon-save',
        handler : function() {
          $.ajax({
            url : "${pageContext.request.contextPath}/teacher/topic/saveTopic",
            type : "post",
            data : {
              "topicId" : selected_topic_id,
              "passStatus" : $("#pass_status").val(),
              "teacherComment" : $("#teacher_comment").val()
            },
            success : function(data, textStatus) {
              $.messager.show({
                title : '提示',
                msg : data.msg
              });
            }
          });
          $temp_dialog.dialog('destroy');
          $('#topic_list_grid').datagrid('reload');
        }
      } ]
    });
  }

  $(function() {
    //初始化Topic列表
    $('#topic_list_grid').datagrid(
        {
          url : '${pageContext.request.contextPath}/teacher/topic/getMyStudentTopics',
          queryParams : {
            "teacherId" : '${sessionScope.USER.id}'
          },
          fitColumns : true,
          border : false,
          nowrap : false,
          singleSelect : true,
          frozenColumns : [ [ {
            field : 'id',
            title : 'Id',
            width : 10,
            hidden : true
          }, {
            field : 'studentId',
            title : 'studentId',
            width : 10,
            hidden : true
          } ] ],
          columns : [ [
              {
                field : 'studentName',
                title : '学生',
                width : 100,
              },
              {
                field : 'mainName',
                title : '标题',
                width : 100,
                formatter : function(value, rowData, index) {
                  var getTopic = '<a href="javascript:void(0)" class="easyui-linkbutton" id="btnChange"'
                      + 'data-options="plain:true" onclick="one_topic_detail(' + rowData.id + ')">' + value + '</a>';
                  return getTopic;
                }
              }, {
                field : 'subName',
                title : '副标题',
                width : 100,
              }, {
                field : 'createTime',
                title : '创建时间',
                width : 80,
              }, {
                field : 'updateTime',
                title : '更改时间',
                width : 80,
              }, {
                field : 'passStatus',
                title : '状态',
                width : 80,
              } ] ]
        });
  });
</script>



<fieldset style="color: #0099FF">
	<legend id="legend" align="left" style="color: #0099FF">学生列表</legend>
	<div id="topic_list_grid"></div>
</fieldset>
<br />
</div>


