package sse.controller;

import java.util.HashMap;

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
import sse.commandmodel.WillModel;
import sse.entity.User;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.TeacherListModel;
import sse.service.impl.StudentDocumentServiceImpl;
import sse.service.impl.StudentWillServiceImpl;
import sse.service.impl.StudentWillServiceImpl.TeacherDetail;

@Controller
@RequestMapping(value = "/student/will")
public class StudentWillController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StudentWillController.class);

    @Autowired
    private StudentWillServiceImpl studentWillService;

    @Autowired
    private StudentDocumentServiceImpl sutdentDocumentService;

    @ResponseBody
    @RequestMapping(value = "/getAllTeachers", method = { RequestMethod.GET, RequestMethod.POST })
    public GenericDataGrid<TeacherListModel> getAllTeachers(HttpServletRequest request)
    {
        int page = 1;
        int pageSize = 10;
        GenericDataGrid<TeacherListModel> s = studentWillService.findTeachersForPagingInGenericDataGrid(page, pageSize,
                null, "ASC");
        return s;
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