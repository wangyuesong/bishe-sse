package sse.dao.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import sse.dao.base.GenericDao;
import sse.entity.Teacher;
import sse.pageModel.TeacherListModel;
import sse.utils.ClassTool;

@Repository
public class TeacherDaoImpl extends GenericDao<Integer, Teacher>
{

    /*
     * (non-Javadoc)
     * 
     * @see sse.dao.impl.ITeacherDao#findTeachersForPaging()
     */
    public List<Teacher> findTeachersForPaging(int page, int pageSize, String sortCriteria, String order)
    {
        List<Teacher> teacherList = this.findForPaging("select t from Teacher t", new HashMap<String, Object>(), page,
                pageSize, sortCriteria, order);
        return teacherList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see sse.dao.impl.ITeacherDao#findTeachersForCount()
     */
    public long findTeachersForCount()
    {
        return this.findForCount("select t from Teacher t", null);
    }

    public int getTeacherIdByAccount(String account)
    {
        String queryStr = "select t from Teacher t where t.account = :account";
        List<Teacher> teachers = this.getEntityManager()
                .createQuery(queryStr, Teacher.class)
                .setParameter("account", account).getResultList();
        if (!CollectionUtils.isEmpty(teachers))
            return teachers.get(0).getId();
        else
            return -1;
    }

}
