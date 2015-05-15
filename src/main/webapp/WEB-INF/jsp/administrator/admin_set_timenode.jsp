<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/inc.jsp"></jsp:include>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jquery.datetimepicker.js"></script>
<link
	href="${pageContext.request.contextPath}/resources/style/jquery.datetimepicker.css"
	rel="stylesheet">

<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/col.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/responsivegridsystem/css/8cols.css"
	rel="stylesheet">

<fieldset>
	<legend id="legend" align="center">时间节点</legend>
	<div>
		<table id="time_node_datagrid"></table>
	</div>
</fieldset>
<br />
<fieldset>
	<legend id="detail_legend" align="center">节点规则</legend>
	<div class="section group">
		<div class="col span_1_of_8">
			<label>学生禁止:</label>
		</div>
		<div class="col span_3_of_8">
			<select class="easyui-combobox" name="student_access_rule"
				id="student_access_rule"
				data-options="multiple:true,multiline:true,
			url:'${pageContext.request.contextPath}/admin/timenodemessage/getAllStudentAccessRules'
			,method: 'post'
			,valueField: 'url'
			,textField: 'name'"
				style="width: 200px; height: 50px"></select>
		</div>

		<div class="col span_1_of_8">
			<label>教师禁止:</label>
		</div>
		<div class="col span_3_of_8">
			<select class="easyui-combobox" name="teacher_access_rule"
				id="teacher_access_rule"
				data-options="multiple:true,multiline:true,
			url:'${pageContext.request.contextPath}/admin/timenodemessage/getAllTeacherAccessRules'
			,method: 'post'
			,valueField: 'url'
			,textField: 'name'"
				style="width: 200px; height: 50px"></select>
		</div>
	</div>
	<a href="javascript:void(0);" class="easyui-linkbutton"
		onclick="save_access_rules()"
		data-options="iconCls:'icon-save',plain:true" id="updateWillButton">保存</a>
