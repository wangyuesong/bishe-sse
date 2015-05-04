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
	<div style="margin: 20px 0;"></div>
	<table id="dg" class="easyui-datagrid" title="Row Editing in DataGrid"
		style="height: auto"
		data-options="
                iconCls: 'icon-edit',
                singleSelect: true,
                toolbar: '#tb',
                url: '${pageContext.request.contextPath}/admin/getCurrentMatchCondition',
                method: 'get',
                onClickCell: onClickCell
            ">
		<thead>
			<tr>
				<th data-options="field:'studentAccount',width:100">学号</th>
				<th data-options="field:'studentName',width:100">姓名</th>
				<table id='dg'></table>
				<%-- 				<th
					data-options="field:'teacherAccount',width:200,
                        formatter:function(value,row){
                            return row.teacherName;
                        },
                        editor:{
                            type:'combobox',
                            options:{
                                valueField:'account',
                                textField:'name',
                                url: '${pageContext.request.contextPath}/admin/getAllTeachers',
                                method: 'get'
                            }
                        }">指导教师</th> --%>


				<th data-options="field:'matchLevel',width:100">志愿匹配等级</th>
			</tr>
		</thead>
	</table>
	<div id="tb" style="height: auto">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-add',plain:true" onclick="append()">Append</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">Remove</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-save',plain:true" onclick="accept()">Accept</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-undo',plain:true" onclick="reject()">Reject</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search',plain:true"
			onclick="getChanges()">GetChanges</a>
	</div>
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
      width : 300,
      height : 300,
      singleSelect : true,
      idField : 'teacherAccount',
      url : '${pageContext.request.contextPath}/admin/getAllTeachers',
      columns : [ [ {
        field : 'teacherAccount',
        name : '教师账号',
        width : 100,
      }, {
        field : 'teacherName',
        name : '教师姓名',
        width : 100,
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
        /*   $.ajax({
            url : "${pageContext.request.contextPath}/admin/findTeacherAccountById?teacherId=" + teacher_id,
            type : "get",
            async : false,
            success : function(data, textStatus) {
              $('#dg').datagrid('getRows')[editIndex]['teacherAccount'] = data;
             
            }
          }); */
        alert(JSON.stringify($('#dg').datagrid('getRows')));
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
  </script>
</body>
