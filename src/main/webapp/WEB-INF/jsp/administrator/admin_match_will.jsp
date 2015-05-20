<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/inc.jsp"></jsp:include>
<style>
.input {
	width: 170px;
}
</style>
<fieldset>
	<legend>匹配情况</legend>
	<table id="dg">
	</table>
</fieldset>
<script type="text/javascript">
  $.extend($.fn.datagrid.defaults.editors, {
    combogrid : {
      init : function(container, options) {
        var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);
        input.combogrid(options);
        return input;
      },
      destroy : function(target) {
        $(target).combogrid('destroy');
      },
      getValue : function(target) {
        return $(target).combogrid('getValue');
      },
      setValue : function(target, value) {
        $(target).combogrid('setValue', value);
      },
      resize : function(target, width) {
        $(target).combogrid('resize', width);
      }
    }
  });

  $('#dg').datagrid({
    onClickCell : onClickCell,
    singleSelect : true,
    pagination : true,
    rownumbers : true,
    toolbar : [ '-', {
      text : '保存',
      iconCls : 'icon-save',
      handler : accept
    }, '-', '-', {
      text : '取消',
      iconCls : 'icon-undo',
      handler : reject
    }, '-', '-', {
      text : '查看变更',
      iconCls : 'icon-search',
      handler : getChanges
    }, '-', '-', {
      text : '系统分配',
      iconCls : 'icon-search',
      handler : systemAssign
    }, '-', '-', {
      text : '导出到Excel',
      iconCls : 'icon-print',
      handler : exportInExcel
    }, '-'

    ],
    idField : 'teacherAccount',
    url : '${pageContext.request.contextPath}/admin/will/getCurrentMatchConditionInDatagrid',
    columns : [ [ {
      field : 'studentAccount',
      title : '学号',
      name : '学号',
      sortable : true,
      width : 150,
    }, {
      field : 'studentName',
      title : '学生姓名',
      name : '学生姓名',
      width : 150,
    }, {
      field : 'teacherAccount',
      title : '教师',
      name : '教师',
      width : 150,
      formatter : function(value, row) {
        return row.teacherName;
      },
      editor : {
        type : 'combogrid',
        options : {
          panelWidth : 450,
          idField : 'id',
          fitColumns : true,
          textField : 'name',
          url : '${pageContext.request.contextPath}/admin/will/getAllTeachers',
          columns : [ [ {
            field : 'account',
            title : '工号',
            width : 60
          }, {
            field : 'capacity',
            title : '容量',
            width : 100
          }, {
            field : 'name',
            title : '姓名',
            width : 120
          } ] ]
        }
      }
    }, {
      field : 'matchLevel',
      title : '匹配等级',
      name : '匹配等级',
      width : 150,
    }, {
      field : 'matchType',
      title : '匹配方式',
      name : '匹配方式',
      width : 150,
    } ] ]
  });

  var p = $('#dg').datagrid('getPager');
  $(p).pagination({
    pageSize : 10,
    pageList : [ 5, 10, 15 ],
    beforePageText : "第",
    afterPageText : "页,共{pages}页"
  });

  var editIndex = undefined;

  function endEditing() {
    if (editIndex == undefined) {
      return true
    }
    if ($('#dg').datagrid('validateRow', editIndex)) {
      var ed = $('#dg').datagrid('getEditor', {
        index : editIndex,
        field : 'teacherAccount'
      });
      var teacher_name = $(ed.target).combogrid('getText');
      var teacher_id = $(ed.target).combogrid('getValue');
      //更改绑定的数据
      var one_row_data = $('#dg').datagrid('getRows')[editIndex];
      one_row_data['teacherName'] = teacher_name;
      one_row_data['teacherId'] = teacher_id;
      one_row_data['matchLevel'] = "";
      one_row_data['matchType'] = '手工分配';
      $('#dg').datagrid('endEdit', editIndex);
      $.ajax({
        url : "${pageContext.request.contextPath}/admin/will/doCapacityCheck",
        type : "post",
        async : false,
        dataType : 'json',
        contentType : 'application/json',
        data : JSON.stringify($('#dg').datagrid('getChanges')),
        success : function(data, textStatus) {
          if (!data.success) {
            $.messager.show({
              title : '错误',
              msg : data.msg
            });
          }
        }
      });
      editIndex = undefined;
      return true;
    } else {
      return false;
    }
  }
  function onClickCell(index, field) {
    if (editIndex != index) {
      if (endEditing()) {
        $('#dg').datagrid('selectRow', index).datagrid('beginEdit', index);
        var ed = $('#dg').datagrid('getEditor', {
          index : index,
          field : 'teacherAccount'
        });
        ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
        editIndex = index;
      } else {
        $('#dg').datagrid('selectRow', editIndex);
      }
    }
  }
  function append() {
    if (endEditing()) {
      $('#dg').datagrid('appendRow', {
        status : 'P'
      });
      editIndex = $('#dg').datagrid('getRows').length - 1;
      $('#dg').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
    }
  }
  function removeit() {
    if (editIndex == undefined) {
      return;
    }
    $('#dg').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
    editIndex = undefined;
  }
  function accept() {
    if (endEditing()) {
      /*  $('#dg').datagrid('acceptChanges'); */
      var changed_rows = $('#dg').datagrid('getChanges');
      $.ajax({
        //发送请求改变学生和老师的匹配
        url : "${pageContext.request.contextPath}/admin/will/updateMatchPairs",
        dataType : 'json',
        contentType : 'application/json',
        data : JSON.stringify(changed_rows),
        type : "post",
        success : function(data, textStatus) {
          if (!data.success) {
            $.messager.show({
              title : '错误',
              msg : data.msg
            });
          } else {
            $.messager.show({
              title : '成功',
              msg : data.msg
            });
          }
          $('#dg').datagrid('reload');
        }
      });
    }
  }
  function reject() {
    $('#dg').datagrid('rejectChanges');
    editIndex = undefined;
  }
  function getChanges() {

    var rows = $('#dg').datagrid('getChanges');
    alert(JSON.stringify(rows));
    alert(rows.length + ' rows are changed!');
  }
  function systemAssign() {
    $dialog = $('<div class="temp_dialog"></div>').dialog({
      href : '${pageContext.request.contextPath}/dispatch/administrator/admin_match_will_preference_select',
      onClose : function() {
        $(this).dialog('destroy');
      },
      width : $(document.body).width() * 0.5,
      height : $(document.body).height() * 0.5,
      collapsible : true,
      resizable : true,
      modal : true,
      title : '优先排序',
      buttons : [ {
        text : '确定',
        iconCls : 'icon-ok',
        handler : function() {
          //获取偏好排序
          var a = "";
          $('.drag-item').each(function() {
            a += $(this).attr("value") + ",";
          });
          a = a.substring(0, a.length - 1);

          $.ajax({
            //发送请求改变学生和老师的匹配
            url : "${pageContext.request.contextPath}/admin/will/systemAssign",
            data : {
              "sortCriteria" : a
            },
            type : "post",
            success : function(data, textStatus) {
              data_length = $('#dg').datagrid('getRows').length;
              $.each(data, function(index, value) {
                for (i = 0; i < data_length; i++) {
                  if ($.trim($('#dg').datagrid('getRows')[i]['studentId']) + "" == $.trim(value.studentId) + "") {
                    var one_row_data = $('#dg').datagrid('getRows')[i];
                    $('#dg').datagrid('selectRow', i).datagrid('beginEdit', i);
                    one_row_data['teacherName'] = value.teacherName;
                    one_row_data['teacherId'] = value.teacherId;
                    one_row_data['matchLevel'] = value.matchLevel;
                    one_row_data['matchType'] = value.matchType;
                    $('#dg').datagrid('endEdit', i);
                  }
                }
              });
              $dialog.dialog('destroy');
              $.messager.show({
                title : '提示',
                msg : "系统分配完毕，请保存"
              });

            }
          });

        }
      } ]
    });

  }
  function exportInExcel() {
    window.location.href = "${pageContext.request.contextPath}/admin/will/exportCurrentMatchConditionInExcel";
  }

  $("fieldset").css("border", "1px #99BBE8 dashed").css("padding", "20px").attr("align", "left");
  $("legend").css("color", "#0099FF").attr("align", "left");
</script>
</body>
