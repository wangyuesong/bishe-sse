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
import sse.commandmodel.WillModel;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.TeacherSelectModel;
import sse.service.impl.AdminWillServiceImpl;
import sse.service.impl.StudentWillServiceImpl;
import sse.service.impl.TeacherServiceImpl;
import sse.utils.PaginationAndSortModel;

/**
 * @author yuesongwang
 *
 */
@Controller
@RequestMapping(value = "/admin/will/")
public class AdminWillController {

    @Autowired
    public AdminWillServiceImpl adminWillServiceImpl;

    @Autowired
    private TeacherServiceImpl teacherServiceImpl;

    @Autowired
    private StudentWillServiceImpl studentServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/getCurrentMatchConditionInDatagrid", method = { RequestMethod.GET, RequestMethod.POST })
    public GenericDataGrid<MatchPair> getCurrentMatchConditionInDatagrid(HttpServletRequest request,
            HttpServletResponse response) {
        PaginationAndSortModel pam = new PaginationAndSortModel(request);
        return adminWillServiceImpl.findCurrentMatchConditionsForDatagrid(pam);
    }

    @ResponseBody
    @RequestMapping(value = "/exportCurrentMatchConditionInExcel", method = { RequestMethod.GET, RequestMethod.POST })
    public void exportCurrentMatchConditionInExcel(HttpServletRequest request,
            HttpServletResponse response) {
        adminWillServiceImpl.exportCurrentMatchConditionInExcel();
    }

    @ResponseBody
    @RequestMapping(value = "/getWillListInDatagrid", method = { RequestMethod.GET, RequestMethod.POST })
    public GenericDataGrid<WillModel> getWillListInDatagrid(HttpServletRequest request, HttpServletResponse response) {
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        return adminWillServiceImpl.getWillListForDatagrid(Integer.parseInt(page), Integer.parseInt(rows));
    }

    @ResponseBody
    @RequestMapping(value = "/findTeacherAccountById", method = { RequestMethod.GET })
    public String findTeacherAccountById(HttpServletRequest request, HttpServletResponse response) {
        return adminWillServiceImpl.findTeacherAccountById(Integer.parseInt(request.getParameter("teacherId")));
    }

    @ResponseBody
    @RequestMapping(value = "/doMatch", method = { RequestMethod.GET })
    public void doMatch(HttpServletRequest request, HttpServletResponse response) {
        List<MatchPair> matchPairs = adminWillServiceImpl.doMatch();
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
        return new BasicJson(true, "更新成功", null);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllTeachers", method = { RequestMethod.GET, RequestMethod.POST })
    public List<TeacherSelectModel> getAllTeachers(HttpServletRequest request)
    {
        return adminWillServiceImpl.findAllTeachersInSelectModelList();
    }

    @ResponseBody
    @RequestMapping(value = "/systemAssign", method = { RequestMethod.GET, RequestMethod.POST })
    public List<MatchPair> systemAssign(HttpServletRequest request, HttpServletResponse response) {
        List<MatchPair> matchPairs = adminWillServiceImpl.doMatch();
        return matchPairs;
    }

    @ResponseBody
    @RequestMapping(value = "/updateWills", method = { RequestMethod.GET, RequestMethod.POST })
    public BasicJson updateWills(@RequestBody ArrayList<WillModel> wills) {

        adminWillServiceImpl.updateWills(wills);
        return new BasicJson(true, "更新成功", null);
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
