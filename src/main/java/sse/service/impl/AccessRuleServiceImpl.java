package sse.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.dao.impl.AccessRuleDaoImpl;
import sse.dao.impl.TimeNodeDaoImpl;
import sse.entity.AccessRule;
import sse.entity.TimeNode;
import sse.permission.PermissionCheckEntity;

/**
 * @Project: sse
 * @Title: AccessRuleServiceImpl.java
 * @Package sse.service.impl
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月15日 下午1:51:44
 * @version V1.0
 */
@Service
public class AccessRuleServiceImpl {

    @Autowired
    TimeNodeDaoImpl timeNodeDaoImpl;

    @Autowired
    AccessRuleDaoImpl accessRuleDaoImpl;

    List<PermissionCheckEntity> studentBannedPermissions;

    List<PermissionCheckEntity> teacherBannedPermissions;

    //容器启动时，自动调用该方法！初始化权限表
    @PostConstruct
    @SuppressWarnings("unchecked")
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
    
    public boolean doStudentAccessCheck(String requestUrl)
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

    public boolean doTeacherAccessCheck(String requestUrl)
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
}
