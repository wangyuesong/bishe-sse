package sse.controller;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

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
import sse.service.impl.AccessRuleServiceImpl;

@Controller
public class AjaxRenderViewController {

    @Autowired
    TimeNodeDaoImpl timeNodeDaoImpl;

    @Autowired
    AccessRuleServiceImpl accessRuleServiceImpl;

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
    @RequestMapping(value = "/refreshPermission", method = { RequestMethod.GET })
    public void refreshPermission()
    {
        accessRuleServiceImpl.refreshPermission();
    }

    @RequestMapping(value = "/dispatch/{url}/{url2}", method = { RequestMethod.GET })
    public ModelAndView doRedirect2(
            @PathVariable(value = "url") String redirectUrl,
            @PathVariable(value = "url2") String redirectUrl2)
    {
        boolean result = true;
        if (StringUtils.equals(redirectUrl, "student"))
            result = accessRuleServiceImpl.doStudentAccessCheck(redirectUrl2);
        else if (StringUtils.equals(redirectUrl, "teacher"))
            result = accessRuleServiceImpl.doTeacherAccessCheck(redirectUrl2);
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
