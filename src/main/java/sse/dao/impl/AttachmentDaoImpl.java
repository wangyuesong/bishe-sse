package sse.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sse.dao.base.GenericDao;
import sse.entity.Attachment;
import sse.entity.Document;
import sse.entity.Menu;
import sse.enums.AttachmentStatusEnum;
import sse.enums.DocumentTypeEnum;

/**
 * @author yuesongwang
 *
 */
@Repository
public class AttachmentDaoImpl extends GenericDao<Integer, Attachment>
{
    /** 
     * Description: TODO
     * @param userId
     * @param type
     * @param status
     * @return
     * List<Attachment>
     */
    public List<Attachment> findForeverAttachmentsByUserIdAndDocumentType(int userId, DocumentTypeEnum type,
            AttachmentStatusEnum status)
    {
        String queryStr = "select a from Attachment a where a.status = :status and a.creator.id = :creator and a.document.documentType=:documentType";
        List<Attachment> attachments = this.getEntityManager()
                .createQuery(queryStr, Attachment.class)
                .setParameter("status", status)
                .setParameter("creator", userId)
                .setParameter("documentType", type)
                .getResultList();
        return attachments;
    }

}
