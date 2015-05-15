package sse.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.commandmodel.BasicJson;
import sse.pagemodel.GenericDataGrid;
import sse.pagemodel.StudentCURDModel;
import sse.pagemodel.TeacherCURDModel;
import sse.pagemodel.TimeNodeListModel;
import sse.service.impl.AdminUserServiceImpl;
import sse.utils.PaginationAndSortModel;

/**
 * @author yuesongwang
 *
 */
@Controller
@RequestMapping(value = "/admin/user/")
public class AdminUserController {

    @Autowired
    private AdminUserServiceImpl adminUserServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/getAllStudents", method = { RequestMethod.GET, RequestMethod.POST })
    public GenericDataGrid<StudentCURDModel> getAllStudents(HttpServletRequest request) {
        return adminUserServiceImpl.findAllStudents(new PaginationAndSortModel(request));
    }

    @ResponseBody
    @RequestMapping(value = "/changeStudents", method = { RequestMethod.GET, RequestMethod.POST })
    public BasicJson changeStudents(@RequestBody List<StudentCURDModel> models, HttpServletRequest request) {
        return adminUserServiceImpl.changeStudents(models);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteStudent")
    public BasicJson deleteStudent(int studentId, HttpServletRequest request) {
        return adminUserServiceImpl.deleteStudent(studentId);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllTeachers", method = { RequestMethod.GET, RequestMethod.POST })
    public GenericDataGrid<TeacherCURDModel> getAllTeachers(HttpServletRequest request) {
        return adminUserServiceImpl.findAllTeachers(new PaginationAndSortModel(request));
    }

    @ResponseBody
    @RequestMapping(value = "/changeTeachers", method = { RequestMethod.GET, RequestMethod.POST })
    public BasicJson changeTeachers(@RequestBody List<TeacherCURDModel> models, HttpServletRequest request) {
        return adminUserServiceImpl.changeTeachers(models);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteTeacher")
    public BasicJson deleteTeacher(int teacherId, HttpServletRequest request) {
        return adminUserServiceImpl.deleteTeacher(teacherId);
    }
}
