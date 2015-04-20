<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="easyui-accordion" data-options="fit:false,border:false">

	<script type="text/javascript" charset="utf-8">
    var ctrlTreeMyTeacher;
    $(function() {
      ctrlTreeMyTeacher = $('#ctrlTreeMyTeacher').tree({
        url : '${pageContext.request.contextPath}/menu/getMenuList',
        lines : true,
        onClick : function(node) {
          addTab(centerTabs, node);
        },
        onDblClick : function(node) {
          if (node.state == 'closed') {
            $(this).tree('expand', node.target);
          } else {
            $(this).tree('collapse', node.target);
          }
        }
      });

    });
  </script>
	<div title="我的导师"
		data-options="isonCls:'icon-save',tools : [ {
				iconCls : 'icon-reload',
				handler : function() {
					ctrlTreeMyTeacher.tree('reload');
				}
			}, {
				iconCls : 'icon-redo',
				handler : function() {
					var node = ctrlTreeMyTeacher.tree('getSelected');
					if (node) {
						ctrlTreeMyTeacher.tree('expandAll', node.target);
					} else {
						ctrlTreeMyTeacher.tree('expandAll');8
					}
				}
			}, {
				iconCls : 'icon-undo',
				handler : function() {
					var node = ctrlTreeMyTeacher.tree('getSelected');
					if (node) {
						ctrlTreeMyTeacher.tree('collapseAll', node.target);
					} else {
						ctrlTreeMyTeacher.tree('collapseAll');
					}
				}
			} ]">
		<ul id="ctrlTreeMyTeacher" style="margin-top: 5px;">
		</ul>

	</div>
</div>
