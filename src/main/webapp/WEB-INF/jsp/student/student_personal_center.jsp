<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="/inc.jsp"></jsp:include>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<table id="pg" class="easyui-propertygrid" style="width: 100%"
	data-options="
                url:'${pageContext.request.contextPath}/personal/getPersonalInfo',
                queryParams:{'userId' : ${sessionScope.USER.id}},
                method:'post',
                showGroup:true,
                showHeader: false,
                scrollbarSize:0
            ">
</table>
<div style="margin: 20px 0;">
	<a href="javascript:void(0)" class="easyui-linkbutton"
		data-options="iconCls:'icon-save',plain:true"
		onclick="save_student_personal_info()">保存</a>
</div>
<script type="text/javascript">
  $.extend($.fn.datagrid.defaults.editors, {
    textarea : {
      init : function(container, options) {
        var input = $('<textarea cols=30 rows=10 id="self_description" class="datagrid-editable-input">').appendTo(
            container);
        return input;
      },
      destroy : function(target) {
        $(target).remove();
      },
      getValue : function(target) {
        return $(target).val();
      },
      setValue : function(target, value) {
        $(target).val(value);
      },
      resize : function(target, width) {
        $(target)._outerWidth(width);
      }
    }
  });
  autosize($('textarea'));
  function showGroup() {
    $('#pg').propertygrid({
      showGroup : true
    });
  }
  function hideGroup() {
    $('#pg').propertygrid({
      showGroup : false
    });
  }
  function showHeader() {
    $('#pg').propertygrid({
      showHeader : true
    });
  }
  function hideHeader() {
    $('#pg').propertygrid({
      showHeader : false
    });
  }
  function save_student_personal_info() {
    var rows = $('#pg').propertygrid('getChanges');
    data = {
      "studentId" : '${sessionScope.USER.id}',
      "models" : rows
    };
    $.ajax({
      url : "${pageContext.request.contextPath}/personal/saveStudentPersonalInfo",
      type : "post",
      dataType : "json",
      contentType : "application/json",
      data : JSON.stringify(data),
      success : function(data, textStatus) {
        $('#pg').datagrid('acceptChanges');
        $.messager.show({
          title : '提示',
          msg : data.msg
        });
      }
    });
  }
</script>