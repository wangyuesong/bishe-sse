package sse.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sse.dao.base.GenericDao;
import sse.entity.Attachment;
import sse.entity.Document;

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

    // public List<Document> findDocumentsForPagingByCreatorId(int page, int pageSize, String sort, String order,
    // Integer creatorId)
    // {
    // HashMap<String, Object> paramMap = new HashMap<String, Object>();
    // paramMap.put("creatorId", creatorId);
    // return this.findForPaging("select t from Document t where t.creator.id=:creatorId", paramMap, page,
    // pageSize, sort,
    // order);
    // }
    //
    // public long findDocumentsForCount()
    // {
    // return this.findForCount("select t from Document t", null);
    // }
    //

}
