package sse.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.entity.Teacher;
import sse.jsonmodel.TeacherListModel;
import sse.jsonmodel.TeacherSelectModel;
import sse.pageModel.DataGrid;
import sse.service.impl.AdminServiceImpl;
import sse.service.impl.AdminServiceImpl.MatchPair;
import sse.service.impl.StudentServiceImpl;

/**
 * @author yuesongwang
 *
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    public AdminServiceImpl adminServiceImpl;

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/getCurrentMatchCondition", method = { RequestMethod.GET, RequestMethod.POST })
    public List<MatchPair> getCurrentMatchCondition(HttpServletRequest request, HttpServletResponse response) {
        return studentServiceImpl.findCurrentMatchCondition();
    }

    @ResponseBody
    @RequestMapping(value = "/doMatch", method = { RequestMethod.GET })
    public void doMatch(HttpServletRequest request, HttpServletResponse response) {
        List<MatchPair> matchPairs = adminServiceImpl.doMatch();
    }

    @ResponseBody
    @RequestMapping(value = "/getAllTeachers", method = { RequestMethod.GET, RequestMethod.POST })
    public List<TeacherSelectModel> getAllTeachers(HttpServletRequest request)
    {
        return adminServiceImpl.findAllTeachersForSelect();
    }
}
