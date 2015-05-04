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
<script>
  function show_one_teacher_detail(id) {
    selected_teacher_id = id;
    $('#one_teacher_datagrid').dialog({
      href : '${pageContext.request.contextPath}/dispatch/student/one_teacher_detail',
      onClose : function() {
      },
      width : $(document.body).width() * 0.9,
      height : $(document.body).height() * 0.9,
      collapsible : true,
      modal : true,
      title : '教师详细信息',
      onClose : function() {
        $(this).dialog('destroy');
      }
    });
  }

  $(function() {
    //在载入控件之前暂存所有teacher的信息，用于初始化combogrid
    var all_teacher_json;
    $('#all_teachers_datagrid').datagrid(
        {
          url : '${pageContext.request.contextPath}/admin/getCurrentMatchCondition',
          fitColumns : true,
          border : false,
          nowrap : false,
          pagination : true,
          singleSelect : true,
          pageSize : 10,
          onClickCell : onClickCell,
          //Load之前向服务器请求所有Teacher的数据
          onBeforeLoad : function() {
            $.ajax({
              url : "${pageContext.request.contextPath}/admin/getAllTeachers",
              type : "post",
              async : false,
              success : function(data, textStatus) {
                all_teacher_json = data;
              }
            });
          },
          //Datagrid载入成功后初始化教师的Select
          onLoadSuccess : function() {
            //初始化Select(combogrid)
            /*    $('.all_teacher_combogrid').each(function(index) {
                 $(this).combogrid({
                   panelWidth : 450,
                   idField : 'id',
                   textField : 'name',
                   onChange : function(newValue, oldValue) {
                     alert($(this).val());
                   },
                   columns : [ [ {
                     field : 'name',
                     title : '姓名',
                     width : 60
                   }, {
                     field : 'capacity',
                     title : '容量',
                     width : 100
                   } ] ]
                 });
               }); */
            /*  //初始化Select中的所有教师数据
             $('.all_teacher_combogrid').each(function(index) {
               $(this).combogrid("grid").datagrid("loadData", all_teacher_json);
             });
             //设置Select中的初始选中值，根据data-selected-teacher-id
             $('.all_teacher_combogrid').each(function(index) {
               selected_teacher_id = $(this).attr("data-selected-teacher-id");
               //0代表还未匹配，跳过
               if (selected_teacher_id == 0)
                 return true;
               $(this).combogrid('setValue', selected_teacher_id);
             }); */
          },
          //各列
          frozenColumns : [ [ {
            field : 'studentId',
            title : '学生编号',
            width : 50,
            hidden : true
          }, {
            field : 'teacherId',
            title : '教师编号',
            width : 50,
            hidden : true
          } ] ],
          columns : [ [
              {
                field : 'studentName',
                sortable : true,
                title : '学生姓名',
                width : 40
              },
              {
                field : 'studentAccount',
                title : '学号',
                sortable : true,
                width : 40,
              },
              {
                field : 'teacherName',
                title : '教师姓名',
                width : 100,
                sortable : true,
                editor : {
                  type : "combobox",
                  options : {
                    valueField : 'id',
                    idField : 'id',
                    textField : 'name',
                    data : all_teacher_json
                  }
                }

              /* formatter : function(value, rowData, index) {
                //这里使用data-selected-teacher-id暂存该select中应该被选中的值
                var select = "<input data-selected-teacher-id='" + rowData.teacherId + "' class='all_teacher_combogrid'>";
                return select;
              } */
              },
              {
                field : 'matchLevel',
                title : '志愿匹配等级',
                width : 55,
                sortable : true
              },
              {
                field : 'opt',
                title : '操作',
                width : 100,
                formatter : function(value, rowData, index) {
                  var remove = '<a href="javascript:void(0)" class="easyui-linkbutton" id="btnCancelUpload"'
                      + 'data-options="plain:true" onclick="delete_one_attachment(' + rowData.id + ')">删除</a>';
                  return remove;
                }
              } ] ],
          toolbar : [ '-', {
            text : '刷新',
            iconCls : 'icon-reload',
            handler : function() {
              $('#all_teachers_datagrid').datagrid('reload');
            }
          }, '-', {
            text : '系统分配',
            iconCls : 'icon-reload',
            handler : function() {
              var myData = $('#all_teachers_datagrid').datagrid('getData');
              alert("data" + JSON.stringify(myData));
              all_teachers_datagrid.datagrid('reload');
            }
          } ]
        });
  });
