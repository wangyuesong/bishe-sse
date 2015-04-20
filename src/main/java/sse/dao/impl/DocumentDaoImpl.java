package sse.dao.impl;

import java.util.HashMap;
import java.util.LinkedList;
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

    public long findDocumentsForCount()
    {
        return this.findForCount("select t from Document t", null);
    }

    // public int getTeacherIdByAccount(String account)
    // {
    // String queryStr = "select t from Document t where t.account = :account";
    // List<Teacher> teachers = this.getEntityManager()
    // .createQuery(queryStr, Teacher.class)
    // .setParameter("account", account).getResultList();
    // if (!CollectionUtils.isEmpty(teachers))
    // return teachers.get(0).getId();
    // else
    // return -1;
    // }

}
