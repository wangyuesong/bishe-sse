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
      }, '-' ],
      idField : 'teacherAccount',
      url : '${pageContext.request.contextPath}/admin/will/getWillList',
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
        field : 'firstWill',
        title : '第一志愿',
        name : '第一志愿',
        width : 150,
        formatter : function(value, row) {
          return row.firstWillTeacherName;
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
        field : 'secondWill',
        title : '第二志愿',
        name : '第二志愿',
        width : 150,
        formatter : function(value, row) {
          return row.secondWillTeacherName;
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
        field : 'thirdWill',
        title : '第三志愿',
        name : '第三志愿',
        width : 150,
        formatter : function(value, row) {
          return row.thirdWillTeacherName;
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
        var first = $('#dg').datagrid('getEditor', {
          index : editIndex,
          field : 'firstWill'
        });
        var second = $('#dg').datagrid('getEditor', {
          index : editIndex,
          field : 'secondWill'
        });
        var third = $('#dg').datagrid('getEditor', {
          index : editIndex,
          field : 'thirdWill'
        });
        var first_will_teacher_name = $(first.target).combogrid('getText');
        var first_will_teacher_id = $(first.target).combogrid('getValue');
        var second_will_teacher_name = $(second.target).combogrid('getText');
        var second_will_teacher_id = $(second.target).combogrid('getValue');
        var third_will_teacher_name = $(third.target).combogrid('getText');
        var third_will_teacher_id = $(third.target).combogrid('getValue');
        //更改绑定的数据
        var one_row_data = $('#dg').datagrid('getRows')[editIndex];
        one_row_data['firstWill'] = first_will_teacher_id;
        one_row_data['firstWillTeacherName'] = first_will_teacher_name;
        one_row_data['secondWill'] = second_will_teacher_id;
        one_row_data['secondWillTeacherName'] = second_will_teacher_name;
        one_row_data['thirdWill'] = third_will_teacher_id;
        one_row_data['thirdWillTeacherName'] = third_will_teacher_name;
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
            field : field
          });
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
        //FIXME
        //此处有问题，一个row中有三个combogrid时，getChanges获取不到改动，此处采用直接把所有rows都传回后台的方式
        var rows = $('#dg').datagrid('getRows');
        $.ajax({
          //发送请求改变学生和老师的匹配
          url : "${pageContext.request.contextPath}/admin/will/updateWills",
          dataType : 'json',
          contentType : 'application/json',
          data : JSON.stringify(rows),
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
      var rows = $('#dg').datagrid('getRows');
      row = $('#dg').datagrid('getChanges');
      alert(row.length + ' rows are changed!');
    }
  </script>
</body>
