package sse.dao.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sse.dao.base.GenericDao;
import sse.entity.Document;
import sse.entity.TimeNode;
import sse.enums.DocumentTypeEnum;

/**
 * @author yuesongwang
 *
 */
@Repository
public class DocumentDaoImpl extends GenericDao<Integer, Document>
{

    public List<Document> findDocumentsForPagingByCreatorId(int page, int pageSize, String sort, String order,
            Integer creatorId)
    {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("creatorId", creatorId);
        return this.findForPaging("select t from Document t where t.creator.id=:creatorId", paramMap, page,
                pageSize, sort,
                order);
    }

    public Document findDocumentByStudentIdAndType(int studentId, DocumentTypeEnum documentType)
    {
        String queryStr = "select d from Document d where d.creator.id=:studentId and d.documentType=:documentType";
        List<Document> documentList = this.getEntityManager()
                .createQuery(queryStr, Document.class).setParameter("studentId", studentId)
                .setParameter("documentType", documentType).
                getResultList();
        if (documentList.size() == 0)
            return null;
        else
            return documentList.get(0);
    }
}
