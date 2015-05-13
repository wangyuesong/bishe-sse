package sse.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.commandmodel.BasicJson;
import sse.commandmodel.SystemMessageFormModel;
import sse.dao.impl.AttachmentDaoImpl;
import sse.dao.impl.SystemMessageDaoImpl;
import sse.dao.impl.TimeNodeDaoImpl;
import sse.dao.impl.TimeNodeDaoImpl.CalendarEvent;
import sse.entity.Attachment;
import sse.entity.SystemMessage;
import sse.entity.TimeNode;
import sse.enums.AttachmentStatusEnum;
import sse.enums.TimeNodeTypeEnum;
import sse.exception.SSEException;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.SystemMessageListModel;
import sse.pageModel.TimeNodeListModel;
import sse.service.impl.DocumentSerivceImpl.SimpleAttachmentInfo;
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
    private SystemMessageDaoImpl systemMessageDaoImpl;

    /**
     * Description: 获得所有时间节点
     * 
     * @return
     *         TimeNodeFormModel
     */
    public GenericDataGrid<TimeNodeListModel> getCurrentTimeNodesInDatagrid(PaginationAndSortModel pam) {
        List<TimeNode> timeNodes = timeNodeDaoImpl.findForPaging("select t from TimeNode t", null,
                pam.getPage(), pam.getRows(), pam.getSort(), pam.getOrder());
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

    /**
     * Description: TODO
     * 
     * @return
     *         List<SimpleAttachmentInfo>
     */
    public List<SimpleAttachmentInfo> getTempAttachmentsOfAdmin() {
        // TODO Auto-generated method stub
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("role", "Administrator");
        params.put("status", AttachmentStatusEnum.TEMP);
        List<Attachment> attachments = attachmentDaoImpl
                .findForPaging("select a from Attachment a where a.creator.role=:role and a.status=:status", params);
        List<SimpleAttachmentInfo> attachmentInfos = new ArrayList<DocumentSerivceImpl.SimpleAttachmentInfo>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Attachment a : attachments)
            attachmentInfos.add(new SimpleAttachmentInfo(a.getId(), a.getListName(), sdf.format(a.getCreateTime()),
                    "管理员"));
        return attachmentInfos;
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
        systemMessageDaoImpl.persistWithTransaction(message);
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
            // 新建
            if (model.getId() == 0)
            {
                TimeNode t;
                try {
                    t = new TimeNode(TimeNodeTypeEnum.getType(model.getType()), model.getName(), sdf.parse(model
                            .getTime()), model.getDescription());
                } catch (ParseException e) {
                    throw new SSEException("时间格式不对", e);
                }
                timeNodeDaoImpl.persistWithTransaction(t);
            }
            else
            {
                TimeNode t = timeNodeDaoImpl.findById(model.getId());
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
}
