<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/inc.jsp"></jsp:include>
<script>
  var loginForm;
  var loginDialog;

  $(function() {
    loginForm = $('#loginForm').form({
      url : '${pageContext.request.contextPath}/user/login',
      success : function(result) {
        var r = $.parseJSON(result);
        if (r.success) {
          loginDialog.dialog('close');
          if (r.obj.role == 'Student')
            window.location.href = "dispatch/stu_index";
          else if (r.obj.role == 'Teacher')
            window.location.href = "dispatch/tea_index";
        } else if (!r.success) {
          uo
        }

      }
    });

    loginDialog = $("#loginDialog").show().dialog({
      modal : true,
      title : '软件学院毕设选题系统',
      closeable : false,
      buttons : [ {
        text : "登陆",
        handler : function() {
          loginForm.submit();
        }
      } ]
    }).window('center');
  });
</script>
</head>
<body>
	<div class="easyui-dialog" id="loginDialog" title="登陆"
		style="width: 310px; height: 200px; padding: 10px">
		<div style="overflow: hidden">
			<div style="text-align: center;">
				<form method="post" id="loginForm">
					<table border="0" style="text-align: center;">
						<tr>
							<th colspan="2">软件学院毕设选题系统</th>
						</tr>
						<tr>
							<th>登陆名</th>
							<td><input name="account" class="easyui-validatebox"
								data-options="required:true,validType:'length[1,50]'" /></td>
						</tr>
						<tr>
							<th>密码</th>
							<td><input name="password" type="password"
								class="easyui-validatebox"
								data-options="required:true,validType:'length[6,16]'" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>