</fieldset>
<script type="text/javascript">
  var time_node_types = [ {
    "value" : "关键",
    "text" : "关键"
  }, {
    "value" : "普通",
    "text" : "普通"
  } ];
  var editIndex = undefined;
  var selected_time_node_id = undefined;
  var grid = $('#time_node_datagrid');

  $('#time_node_datagrid').datagrid(
      {
        url : '${pageContext.request.contextPath}/admin/timenodemessage/getCurrentTimeNodesInDatagrid',
        type : 'post',
        onClickCell : onClickCell,
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
              field : 'name',
              title : '节点名',
              width : 80,
              editor : {
                type : "textbox"
              }
            },
            {
              field : 'time',
              title : '时间',
              sortable : "true",
              width : 80,
              editor : {
                type : "datetimepicker"
              }
            },
            {
              field : 'type',
              title : '类型',
              width : 30,
              editor : {
                type : "combobox",
                options : {
                  data : time_node_types,
                  valueField : "value",
                  textField : "text"
                }
              }
            },
            {
              field : 'description',
              title : '描述',
              width : 100,
              editor : {
                type : "textbox"
              }
            },
            {
              field : 'opt',
              title : '操作',
              width : 100,
              formatter : function(value, rowData, index) {
                var edit = '<a href="javascript:void(0)" class="easyui-linkbutton" id="btnDelete"'
                    + 'data-options="plain:true" onclick="edit_access_rules(' + rowData.id + ',\'' + rowData.name
                    + '\')">更改规则</a>';
                var remove = '<a href="javascript:void(0)" class="easyui-linkbutton" id="btnDelete"'
                    + 'data-options="plain:true" onclick="delete_time_node(' + rowData.id + ')">删除节点</a>';
                return edit + " " + remove;
              }
            } ] ],
        toolbar : [ '-', {
          text : '保存',
          iconCls : 'icon-save',
          handler : accept
        }, '-', '-', {
          text : '增加',
          iconCls : 'icon-add',
          handler : append
        }, '-', '-', {
          text : '取消',
          iconCls : 'icon-undo',
          handler : reject
        }, '-', '-', {
          text : '查看变更',
          iconCls : 'icon-search',
          handler : getChanges
        }, '-' ]
      });

  $('#time_node_datagrid').datagrid('getPager').pagination({
    pageSize : 10,
    pageList : [ 5, 10, 15 ],
    beforePageText : "第",
    afterPageText : "页,共{pages}页"
  });
  $.extend($.fn.datagrid.defaults.editors, {
    datetimepicker : {
      init : function(container, options) {
        var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);
        input.datetimepicker({
          lang : 'ch',
        });
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

  function endEditing() {
    if (editIndex == undefined) {
      return true
    }
    if (grid.datagrid('validateRow', editIndex)) {
      var ed = grid.datagrid('getEditor', {
        index : editIndex,
        field : 'teacherAccount'
      });
      grid.datagrid('endEdit', editIndex);
      editIndex = undefined;
      return true;
    } else {
      return false;
    }
  }
  function onClickCell(index, field) {
    if (editIndex != index) {
      if (endEditing() && field != "opt") {
        grid.datagrid('selectRow', index).datagrid('beginEdit', index);
        var ed = grid.datagrid('getEditor', {
          index : index,
          field : field
        });
        /*  ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus(); */
        editIndex = index;
      } else {
        grid.datagrid('selectRow', editIndex);
      }
    }
  }
  function append() {
    if (endEditing()) {
      grid.datagrid('appendRow', {});
      editIndex = grid.datagrid('getRows').length - 1;
      grid.datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
    }
  }
  function removeit() {
    if (editIndex == undefined) {
      return;
    }
    grid.datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
    editIndex = undefined;
  }
  function accept() {
    if (endEditing()) {
      var changed_rows = grid.datagrid('getChanges');
      $.ajax({
        url : "${pageContext.request.contextPath}/admin/timenodemessage/changeTimeNodes",
        type : "post",
        dataType : 'json',
        contentType : 'application/json',
        data : JSON.stringify(grid.datagrid('getChanges')),
        success : function(data, textStatus) {
          $.messager.show({
            title : '提示',
            msg : data.msg
          });
        }
      });
      grid.datagrid('acceptChanges');
    }
  }
  function reject() {
    grid.datagrid('rejectChanges');
    editIndex = undefined;
  }
  function getChanges() {
    var rows = grid.datagrid('getChanges');
    alert(JSON.stringify(rows));
    alert(rows.length + ' rows are changed!');
  }

  function delete_time_node(time_node_id) {
    $.ajax({
      url : "${pageContext.request.contextPath}/admin/timenodemessage/deleteTimeNode",
      data : {
        "timeNodeId" : time_node_id
      },
      type : "post",
      success : function(data, textStatus) {
        $.messager.show({
          title : '提示',
          msg : data.msg
        });
        grid.datagrid("reload");
      }
    });

  }

  function edit_access_rules(time_node_id, time_node_name) {
    selected_time_node_id = time_node_id;
    $("#detail_legend").html(time_node_name + "节点规则");
    $.ajax({
      url : "${pageContext.request.contextPath}/admin/timenodemessage/getAccessRulesByTimeNodeIdAndRole",
      type : "post",
      data : {
        "timeNodeId" : time_node_id,
        "role" : "Student"
      },
      success : function(data, textStatus) {
        $("#student_access_rule").combobox("clear");
        $.each(data, function(index, value) {
          banned = value.banned;
          if (banned)
            $("#student_access_rule").combobox("select", value.url);
        })

      }
    });

    $.ajax({
      url : "${pageContext.request.contextPath}/admin/timenodemessage/getAccessRulesByTimeNodeIdAndRole",
      type : "post",
      data : {
        "timeNodeId" : time_node_id,
        "role" : "Teacher"
      },
      success : function(data, textStatus) {
        $("#teacher_access_rule").combobox("clear");
        $.each(data, function(index, value) {
          banned = value.banned;
          if (banned)
            $("#teacher_access_rule").combobox("select", value.url);
        })

      }
    });
  }

  function save_access_rules() {
    if (selected_time_node_id == undefined)
      return;
    data = {
      "teacherRules" : $("#teacher_access_rule").combo("getValues"),
      "studentRules" : $("#student_access_rule").combo("getValues"),
      "timeNodeId" : selected_time_node_id
    };
    $.ajax({
      url : "${pageContext.request.contextPath}/admin/timenodemessage/saveAccessRules",
      type : "post",
      data : JSON.stringify(data),
      dataType : "json",
      contentType : "application/json",
      success : function(data, textStatus) {

      }
    });
  }

  //CSS related

  $("fieldset").css("border", "1px #99BBE8 dashed").css("padding", "20px").attr("align", "left");
  $("legend").css("color", "#0099FF").attr("align", "left");
</script>