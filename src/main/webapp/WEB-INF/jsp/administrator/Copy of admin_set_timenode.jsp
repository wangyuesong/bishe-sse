<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/inc.jsp"></jsp:include>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jquery.datetimepicker.js"></script>
<link
	href="${pageContext.request.contextPath}/resources/style/jquery.datetimepicker.css"
	rel="stylesheet">

<fieldset>
	<legend id="legend" align="center">时间节点</legend>
	<div>
		<table width="98%" border="0" cellpadding="2" class="tableForm"
			cellspacing="1" bgcolor="#D1DDAA" align="center"
			style="margin-top: 8px">
			<tr bgcolor="#FAFAF1">
				<td width="15%">填报志愿:</td>
				<td width="35%"><input type="text" value=""
					id="tianbaozhiyuan_datetime_picker" /></td>
				<td width="15%">描述:</td>
				<td width="35%"><input class="easyui-textbox"
					id="tianbaozhiyuan_desc" data-options="multiline:true"
					style="width: 100%; height: 100px"></td>
			</tr>
			<tr bgcolor="#FAFAF1">
				<td width="15%">课题申报:</td>
				<td width="35%"><input type="text" value=""
					id="ketishenbao_datetime_picker" /></td>
				<td width="15%">描述:</td>
				<td width="35%"><input class="easyui-textbox"
					id="keitishenbao_desc" data-options="multiline:true"
					style="width: 100%; height: 100px"></td>
			</tr>

			<tr bgcolor="#FAFAF1">
				<td width="15%">毕设进行:</td>
				<td width="35%"><input type="text" value=""
					id="bishejinxing_datetime_picker" /></td>
				<td width="15%">描述:</td>
				<td width="35%"><input class="easyui-textbox"
					id="bishejinxing_desc" data-options="multiline:true"
					style="width: 100%; height: 100px"></td>
			</tr>
			<tr bgcolor="#FAFAF1">
				<td width="15%">答辩申请:</td>
				<td width="35%"><input type="text" value=""
					id="dabianshenqing_datetime_picker" /></td>
				<td width="15%">描述:</td>
				<td width="35%"><input class="easyui-textbox"
					id="dabianshenqing_desc" data-options="multiline:true"
					style="width: 100%; height: 100px"></td>
			</tr>
			<tr>
				<td width="15%"><a href="javascript:void(0);"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-reload',plain:true"
					id="updateWillButton" onclick="update_timenodes()">更新</a></td>
			</tr>
		</table>
	</div>
</fieldset>
<script type="text/javascript">
  function update_timenodes() {
    $.ajax({
      url : "${pageContext.request.contextPath}/admin/timenodemessage/updateTimeNodes",
      data : {
        "tianbaozhiyuan_date" : $('#tianbaozhiyuan_datetime_picker').val(),
        "ketishenbao_date" : $('#ketishenbao_datetime_picker').val(),
        "bishejinxing_date" : $('#bishejinxing_datetime_picker').val(),
        "dabianshenqing_date" : $('#dabianshenqing_datetime_picker').val(),
        "tianbaozhiyuan_desc" : $('#tianbaozhiyuan_desc').textbox("getValue"),
        "ketishenbao_desc" : $('#keitishenbao_desc').textbox("getValue"),
        "bishejinxing_desc" : $('#bishejinxing_desc').textbox("getValue"),
        "dabianshenqing_desc" : $('#dabianshenqing_desc').textbox("getValue"),
      },
      type : "post",
      success : function(data, textStatus) {
        $.messager.show({
          title : '提示',
          msg : data.msg
        });
      }
    });
  }

  $.ajax({
    url : "${pageContext.request.contextPath}/admin/timenodemessage/getCurrentTimeNodes",
    success : function(data, textStatus) {
      $('#tianbaozhiyuan_datetime_picker').val(data.tianbaozhiyuan_date);
      $('#ketishenbao_datetime_picker').val(data.ketishenbao_date);
      $('#bishejinxing_datetime_picker').val(data.bishejinxing_date);
      $('#dabianshenqing_datetime_picker').val(data.dabianshenqing_date);
      $('#tianbaozhiyuan_desc').textbox("setValue", data.tianbaozhiyuan_desc);
      $('#keitishenbao_desc').textbox("setValue", data.ketishenbao_desc);
      $('#bishejinxing_desc').textbox("setValue", data.bishejinxing_desc);
      $('#dabianshenqing_desc').textbox("settValue", dabianshenqing_desc);
    }
  });

  $('#tianbaozhiyuan_datetime_picker').datetimepicker({
    lang : 'ch',
  });
  $('#ketishenbao_datetime_picker').datetimepicker({
    lang : 'ch',
  });
  $('#bishejinxing_datetime_picker').datetimepicker({
    lang : 'ch',
  });
  $('#dabianshenqing_datetime_picker').datetimepicker({
    lang : 'ch',
  });

  $("fieldset").css("border", "1px #99BBE8 dashed").css("padding", "20px").attr("align", "left");
  $("legend").css("color", "#0099FF").attr("align", "left");
</script>