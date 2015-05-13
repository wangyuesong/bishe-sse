package sse.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sse.commandmodel.BasicJson;
import sse.commandmodel.SystemMessageFormModel;
import sse.commandmodel.TimeNodeFormModel;
import sse.exception.SSEException;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.SystemMessageListModel;
import sse.service.impl.AdminTimenodeServiceImpl;
import sse.service.impl.DocumentSerivceImpl.AttachmentInfo;
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
    @RequestMapping(value = "/updateTimeNodes")
    public BasicJson updateTimeNodes(TimeNodeFormModel model,
            HttpServletRequest request) {
        adminTimenodeServiceImpl.updateTimeNodes(model);
        return new BasicJson(true, "已更新", null);
    }

    @ResponseBody
    @RequestMapping(value = "/getCurrentTimeNodes")
    public TimeNodeFormModel getCurrentTimeNodes(HttpServletRequest request) {
        return adminTimenodeServiceImpl.getCurrentTimeNodes();
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

}
