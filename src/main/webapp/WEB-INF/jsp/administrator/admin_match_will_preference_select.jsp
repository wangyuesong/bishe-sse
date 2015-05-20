<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div style="margin: 20px 0;"></div>
<ul style="margin: 0; padding: 0; margin-left: 10px;">
	<!-- 此处硬编码 -->
	<li class="drag-item" value="w.updateTime">志愿填报时间</li>
	<li class="drag-item" value="w.student.gpa">绩点</li>
</ul>
<style type="text/css">
.drag-item {
	list-style-type: none;
	display: block;
	padding: 5px;
	border: 1px solid #ccc;
	margin: 2px;
	width: 100%;
	background: #fafafa;
	color: #444;
}

.indicator {
	position: absolute;
	font-size: 9px;
	width: 10px;
	height: 10px;
	display: none;
	color: red;
}
</style>
<script>
  $(function() {
    var indicator = $('<div class="indicator"></div>').appendTo('body');
    $('.drag-item').draggable({
      revert : true,
      deltaX : 0,
      deltaY : 0
    }).droppable({
      onDragOver : function(e, source) {
        indicator.css({
          display : 'block',
          left : $(this).offset().left - 10,
          top : $(this).offset().top + $(this).outerHeight() - 5
        });
      },
      onDragLeave : function(e, source) {
        indicator.hide();
      },
      onDrop : function(e, source) {
        $(source).insertAfter(this);
        indicator.hide();
      }
    });
  });
</script>