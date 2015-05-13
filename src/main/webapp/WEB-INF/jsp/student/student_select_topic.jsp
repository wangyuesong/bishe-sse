<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/inc.jsp"></jsp:include>
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/col.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/8cols.css"
	rel="stylesheet">
<script>
  $(function() {
    $.ajax({
      url : "${pageContext.request.contextPath}/student/topic/getTopic",
      data : {
        "studentId" : '${sessionScope.USER.id}'
      },
      type : "post",
      success : function(data, textStatus) {
        $("#mainName").textbox("setValue", data.mainName);
        $("#subName").textbox("setValue", data.subName);
        $("#outsider").textbox("setValue", data.outsider);
        $("#description").textbox("setValue", data.description);
        $('#topicType').combo('setValue', data.topicType).combo('setText', data.topicType);
        $("#pass_status").html(data.passStatus);
        $("#teacher_comment").html(data.teacherComment);
      }
    });

    $("#topic_form").form({
      url : '${pageContext.request.contextPath}/student/topic/saveTopic',
      type : "post",
      success : function(data, textResult) {
        var jdata = $.parseJSON(data);
        $.messager.show({
          title : "成功",
          msg : jdata.msg
        });
      }
    });
  })

  function save_topic() {
    $("#topic_form").submit();
  }
</script>
<div style="padding: 20px">
	<fieldset>
		<form id="topic_form" type="post">
			<input name="studentId" type="hidden" value="${sessionScope.USER.id}" />
			<legend id="legend">
				选题申请表 <a href="javascript:void(0);" class="easyui-linkbutton"
					data-options="iconCls:'icon-save',plain:true" id="updateWillButton"
					onclick="save_topic()">保存</a>
			</legend>
			<div class="section group">
				<div class="col span_1_of_8">
					<label>主标题:</label>
				</div>
				<div class="col span_7_of_8">
					<input class="easyui-textbox" name="mainName" id="mainName"
						style="width: 100%;"></input>
				</div>
			</div>

			<div class="section group">
				<div class="col span_1_of_8">
					<label>副标题:</label>
				</div>
				<div class="col span_7_of_8">
					<input class="easyui-textbox" name="subName" id="subName"
						style="width: 100%;"></input>
				</div>
			</div>

			<div class="section group">
				<div class="col span_1_of_8">
					<label>类型:</label>
				</div>
				<div class="col span_3_of_8">
					<select id="topicType" style="width: 100%;" name="topicType"></select>
				</div>
				<div class="col span_1_of_8">
					<label>校外指导:</label>
				</div>
				<div class="col span_3_of_8">
					<input class="easyui-textbox" name="outsider" id="outsider"
						style="width: 100%;"></input>
				</div>
			</div>
			<div class="section group">
				<div class="col span_1_of_8">
					<label>题目描述:</label>
				</div>
				<div class="col span_7_of_8">
					<input class="easyui-textbox" name="description" id="description"
						data-options="multiline:true" style="width: 100%; height: 300px"></input>
				</div>
			</div>
			<div class="section group">
				<div class="col span_1_of_8">
					<label>状态:</label>
				</div>
				<div class="col span_1_of_8">
					<label id="pass_status"></label>
				</div>
				<div class="col span_1_of_8">
					<label>教师意见:</label>
				</div>
				<div class="col span_5_of_8">
					<label id="teacher_comment"></label>
				</div>
			</div>
		</form>
	</fieldset>
</div>
<div id="type_selections">
	<div style="color: #99BBE8; background: #fafafa; padding: 5px;">选择一种类型</div>
	<div style="padding: 10px">
		<input type="radio" name="topicType" value="教师选题"><span>教师选题</span><br />
		<input type="radio" name="topicType" value="企业选题"><span>企业选题</span><br />
		<input type="radio" name="topicType" value="个人选题"><span>个人选题</span><br />
	</div>
</div>

<script>
  $(function() {
    $('#topicType').combo({
      required : true,
      editable : false
    });
    $('#type_selections').appendTo($('#topicType').combo('panel'));
    $('#topicType').combo('setValue', "教师选题").combo('setText', "教师选题");
    $('#type_selections input').click(function() {
      var v = $(this).val();
      var s = $(this).next('span').text();
      $('#topicType').combo('setValue', v).combo('setText', s).combo('hidePanel');
    });
  })
  $("fieldset").css("border", "1px #99BBE8 dashed").css("padding", "20px").attr("align", "left");
  $("legend").css("color", "#0099FF").attr("align", "left");
</script>
