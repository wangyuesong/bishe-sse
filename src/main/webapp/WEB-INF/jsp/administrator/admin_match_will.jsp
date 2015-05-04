<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/inc.jsp"></jsp:include>
<style>
.input {
	width: 170px;
}
</style>
</head>
<body>
	<table id="dg">
	</table>

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
      }, '-' ],
      idField : 'teacherAccount',
      url : '${pageContext.request.contextPath}/admin/getCurrentMatchCondition',
      columns : [ [ {
        field : 'studentAccount',
        title : '学号',
        name : '学号',
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
            textField : 'name',
            url : '${pageContext.request.contextPath}/admin/getAllTeachers',
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
      } ] ]
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
        var teacher_name = $(ed.target).combobox('getText');
        var teacher_id = $(ed.target).combobox('getValue');
        //更改绑定的数据
        $('#dg').datagrid('getRows')[editIndex]['teacherName'] = teacher_name;
        $('#dg').datagrid('getRows')[editIndex]['teacherId'] = teacher_id;
        $.ajax({
          url : "${pageContext.request.contextPath}/admin/doCapacityCheck",
          type : "post",
          async : false,
          dataType : 'json',
          contentType : 'application/json',
          data : JSON.stringify($('#dg').datagrid('getRows')),
          success : function(data, textStatus) {
            if (!data.success) {
              $.messager.show({
                title : '错误',
                msg : data.msg
              });
            }
          }
        });
        $('#dg').datagrid('endEdit', editIndex);
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
        var rows = $('#dg').datagrid('getChanges');
        alert(rows);
        $.ajax({
          //发送请求改变学生和老师的匹配
          url : "${pageContext.request.contextPath}/admin/updateMatchPairs",
          dataType : 'json',
          contentType : 'application/json',
          data : JSON.stringify(rows),
          type : "post",
          success : function(data, textStatus) {
            /* $.each(data, function(n, value) {
              alert(value.listName);
            }); */
            $('#dg').datagrid("reload");
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
      alert("System assign");
    }
  </script>
</body>
