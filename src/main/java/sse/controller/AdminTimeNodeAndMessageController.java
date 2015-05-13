package sse.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.commandmodel.BasicJson;
import sse.commandmodel.SystemMessageFormModel;
import sse.dao.impl.TimeNodeDaoImpl.CalendarEvent;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.SystemMessageListModel;
import sse.pageModel.TimeNodeListModel;
import sse.service.impl.AdminTimenodeServiceImpl;
import sse.service.impl.DocumentSerivceImpl.SimpleAttachmentInfo;
import sse.service.impl.StudentWillServiceImpl;
import sse.service.impl.TeacherStudentServiceImpl;
import sse.utils.PaginationAndSortModel;

/**
 * @author yuesongwang
 *
 */
@Controller
@RequestMapping(value = "/admin/timenodemessage/")
public class AdminTimeNodeAndMessageController {

    @Autowired
    private AdminTimenodeServiceImpl adminTimenodeServiceImpl;

    @Autowired
    private TeacherStudentServiceImpl teacherServiceImpl;

    @Autowired
    private StudentWillServiceImpl studentServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/getCurrentTimeNodesInDatagrid")
    public GenericDataGrid<TimeNodeListModel> getCurrentTimeNodesInDatagrid(HttpServletRequest request) {
        PaginationAndSortModel pam = new PaginationAndSortModel(request);
        return adminTimenodeServiceImpl.getCurrentTimeNodesInDatagrid(pam);
    }

    @ResponseBody
    @RequestMapping(value = "/getSystemMessagesInDatagrid")
    public GenericDataGrid<SystemMessageListModel> getSystemMessagesInDatagrid(HttpServletRequest request) {
        PaginationAndSortModel pam = new PaginationAndSortModel(request);
        return adminTimenodeServiceImpl.getSystemMessagesInDatagrid(pam);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllTempAttachments")
    public List<SimpleAttachmentInfo> getAllTempAttachments(HttpServletRequest request,
            HttpServletResponse response) {
        return adminTimenodeServiceImpl.getTempAttachmentsOfAdmin();
    }

    @ResponseBody
    @RequestMapping(value = "/confirmCreateSystemMessage")
    public BasicJson confirmCreateDocument(SystemMessageFormModel model, HttpServletRequest request) {
        adminTimenodeServiceImpl.confirmCreateDocumentAndAddSystemMessageToDB(model);
        return new BasicJson(true, "发布成功", null);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteSystemMessage")
    public BasicJson deleteSystemMessage(int systemMessageId, HttpServletRequest request) {
        adminTimenodeServiceImpl.deleteSystemMessage(systemMessageId);
        return new BasicJson(true, "删除成功", null);
    }

    @ResponseBody
    @RequestMapping(value = "/changeTimeNodes")
    public BasicJson changeTimeNodes(@RequestBody List<TimeNodeListModel> models, HttpServletRequest request) {
        return adminTimenodeServiceImpl.changeTimeNodes(models);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteTimeNode")
    public BasicJson deleteTimeNode(int timeNodeId, HttpServletRequest request) {
        return adminTimenodeServiceImpl.deleteTimeNode(timeNodeId);
    }

    @ResponseBody
    @RequestMapping(value = "/getTimeNodes")
    public List<CalendarEvent> getTimeNodes(HttpServletRequest request) {
        return adminTimenodeServiceImpl.getAllTimeNodes();
    }
}
