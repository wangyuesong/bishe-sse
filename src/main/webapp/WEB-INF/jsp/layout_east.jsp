<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<body>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/jquery.eventCalendar.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/moment.js"></script>

	<div id="calendar"></div>
	<script>
    $(function() {
      $("#calendar").eventCalendar({
        eventsjson : '${pageContext.request.contextPath}/admin/timenodemessage/getTimeNodes',
        monthNames : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" ],
        dayNames : [ '周一', '周二', '周三', '周四', '周五', '周六', '周日' ],
        dayNamesShort : [ '一', '二', '三', '四', '五', '六', '日' ],
        txt_noEvents : "无事件",
        txt_SpecificEvents_prev : "",
        txt_SpecificEvents_after : "事件:",
        txt_next : "下一个",
        txt_prev : "前一个",
        txt_NextEvents : "下一个事件",
        txt_GoToEventUrl : "到事件"
      })
    });
  </script>
</body>