</script>
</head>
<body>
	<!-- <div id="dd" title="My Dialog" style="width: 400px; height: 200px;">
	Dialog Content.</div> -->
	<div id="tab" data-options="region:'center'" style="height: 100%">
		<table id="dg1" class="easyui-datagrid"
			title="Row Editing in DataGrid" style="width: 700px; height: auto"
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
					<th name="hello"
						data-options="field:'studentAccount',width:80,editor:'textbox'">学号</th>
					<th data-options="field:'studentName',width:80,align:'right'">姓名</th>
					<th
						data-options="field:'teacherAccount',width:100,
                        formatter:function(value,row){
                            return row.teacherName;
                        },
                        editor:'textbox'
                       <%--  editor:{
                            type:'combobox',
                            options:{
                                valueField:'id',
                                textField:'name',
                                method:'get',
                                data: '${pageContext.request.contextPath}/admin/getAllTeachers'
                            } --%>
                        ">匹配教师</th>
					<!-- <th
						data-options="field:'unitcost',width:80,align:'right',editor:'numberbox'">Unit
						Cost</th>
					<th data-options="field:'attr1',width:250,editor:'textbox'">Attribute</th>
					<th
						data-options="field:'status',width:60,align:'center',editor:{type:'checkbox',options:{on:'P',off:''}}">Status</th> -->
				</tr>
			</thead>
		</table>
	</div>
	<div id="one_teacher_datagrid"></div>



	<script>
/*     //有关Editor
    var editIndex = undefined;
    function endEditing() {
      if (editIndex == undefined) {
        return true
      }
      if ($('#all_teachers_datagrid').datagrid('validateRow', editIndex)) {
        var ed = $('#all_teachers_datagrid').datagrid('getEditor', {
          index : editIndex,
          field : 'teacherName'
        });
        var teacherName = $(ed.target).combox('getText');
        $('#all_teachers_datagrid').datagrid('getRows')[editIndex]['teacherName'] = teacherName;
        $('#all_teachers_datagrid').datagrid('endEdit', editIndex);
        editIndex = undefined;
        return true;
      } else {
        return false;
      }
    }
    function onClickCell(index, field) {
      if (editIndex != index) {
        if (endEditing()) {
          $('#all_teachers_datagrid').datagrid('selectRow', index).datagrid('beginEdit', index);
          var $all_teachers_datagrid = $('#all_teachers_datagrid');
          var a = $all_teachers_datagrid.datagrid('getEditor', {
            index : 1
          });
          var ed = $all_teachers_datagrid.datagrid('getEditor', {
            index : index,
            field : field
          });
          ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
          editIndex = index;
        } else {
          $('#all_teachers_datagrid').datagrid('selectRow', editIndex);
        }
      }
    } */
  </script>

	<h2>Row Editing in DataGrid</h2>
	<p>Click the row to start editing.</p>
	<div style="margin: 20px 0;"></div>

	<table id="dg" class="easyui-datagrid" title="Row Editing in DataGrid"
		style="width: 700px; height: auto"
		data-options="
                iconCls: 'icon-edit',
                singleSelect: true,
                toolbar: '#tb',
                url: '${pageContext.request.contextPath}/resources/data.json',
                method: 'get',
                onClickCell: onClickCell
            ">
		<thead>
			<tr>
				<th data-options="field:'itemid',width:80">Item ID</th>
				<th
					data-options="field:'productid',width:100,
                        formatter:function(value,row){
                            return row.productname;
                        },
                        editor:{
                            type:'combobox',
                            options:{
                                valueField:'productid',
                                textField:'productname',
                                method:'get',
                                url:'products.json',
                                required:true
                            }
                        }">Product</th>
				<th
					data-options="field:'listprice',width:80,align:'right',editor:{type:'numberbox',options:{precision:1}}">List
					Price</th>
				<th
					data-options="field:'unitcost',width:80,align:'right',editor:'numberbox'">Unit
					Cost</th>
				<th data-options="field:'attr1',width:250,editor:'textbox'">Attribute</th>
				<th
					data-options="field:'status',width:60,align:'center',editor:{type:'checkbox',options:{on:'P',off:''}}">Status</th>
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
    var editIndex = undefined;
    function endEditing() {
      if (editIndex == undefined) {
        return true
      }
      if ($('#dg').datagrid('validateRow', editIndex)) {
        var ed = $('#dg').datagrid('getEditor', {
          index : editIndex,
          field : 'productid'
        });
        var productname = $(ed.target).combobox('getText');
        $('#dg').datagrid('getRows')[editIndex]['productname'] = productname;
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
        return

        

      }
      $('#dg').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
      editIndex = undefined;
    }
    function accept() {
      if (endEditing()) {
        $('#dg').datagrid('acceptChanges');
      }
    }
    function reject() {
      $('#dg').datagrid('rejectChanges');
      editIndex = undefined;
    }
    function getChanges() {
      var rows = $('#dg').datagrid('getChanges');
      alert(rows.length + ' rows are changed!');
    }
  </script>
</body>
