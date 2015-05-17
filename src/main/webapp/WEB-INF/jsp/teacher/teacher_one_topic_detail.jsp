<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/8cols.css"
	rel="stylesheet">
<script>
  //载入这个Topic的Detail
  $(function() {
    $.ajax({
      url : "${pageContext.request.contextPath}/teacher/topic/getTopic",
      data : {
        "topicId" : selected_topic_id
      },
      type : "post",
      success : function(data, textStatus) {
        $("#mainName").text(data.mainName);
        $("#subName").text(data.subName);
        $("#outsider").text(data.outsider);
        $("#description").text(data.description);
        $('#topic_type').text(data.topicType);
        $("#pass_status").val(data.passStatus);
        $("#teacher_comment").text(data.teacherComment);
      }
    });

    autosize($('textarea'));

    $("fieldset").css("border", "1px #99BBE8 dashed").css("padding", "20px").attr("align", "left");
    $("legend").css("color", "#0099FF").attr("align", "left");
  });
</script>
<div style="padding: 20px">
	<fieldset>
		<legend>学生选题</legend>
		<div class="section group">
			<div class="col span_1_of_8">
				<label>主标题:</label>
			</div>
			<div class="col span_3_of_8">
				<label id="mainName" style="width: 100%;"></label>
			</div>
			<div class="col span_1_of_8">
				<label>副标题:</label>
			</div>
			<div class="col span_3_of_8">
				<label id="subName" style="width: 100%;"></label>
			</div>
		</div>
		<div class="section group">
			<div class="col span_1_of_8">
				<label>类型:</label>
			</div>
			<div class="col span_3_of_8">
				<label id="topic_type" style="width: 100%;"></label>
			</div>
			<div class="col span_1_of_8">
				<label>校外指导:</label>
			</div>
			<div class="col span_3_of_8">
				<label id="outsider" style="width: 100%;"></label>
			</div>
		</div>
		<div class="section group">
			<div class="col span_1_of_8">
				<label>题目描述:</label>
			</div>
			<div class="col span_7_of_8">
				<p id="description" style="width: 100%;"></p>
			</div>
		</div>
		<div class="section group">
			<div class="col span_1_of_8">
				<label>状态:</label>
			</div>
			<div class="col span_1_of_8">
				<select id="pass_status" style="width: 100%;" name="passStatus">
					<option value="待审核">待审核</option>
					<option value="待修正">待修正</option>
					<option value="通过">通过</option>
				</select>
			</div>
			<div class="col span_1_of_8">
				<label>教师意见:</label>
			</div>
			<div class="col span_5_of_8">
				<textarea id="teacher_comment" name="teacherComment"
					style="width: 100%; height: 300px"></textarea>
			</div>
		</div>
	</fieldset>
</div>
