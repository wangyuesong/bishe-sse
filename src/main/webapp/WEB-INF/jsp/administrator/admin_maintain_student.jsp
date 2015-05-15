<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/inc.jsp"></jsp:include>
<fieldset>
	<legend>学生账号</legend>
	<table id="student_grid">
	</table>
</fieldset>


<fieldset>
	<legend>学生账号</legend>
	<table id="teacher_grid">
	</table>
</fieldset>

<script>
  var editIndex = undefined;
  var grid = $('#student_grid');
  $(function() {

    grid.datagrid({
      url : '${pageContext.request.contextPath}/admin/user/getAllStudents',
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
            field : 'account',
            title : '学号',
            sortable : "true",
            width : 80,
            editor : {
              type : "textbox"
            }
          },
          {
            field : 'name',
            title : '姓名',
            sortable : "true",
            width : 80,
            editor : {
              type : "textbox"
            }
          },
          {
            field : 'gender',
            title : '性别',
            sortable : "true",
            width : 80,
            editor : {
              type : "textbox"
            }
          },
          {
            field : 'gpa',
            title : '绩点',
            sortable : "true",
            width : 80,
            editor : {
              type : "textbox"
            }
          },
          {
            field : 'email',
            title : 'Email',
            width : 80,
            editor : {
              type : "textbox"
            }
          },
          {
            field : 'phone',
            title : '电话',
            width : 80,
            editor : {
              type : "textbox"
            }
          },
          {
            field : 'password',
            title : '密码',
            width : 80,
            editor : {
              type : "textbox"
            }
          },
          {
            field : 'selfDescription',
            title : '自我介绍',
            width : 80,
            editor : {
              type : "textbox"
            }
          },
          {
            field : 'opt',
            title : '操作',
            width : 50,
            formatter : function(value, rowData, index) {
              var remove = '<a href="javascript:void(0)" class="easyui-linkbutton" id="btnDelete"'
                  + 'data-options="plain:true" onclick="delete_student(' + rowData.id + ')">删除</a>';
              return remove;
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

    grid.datagrid('getPager').pagination({
      pageSize : 10,
      pageList : [ 5, 10, 15 ],
      beforePageText : "第",
      afterPageText : "页,共{pages}页"
    });

    //CSS related
    $("fieldset").css("border", "1px #99BBE8 dashed").css("padding", "20px").attr("align", "left");
    $("legend").css("color", "#0099FF").attr("align", "left");
  });

  function endEditing() {
    if (editIndex == undefined) {
      return true
    }
    if (grid.datagrid('validateRow', editIndex)) {
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
        url : "${pageContext.request.contextPath}/admin/user/changeStudents",
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
  function delete_student(student_id) {
    $.ajax({
      url : "${pageContext.request.contextPath}/admin/user/deleteStudent",
      type : "post",
      data : {
        "studentId" : student_id
      },
      success : function(data, textStatus) {
        $.messager.show({
          title : '提示',
          msg : data.msg
        });
      }
    });
    grid.datagrid("reload");
  }

  function getChanges() {
    var rows = grid.datagrid('getChanges');
    alert(JSON.stringify(rows));
    alert(rows.length + ' rows are changed!');
  }
</script>