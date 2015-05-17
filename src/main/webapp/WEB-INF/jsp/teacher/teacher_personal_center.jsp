<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<h2>Basic PropertyGrid</h2>
<p>Click on row to change each property value.</p>
<div style="margin: 20px 0;">
	<a href="javascript:void(0)" class="easyui-linkbutton"
		onclick="showGroup()">ShowGroup</a> <a href="javascript:void(0)"
		class="easyui-linkbutton" onclick="hideGroup()">HideGroup</a> <a
		href="javascript:void(0)" class="easyui-linkbutton"
		onclick="showHeader()">ShowHeader</a> <a href="javascript:void(0)"
		class="easyui-linkbutton" onclick="hideHeader()">HideHeader</a> <a
		href="javascript:void(0)" class="easyui-linkbutton"
		onclick="getChanges()">GetChanges</a>
</div>
<table id="pg" class="easyui-propertygrid" style="width: 300px"
	data-options="
                url:'propertygrid_data1.json',
                method:'get',
                showGroup:true,
                scrollbarSize:0
            ">
</table>

<script type="text/javascript">
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
  function getChanges() {
    var s = '';
    var rows = $('#pg').propertygrid('getChanges');
    for (var i = 0; i < rows.length; i++) {
      s += rows[i].name + ':' + rows[i].value + ',';
    }
    alert(s)
  }
</script>