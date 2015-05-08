package sse.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import sse.dao.base.GenericDao;
import sse.entity.Document;

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

}
