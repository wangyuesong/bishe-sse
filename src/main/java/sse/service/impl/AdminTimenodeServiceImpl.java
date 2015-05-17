package sse.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.commandmodel.BasicJson;
import sse.commandmodel.SystemMessageFormModel;
import sse.dao.impl.AccessRuleDaoImpl;
import sse.dao.impl.AttachmentDaoImpl;
import sse.dao.impl.SystemMessageDaoImpl;
import sse.dao.impl.TimeNodeDaoImpl;
import sse.dao.impl.TimeNodeDaoImpl.CalendarEvent;
import sse.entity.AccessRule;
import sse.entity.Attachment;
import sse.entity.SystemMessage;
import sse.entity.TimeNode;
import sse.enums.AttachmentStatusEnum;
import sse.enums.TimeNodeTypeEnum;
import sse.exception.SSEException;
import sse.pagemodel.AccessRuleListModel;
import sse.pagemodel.GenericDataGrid;
import sse.pagemodel.SystemMessageListModel;
import sse.pagemodel.TimeNodeListModel;
import sse.service.impl.DocumentSerivceImpl.SimpleAttachmentInfo;
import sse.utils.AccessRulePropertiesUtil;
import sse.utils.PaginationAndSortModel;

/**
 * @Project: sse
 * @Title: AdminWillServiceImpl.java
 * @Package sse.service.impl
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月8日 上午11:16:22
 * @version V1.0
 */
