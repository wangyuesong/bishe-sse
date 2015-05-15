package sse.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.commandmodel.BasicJson;
import sse.commandmodel.SystemMessageFormModel;
import sse.pagemodel.SystemMessageListModel;
import sse.pagemodel.GenericDataGrid;
import sse.pagemodel.AccessRuleListModel;
import sse.pagemodel.TimeNodeListModel;
import sse.dao.impl.TimeNodeDaoImpl.CalendarEvent;
import sse.service.impl.AccessRuleServiceImpl;
import sse.service.impl.AdminTimenodeServiceImpl;
import sse.service.impl.DocumentSerivceImpl.SimpleAttachmentInfo;
import sse.service.impl.StudentWillServiceImpl;
import sse.service.impl.TeacherStudentServiceImpl;
import sse.utils.AccessRulePropertiesUtil;
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
    private AccessRuleServiceImpl accessRuleServiceImpl;

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
        BasicJson returnJson = adminTimenodeServiceImpl.changeTimeNodes(models);
        accessRuleServiceImpl.refreshPermission();
        return returnJson;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteTimeNode")
    public BasicJson deleteTimeNode(int timeNodeId, HttpServletRequest request) {
        BasicJson returnJson = adminTimenodeServiceImpl.deleteTimeNode(timeNodeId);
        accessRuleServiceImpl.refreshPermission();
        return returnJson;
    }

    @ResponseBody
    @RequestMapping(value = "/getTimeNodes")
    public List<CalendarEvent> getTimeNodes(HttpServletRequest request) {
        return adminTimenodeServiceImpl.getAllTimeNodes();
    }

    @ResponseBody
    @RequestMapping(value = "/getAllStudentAccessRules")
    public List<AccessRuleListModel> getAllStudentAccessRules(HttpServletRequest request) {
        List<AccessRuleListModel> models = new ArrayList<AccessRuleListModel>();
        Map<String, String> maps = AccessRulePropertiesUtil.getStudentAccessRuleNameAndURLMapping();
        for (String key : maps.keySet())
        {
            models.add(new AccessRuleListModel(key, maps.get(key)));
        }
        return models;
    }

    @ResponseBody
    @RequestMapping(value = "/getAllTeacherAccessRules")
    public List<AccessRuleListModel> getAllTeacherAccessRules(HttpServletRequest request) {
        List<AccessRuleListModel> models = new ArrayList<AccessRuleListModel>();
        Map<String, String> maps = AccessRulePropertiesUtil.getTeacherAccessRuleNameAndURLMapping();
        for (String key : maps.keySet())
        {
            models.add(new AccessRuleListModel(key, maps.get(key)));
        }
        return models;
    }

    @ResponseBody
    @RequestMapping(value = "/getAccessRulesByTimeNodeIdAndRole")
    public List<AccessRuleListModel> getStudentAccessRulesByTimeNodeIdAndRole(int timeNodeId, String role,
            HttpServletRequest request) {
        return adminTimenodeServiceImpl.getStudentAccessRulesByTimeNodeId(timeNodeId, role);
    }

    @ResponseBody
    @RequestMapping(value = "/saveAccessRules")
    public BasicJson saveAccessRules(@RequestBody UpdateRuleModel model,
            HttpServletRequest request) {
        adminTimenodeServiceImpl.updateAccessRules(Integer.parseInt(model.getTimeNodeId()), model.getTeacherRules(),
                model.getStudentRules());
        accessRuleServiceImpl.refreshPermission();
        return new BasicJson(true, "更新成功", null);
    }

    @ResponseBody
    @RequestMapping(value = "/getBannedAccessRulesByTimeNodeId")
    public List<AccessRuleListModel> getBannedAccessRulesByTimeNodeId(int timeNodeId, HttpServletRequest request) {
        List<AccessRuleListModel> models = adminTimenodeServiceImpl.getBannedAccessRulesByTimeNodeId(timeNodeId);
        return models;
    }

    public static class UpdateRuleModel
    {
        ArrayList<String> teacherRules;
        ArrayList<String> studentRules;
        String timeNodeId;

        public ArrayList<String> getTeacherRules() {
            return teacherRules;
        }

        public void setTeacherRules(ArrayList<String> teacherRules) {
            this.teacherRules = teacherRules;
        }

        public ArrayList<String> getStudentRules() {
            return studentRules;
        }

        public void setStudentRules(ArrayList<String> studentRules) {
            this.studentRules = studentRules;
        }

        public String getTimeNodeId() {
            return timeNodeId;
        }

        public void setTimeNodeId(String timeNodeId) {
            this.timeNodeId = timeNodeId;
        }

    }
}
