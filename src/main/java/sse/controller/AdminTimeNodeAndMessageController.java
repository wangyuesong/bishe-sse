package sse.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.commandmodel.BasicJson;
import sse.commandmodel.TimeNodeFormModel;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.SystemMessageListModel;
import sse.service.impl.AdminTimenodeServiceImpl;
import sse.service.impl.AdminWillServiceImpl;
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
    public AdminWillServiceImpl adminWillServiceImpl;

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
}
