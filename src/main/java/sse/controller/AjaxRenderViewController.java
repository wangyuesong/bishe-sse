package sse.controller;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import sse.dao.impl.AccessRuleDaoImpl;
import sse.dao.impl.TimeNodeDaoImpl;
import sse.entity.AccessRule;
import sse.entity.TimeNode;
import sse.permission.PermissionCheckEntity;
import sse.utils.AccessRulePropertiesUtil;

@Controller
public class AjaxRenderViewController {

    @Autowired
    TimeNodeDaoImpl timeNodeDaoImpl;

    @Autowired
    AccessRuleDaoImpl accessRuleDaoImpl;

    List<PermissionCheckEntity> studentBannedPermissions;

    List<PermissionCheckEntity> teacherBannedPermissions;

    /**
     * Description: 刷新系统内存中的准入规则黑名单
     * 步骤：1.找出所有时间节点
     * 对于每一个时间节点：
     * 1.创建一个studentPce，一个teacherPce，代表两个权限检查体，设置权限检查体的key为该时间节点
     * 2.找到时间节点对应的AccessRule们（如果存在的话）
     * 对于每一个AccessRule，根据其Role（student，teacher）将其加入对应PermissionCheckEntity的bannedUrls中
     * 3.对于studentPce和teacherPce,如果bannedUrls不是空的话，则将这个Pce加入内存中的准入规则黑名单中（studentBannedPermissions，
     * teacherBannedPermissions）
     * void
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/refreshPermission", method = { RequestMethod.GET })
    public void refreshPermission()
    {
        studentBannedPermissions = new LinkedList<PermissionCheckEntity>();
        teacherBannedPermissions = new LinkedList<PermissionCheckEntity>();
        List<TimeNode> timeNodes = timeNodeDaoImpl.findAll();
        for (TimeNode t : timeNodes)
        {
            List<AccessRule> rules = t.getAccessRules();
            PermissionCheckEntity studentPce = new PermissionCheckEntity();
            PermissionCheckEntity teacherPce = new PermissionCheckEntity();
            // 设置时间节点
            studentPce.setTimeNode(t);
            teacherPce.setTimeNode(t);
            // 对于该时间节点的每个禁止URL
            for (AccessRule r : rules)
            {
                if (StringUtils.equals(r.getRole(), "Student"))
                    studentPce.getBannedUrls().add(r.getUrl());
                else if (StringUtils.equals(r.getRole(), "Teacher"))
                    teacherPce.getBannedUrls().add(r.getUrl());
            }
            studentBannedPermissions.add(studentPce);
            teacherBannedPermissions.add(teacherPce);
        }
        Collections.sort(studentBannedPermissions);
        Collections.sort(teacherBannedPermissions);
    }

    private boolean doStudentAccessCheck(String requestUrl)
    {
        boolean result = true;
        Date currentTime = new Date();
        for (int i = 0; i < studentBannedPermissions.size(); i++)
        {
            PermissionCheckEntity currentPce;
            PermissionCheckEntity nextPce;
            if (i + 1 == studentBannedPermissions.size()) // 已经检查到最后一个PCE
            {
                currentPce = studentBannedPermissions.get(i);
                // 如果最后一个PCE比当前时间还晚，证明系统还未开放，所有权限都会被禁止
                if (currentPce.getTimeNode().getTime().compareTo(currentTime) > 0)
                    return false;
                // 当前时间处于最后一个PCE影响时段,则进行最后一个PCE检查
                else
                    return currentPce.doPermissionCheck(requestUrl);
            }
            else {
                currentPce = studentBannedPermissions.get(i);
                nextPce = studentBannedPermissions.get(i + 1);
                // 当前时间处在当前PCE和下一个PCE影响时段之间，则做当前PCE检查
                if (currentPce.getTimeNode().getTime().compareTo(currentTime) < 0
                        && nextPce.getTimeNode().getTime().compareTo(currentTime) > 0)
                    return currentPce.doPermissionCheck(requestUrl);
            }
        }
        return result;
    }
    private boolean doTeacherAccessCheck(String requestUrl)
    {
        boolean result = true;
        Date currentTime = new Date();
        for (int i = 0; i < teacherBannedPermissions.size(); i++)
        {
            PermissionCheckEntity currentPce;
            PermissionCheckEntity nextPce;
            if (i + 1 == teacherBannedPermissions.size()) // 已经检查到最后一个PCE
            {
                currentPce = teacherBannedPermissions.get(i);
                // 如果最后一个PCE比当前时间还晚，证明系统还未开放，所有权限都会被禁止
                if (currentPce.getTimeNode().getTime().compareTo(currentTime) > 0)
                    return false;
                // 当前时间处于最后一个PCE影响时段,则进行最后一个PCE检查
                else
                    return currentPce.doPermissionCheck(requestUrl);
            }
            else {
                currentPce = teacherBannedPermissions.get(i);
                nextPce = teacherBannedPermissions.get(i + 1);
                // 当前时间处在当前PCE和下一个PCE影响时段之间，则做当前PCE检查
                if (currentPce.getTimeNode().getTime().compareTo(currentTime) < 0
                        && nextPce.getTimeNode().getTime().compareTo(currentTime) > 0)
                    return currentPce.doPermissionCheck(requestUrl);
            }
        }
        return result;
    }

    @RequestMapping(value = "/dispatch/{url}/{url2}", method = { RequestMethod.GET })
    public ModelAndView doRedirect2(
            @PathVariable(value = "url") String redirectUrl,
            @PathVariable(value = "url2") String redirectUrl2)
    {
        boolean result = true;
        if (StringUtils.equals(redirectUrl, "student"))
            result = doStudentAccessCheck(redirectUrl2);
        else if (StringUtils.equals(redirectUrl, "teacher"))
            result = doTeacherAccessCheck(redirectUrl2);
        if (result)
            return new ModelAndView(redirectUrl + "/" + redirectUrl2);
        else
            return new ModelAndView("unauthorized");
    }

    @RequestMapping(value = "/dispatch/{url}", method = { RequestMethod.GET })
    public ModelAndView doRedirect(@PathVariable(value = "url") String redirectUrl)
    {
        return new ModelAndView(redirectUrl);
    }
}
