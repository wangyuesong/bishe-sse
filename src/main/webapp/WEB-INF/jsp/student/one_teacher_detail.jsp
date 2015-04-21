<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:include page="/inc.jsp"></jsp:include>
<script>
  $.ajax({
    //selected_teacher_id in all_teachers.jsp
    url : "${pageContext.request.contextPath}/student/showOneTeacherDetail?teacherId=" + selected_teacher_id,
    type : "get",
    success : function(data, textStatus) {
      $.each(data, function(key, value) {
        $('#' + key).html(value);
      });
    }
  });
</script>
<div>
	<fieldset>
		<legend id="legend" align="center">教师信息</legend>
		<div>
			<table width="98%" border="0" cellpadding="2" class="tableForm"
				cellspacing="1" bgcolor="#D1DDAA" align="center"
				style="margin-top: 8px">
				<tr bgcolor="#FAFAF1">
					<td width="15%">姓名:</td>
					<td id="name" width="35%"></td>
					<td width="15%">职称:</td>
					<td id="title" width="35%"></td>
				</tr>
				<tr bgcolor="#FAFAF1">
					<td width="15%">邮箱:</td>
					<td id="email" width="35%"></td>
					<td width="15%">电话:</td>
					<td id="phone" width="35%"></td>
				</tr>
				<tr bgcolor="#FAFAF1">
					<td width="15%">学历:</td>
					<td id="degree" width="35%"></td>
					<td width="15%">计划指导人数:</td>
					<td id="capacity" width="35%"></td>
				</tr>
				<tr bgcolor="#FAFAF1">
					<td width="15%">方向:</td>
					<td id="direction" width="35%"></td>
					<td width="15%">备选题目:</td>
					<td id="candidateTopics" width="35%"></td>
				</tr>
			</table>
		</div>
	</fieldset>
</div>
