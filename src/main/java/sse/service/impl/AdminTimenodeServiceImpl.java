package sse.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.commandmodel.DocumentFormModel;
import sse.commandmodel.SystemMessageFormModel;
import sse.commandmodel.TimeNodeFormModel;
import sse.dao.impl.AttachmentDaoImpl;
import sse.dao.impl.SystemMessageDaoImpl;
import sse.dao.impl.TimeNodeDaoImpl;
import sse.entity.Attachment;
import sse.entity.Document;
import sse.entity.SystemMessage;
import sse.entity.TimeNode;
import sse.entity.User;
import sse.enums.AttachmentStatusEnum;
import sse.enums.DocumentTypeEnum;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.SystemMessageListModel;
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

    public void updateTimeNodes(TimeNodeFormModel model)
    {
        timeNodeDaoImpl.updateTimeNodes(model);
    }

    /**
     * Description: 获得所有时间节点
     * 
     * @return
     *         TimeNodeFormModel
     */
    public TimeNodeFormModel getCurrentTimeNodes() {
        TimeNode tianbaozhiuan = timeNodeDaoImpl.findTimeNodeByName("填报志愿");
        TimeNode ketishenbao = timeNodeDaoImpl.findTimeNodeByName("课题申报");
        TimeNode bishejinxing = timeNodeDaoImpl.findTimeNodeByName("毕设进行");
        TimeNode dabianshenqing = timeNodeDaoImpl.findTimeNodeByName("答辩申请");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        TimeNodeFormModel model = new TimeNodeFormModel(sdf.format(tianbaozhiuan.getTime()),
                sdf.format(ketishenbao.getTime()), sdf.format(bishejinxing.getTime()), sdf.format(dabianshenqing
                        .getTime()),
                tianbaozhiuan.getDescription(), ketishenbao.getDescription(), bishejinxing.getDescription(),
                dabianshenqing.getDescription());
        return model;
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
                .findForPaging("select a from Attachment a where a.creator.role=:role and a.status=:status");
        if (tempAttachmentList != null)
            for (Attachment attachment : tempAttachmentList)
            {
                // Change the status of attachment to FOREVER
                attachment.setStatus(AttachmentStatusEnum.FOREVER);
                message.addAttachment(attachment);
            }
        systemMessageDaoImpl.persistWithTransaction(message);
    }
}