@Service
public class AdminTimenodeServiceImpl {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AdminTimenodeServiceImpl.class);

    @Autowired
    private TimeNodeDaoImpl timeNodeDaoImpl;

    @Autowired
    private AttachmentDaoImpl attachmentDaoImpl;

    @Autowired
    private AccessRuleDaoImpl accessRuleDaoImpl;

    @Autowired
    private SystemMessageDaoImpl systemMessageDaoImpl;

    /**
     * Description: 获得所有时间节点
     * 
     * @return
     *         TimeNodeFormModel
     */
    public GenericDataGrid<TimeNodeListModel> getCurrentTimeNodesInDatagrid(PaginationAndSortModel pam) {
        List<TimeNode> timeNodes = timeNodeDaoImpl.findForPaging("select t from TimeNode t", null,
                pam.getPage(), pam.getRows(), "t." + pam.getSort(), pam.getOrder());
        List<TimeNodeListModel> models = new ArrayList<TimeNodeListModel>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        for (TimeNode t : timeNodes)
        {
            models.add(new TimeNodeListModel(t.getId(), t.getName(), t.getDescription(), sdf.format(t.getTime()), t
                    .getType().getValue()));
        }
        int count = timeNodeDaoImpl.findAllForCount();
        return new GenericDataGrid<TimeNodeListModel>(count, models);
    }

    /**
     * Description: 返回所有SystemMessage
     * 
     * @param pam
     * @return
     *         GenericDataGrid<SystemMessage>
     */
    public GenericDataGrid<SystemMessageListModel> getSystemMessagesInDatagrid(PaginationAndSortModel pam)
    {
        List<SystemMessage> messages = systemMessageDaoImpl.findForPaging("select s from SystemMessage s", null,
                pam.getPage(), pam.getRows(), pam.getSort(), pam.getOrder());
        List<SystemMessageListModel> messageModels = new ArrayList<SystemMessageListModel>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SystemMessage s : messages)
            messageModels.add(new SystemMessageListModel(s.getId(), s.getTitle(), s.getContent(), sdf.format(s
                    .getCreateTime())));
        int count = systemMessageDaoImpl.findForCount("select s from SystemMessage s", null);
        return new GenericDataGrid<SystemMessageListModel>(count, messageModels);
    }

    public void confirmCreateDocumentAndAddSystemMessageToDB(SystemMessageFormModel formModel) {
        SystemMessage message = new SystemMessage();
        message.setContent(formModel.getContent());
        message.setTitle(formModel.getTitle());
        // Firstly find all the temp attachment belongs to this user
        systemMessageDaoImpl.persistWithTransaction(message);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("role", "Administrator");
        params.put("status", AttachmentStatusEnum.TEMP);
        List<Attachment> tempAttachmentList = attachmentDaoImpl
                .findForPaging("select a from Attachment a where a.creator.role=:role and a.status=:status", params);
        if (tempAttachmentList != null)
            for (Attachment attachment : tempAttachmentList)
            {
                // Change the status of attachment to FOREVER
                attachment.setStatus(AttachmentStatusEnum.FOREVER);
                message.addAttachment(attachment);
            }
        systemMessageDaoImpl.mergeWithTransaction(message);
    }

    public void deleteSystemMessage(int messageId)
    {
        systemMessageDaoImpl.removeWithTransaction(systemMessageDaoImpl.findById(messageId));
    }

    /**
     * Description: 更改TimeNodes,包含新增和更新
     * 
     * @param models
     *            void
     */
    public BasicJson changeTimeNodes(List<TimeNodeListModel> models) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        for (TimeNodeListModel model : models)
        {
            TimeNode t;
            // 新建
            if (model.getId() == 0)
                t = new TimeNode();
            else
                t = timeNodeDaoImpl.findById(model.getId());
            t.setType(TimeNodeTypeEnum.getType(model.getType()));
            t.setName(model.getName());
            try {
                t.setTime(sdf.parse(model.getTime()));
            } catch (ParseException e) {
                throw new SSEException("时间格式不对", e);
            }
            t.setDescription(model.getDescription());
            timeNodeDaoImpl.mergeWithTransaction(t);
        }
        return new BasicJson(true, "保存成功", null);
    }

    public BasicJson deleteTimeNode(int timeNodeId)
    {
        timeNodeDaoImpl.removeWithTransaction(timeNodeDaoImpl.findById(timeNodeId));
        return new BasicJson(true, "删除成功", null);
    }

    /**
     * Description: 右侧日历获取所有时间节点
     * 
     * @return
     *         List<CalendarEvent>
     */
    public List<CalendarEvent> getAllTimeNodes()
    {
        return timeNodeDaoImpl.getAllTimeNodes();
    }

    /**
     * Description: 根据时间节点的ID获取所有的其对应的被禁止的准入规则
     * 
     * @param timeNodeId
     *            void
     */
    public List<AccessRuleListModel> getBannedAccessRulesByTimeNodeId(int timeNodeId) {
        List<AccessRule> accessRules = timeNodeDaoImpl.findById(timeNodeId).getAccessRules();
        List<AccessRuleListModel> models = new ArrayList<AccessRuleListModel>();
        for (AccessRule a : accessRules)
        {
            models.add(new AccessRuleListModel(a.getUrl(), a.getMenuName()));
        }
        return models;
    }

    /**
     * Description: TODO
     * 
     * @param timeNodeId
     * @return
     *         List<AccessRuleListModel>
     */
    public List<AccessRuleListModel> getStudentAccessRulesByTimeNodeId(int timeNodeId, String role) {
        Map<String, String> maps;
        if (StringUtils.equals(role, "Student"))
            maps = AccessRulePropertiesUtil.getStudentAccessRuleNameAndURLMapping();
        else
            maps = AccessRulePropertiesUtil.getTeacherAccessRuleNameAndURLMapping();
        List<AccessRuleListModel> models = new ArrayList<AccessRuleListModel>();
        for (String k : maps.keySet())
        {
            models.add(new AccessRuleListModel(k, maps.get(k), false));
        }
        List<AccessRule> totalBannedRules = timeNodeDaoImpl.findById(timeNodeId).getAccessRules();
        // 实际对应某个role的bannedRules
        List<AccessRule> targetRoleBannedRules = new ArrayList<AccessRule>();
        for (AccessRule a : totalBannedRules)
        {
            if (StringUtils.equals(role, "Student"))
                if (StringUtils.equals(a.getRole(), "Student"))
                    targetRoleBannedRules.add(a);
            if (StringUtils.equals(role, "Teacher"))
                if (StringUtils.equals(a.getRole(), "Teacher"))
                    targetRoleBannedRules.add(a);
        }
        List<AccessRuleListModel> returnModels = new ArrayList<AccessRuleListModel>();
        Set<String> keySets = maps.keySet();
        for (String key : keySets)
        {
            AccessRuleListModel model = new AccessRuleListModel(key, maps.get(key));
            // 对于所有AccessRuleURLMappings，进行匹配检查，如果发现
            for (AccessRule eachBannedRule : targetRoleBannedRules)
            {
                if (StringUtils.equals(model.getUrl(), eachBannedRule.getUrl())) {
                    model.setBanned(true);
                    break;
                }
            }
            returnModels.add(model);
        }
        return returnModels;
    }

    /**
     * Description: TODO
     * 
     * @param timeNodeId
     * @param teacherRules
     * @param studentRules
     *            void
     */
    public void updateAccessRules(int timeNodeId, List<String> teacherRules, List<String> studentRules) {
        TimeNode t = timeNodeDaoImpl.findById(timeNodeId);
        // 解除这个时间节点和所有AccessRule的关联
        List<AccessRule> relatedRules = t.getAccessRules();
        for (AccessRule r : relatedRules)
        {
            r.getTimeNodes().remove(t);
            accessRuleDaoImpl.mergeWithTransaction(r);
        }
        t.setAccessRules(new LinkedList<AccessRule>());
        timeNodeDaoImpl.mergeWithTransaction(t);
        timeNodeDaoImpl.refresh(t);
        for (String teacherRuleUrl : teacherRules)
        {
            String teacherRuleName = AccessRulePropertiesUtil.readProperty(teacherRuleUrl, "Teacher");
            AccessRule previousRule = accessRuleDaoImpl.findAccessRuleByUrlAndRole(teacherRuleUrl, "Teacher");
            // 以前不存在这个AccesssRule则建立关系，
            if (previousRule == null) {
                t.addAccessRule(new AccessRule(teacherRuleUrl, teacherRuleName, "Teacher"));
                // 建立关系
                timeNodeDaoImpl.mergeWithTransaction(t);
            }
            // 以前存在这个Rule
            else
            {
                // 以前这个Rule和时间节点没关系,则建立；有关系则什么都不做
                if (!previousRule.getTimeNodes().contains(t)) {
                    t.addAccessRule(previousRule);
                    timeNodeDaoImpl.mergeWithTransaction(t);
                }
            }
        }

        for (String teacherRuleUrl : studentRules)
        {
            String teacherRuleName = AccessRulePropertiesUtil.readProperty(teacherRuleUrl, "Student");
            AccessRule previousRule = accessRuleDaoImpl.findAccessRuleByUrlAndRole(teacherRuleUrl, "Student");
            // 以前不存在这个AccesssRule则建立关系，
            if (previousRule == null) {
                t.addAccessRule(new AccessRule(teacherRuleUrl, teacherRuleName, "Student"));
                // 建立关系
                timeNodeDaoImpl.mergeWithTransaction(t);
            }
            // 以前存在这个Rule
            else
            {
                // 以前这个Rule和时间节点没关系,则建立；有关系则什么都不做
                if (!previousRule.getTimeNodes().contains(t)) {
                    t.addAccessRule(previousRule);
                    timeNodeDaoImpl.mergeWithTransaction(t);
                }
            }
        }
    }
}
