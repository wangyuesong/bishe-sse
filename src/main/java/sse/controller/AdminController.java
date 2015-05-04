package sse.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.commandmodel.BasicJson;
import sse.commandmodel.MatchPair;
import sse.pageModel.TeacherSelectModel;
import sse.service.impl.AdminServiceImpl;
import sse.service.impl.StudentServiceImpl;
import sse.service.impl.TeacherServiceImpl;

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
    private TeacherServiceImpl teacherServiceImpl;

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/getCurrentMatchCondition", method = { RequestMethod.GET, RequestMethod.POST })
    public List<MatchPair> getCurrentMatchCondition(HttpServletRequest request, HttpServletResponse response) {
        return studentServiceImpl.findCurrentMatchConditions();
    }

    @ResponseBody
    @RequestMapping(value = "/findTeacherAccountById", method = { RequestMethod.GET })
    public String findTeacherAccountById(HttpServletRequest request, HttpServletResponse response) {
        return adminServiceImpl.findTeacherAccountById(Integer.parseInt(request.getParameter("teacherId")));
    }

    @ResponseBody
    @RequestMapping(value = "/doMatch", method = { RequestMethod.GET })
    public void doMatch(HttpServletRequest request, HttpServletResponse response) {
        List<MatchPair> matchPairs = adminServiceImpl.doMatch();
    }

    /**
     * @Method: updateMatchPairs
     * @Description: 更新教师和学生之间的关系，在更新之前，首先检查是否有超员（教师容量小于选择人数），如果没有，则按照传入参数进行分配，否则返回错误
     * @param @param request
     * @param @param response
     * @param @param matchPairs
     * @param @return
     * @return BasicJson
     * @throws
     */
    @ResponseBody
    @RequestMapping(value = "/updateMatchPairs", method = { RequestMethod.POST })
    public BasicJson updateMatchPairs(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ArrayList<MatchPair> matchPairs) {
        BasicJson bj = teacherServiceImpl.doCapacityCheck(matchPairs);
        if (!bj.isSuccess())
            return bj;
        else
            teacherServiceImpl.updateRelationshipBetweenTeacherAndStudent(matchPairs);
        // for (MatchPair matchPair : matchPairs)
        // studentServiceImpl.createNewRelationshipBetweenStudentAndTeacher(matchPair.getStudentId(),
        // matchPair.getTeacherId());
        return new BasicJson();
    }

    @ResponseBody
    @RequestMapping(value = "/getAllTeachers", method = { RequestMethod.GET, RequestMethod.POST })
    public List<TeacherSelectModel> getAllTeachers(HttpServletRequest request)
    {
        return adminServiceImpl.findAllTeachersInSelectModelList();
    }

    /**
     * @Method: doCapacityCheck
     * @Description: 每次用户进行一次手动分配，系统都会检查一下教师是否超员
     * @param @param rows
     * @param @return
     * @return BasicJson
     * @throws
     */
    @ResponseBody
    @RequestMapping(value = "/doCapacityCheck", method = { RequestMethod.GET, RequestMethod.POST })
    public BasicJson doCapacityCheck(@RequestBody ArrayList<MatchPair> rows)
    {
        BasicJson bj = teacherServiceImpl.doCapacityCheck(rows);
        return bj;
    }
}
