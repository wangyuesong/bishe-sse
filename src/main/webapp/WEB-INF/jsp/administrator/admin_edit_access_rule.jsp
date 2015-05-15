<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
      url : "${pageContext.request.contextPath}/admin/timenodemessage/getAccessRulesByTimeNodeIdAndRole",
      type : "post",
      success : function(data, textStatus) {

      }
    });

  })
</script>
<div class="section group">
	<div class="col span_1_of_8">
		<label>学生权限:</label>
	</div>
	<div class="col span_7_of_8">
		<select class="easyui-combobox" name="student_access_rule"
			id="student_access_rule"
			data-options="multiple:true,multiline:true,
			url:'${pageContext.request.contextPath}/admin/timenodemessage/getAllStudentAccessRules'
			,method: 'post'
			,valueField: 'url'
			,textField: 'name'"
			style="width: 200px; height: 50px">
		</select>
	</div>
</div>
