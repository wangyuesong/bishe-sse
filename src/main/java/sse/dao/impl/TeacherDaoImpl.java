package sse.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import sse.dao.base.GenericDao;
import sse.entity.Teacher;

@Repository
public class TeacherDaoImpl extends GenericDao<Integer, Teacher>
{


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
