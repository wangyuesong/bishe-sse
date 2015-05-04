package sse.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sse.dao.base.GenericDao;
import sse.entity.Attachment;
import sse.entity.Document;
import sse.entity.Menu;
import sse.enums.AttachmentStatusEnum;

/**
 * @author yuesongwang
 *
 */
@Repository
public class AttachmentDaoImpl extends GenericDao<Integer, Attachment>
{
    public void createTempAttachment(Attachment attachment)
    {
        this.persistWithTransaction(attachment);
    }

    public List<Attachment> findTempAttachmentsByUserId(int userId)
    {
        String queryStr = "select a from Attachment a where a.status = :status and a.creator.id = :creator";
        List<Attachment> attachments = this.getEntityManager()
                .createQuery(queryStr, Attachment.class)
                .setParameter("status", AttachmentStatusEnum.TEMP)
                .setParameter("creator", userId)
                .getResultList();
        return attachments;
    }

}
