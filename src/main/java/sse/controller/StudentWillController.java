package sse.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.commandmodel.BasicJson;
import sse.commandmodel.PaginationAndSortModel;
import sse.commandmodel.WillModel;
import sse.entity.User;
import sse.pagemodel.ActionEventListModel;
import sse.pagemodel.GenericDataGrid;
import sse.pagemodel.TeacherListModel;
import sse.service.impl.DocumentSerivceImpl;
import sse.service.impl.StudentWillServiceImpl;
import sse.service.impl.StudentWillServiceImpl.TeacherDetail;

@Controller
@RequestMapping(value = "/student/will")
public class StudentWillController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StudentWillController.class);

    @Autowired
    private StudentWillServiceImpl studentWillService;

    @Autowired
    private DocumentSerivceImpl sutdentDocumentService;

    @ResponseBody
    @RequestMapping(value = "/getTeachersInDatagrid", method = { RequestMethod.GET, RequestMethod.POST })
    public GenericDataGrid<TeacherListModel> getTeachersInDatagrid(HttpServletRequest request)
    {
        PaginationAndSortModel pam = new PaginationAndSortModel(request);
        GenericDataGrid<TeacherListModel> s = studentWillService.findTeachersForPagingInGenericDataGrid(pam);
        return s;
    }

    @ResponseBody
    @RequestMapping(value = "/getTeacherActionEventsInDatagrid", method = { RequestMethod.GET, RequestMethod.POST })
    public GenericDataGrid<ActionEventListModel> getTeacherActionEventsInDatagrid(String teacherId,
            HttpServletRequest request)
    {
        PaginationAndSortModel pam = new PaginationAndSortModel(request);
        GenericDataGrid<ActionEventListModel> s = studentWillService.findTeachersActionEventsForPagingInGenericDataGrid(
                Integer.parseInt(teacherId),
                ((User) request.getSession().getAttribute("USER")).getId(),
                pam);
        return s;
    }

    @ResponseBody
    @RequestMapping(value = "/getAllTeachersForSelect", method = { RequestMethod.GET, RequestMethod.POST })
    public List<TeacherListModel> getAllTeachersForSelect(HttpServletRequest request)
    {
        return studentWillService.findTeachersForList();
    }

    @ResponseBody
    @RequestMapping(value = "/getPreviousSelection", method = { RequestMethod.GET })
    public HashMap<String, String> getPreviousSelection(HttpServletRequest request)
    {
        int studentId = ((User) (request.getSession().getAttribute("USER"))).getId();
        HashMap<String, String> returnMap = studentWillService.findPreviousWillsInHashMap(studentId);
        return CollectionUtils.isEmpty(returnMap) ? new HashMap<String, String>() : returnMap;
    }

    @ResponseBody
    @RequestMapping(value = "/showOneTeacherDetail", method = { RequestMethod.GET })
    public TeacherDetail showOneTeacherDetail(String teacherId)
    {
        return studentWillService.findOneTeacherDetailByTeacherIdInTeacherDetail(Integer.parseInt(teacherId));
    }

    @ResponseBody
    @RequestMapping(value = "/saveSelection", method = { RequestMethod.POST })
    public BasicJson saveSelection(@ModelAttribute WillModel willModel, HttpServletRequest request)
    {
        User student = ((User) (request.getSession().getAttribute("USER")));
        willModel.setStudentAccount(student.getAccount());
        willModel.setStudentId(student.getId() + "");
        willModel.setStudentName(student.getName());
        studentWillService.updateSelection(willModel);
        return new BasicJson(true, "已经更新您的志愿", null);
    }

}