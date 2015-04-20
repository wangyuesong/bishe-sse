package sse.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import sse.dao.base.GenericDao;
import sse.entity.Teacher;
import sse.entity.Will;
import sse.entity.WillPK;
import sse.enums.WillStatusEnum;
import sse.pageModel.WillModel;

/**
 * @author yuesongwang
 *
 */
@Repository
public class WillDaoImpl extends GenericDao<Integer, Will> {

    @Autowired
    TeacherDaoImpl teacherDaoImpl;

    public HashMap<String, String> findPreviousSelectionByStudentId(int studentId)
    {
        List<Will> wills = this.getEntityManager().createNamedQuery("Will.findAllWillByStudentId", Will.class)
                .setParameter("studentId", studentId).getResultList();
        if (!CollectionUtils.isEmpty(wills))
        {
            HashMap<String, String> returnMap = new HashMap<String, String>();
            for (Will w : wills)
            {
                Teacher t = teacherDaoImpl.findById(w.getId().getTeacherId());
                returnMap.put("" + w.getLevel(), t.getAccount());
            }
            return returnMap;
        }
        else
            return null;
    }

    private void deleteStudentWillByLevel(int studentId, int level)
    {
        String queryStr = "select w from Will w where w.level=:level and w.id.studentId= :studentId";
        List<Will> wills = this.getEntityManager().createQuery(queryStr, Will.class)
                .setParameter("studentId", studentId).setParameter("level", level).getResultList();
        if (!CollectionUtils.isEmpty(wills))
            super.remove(wills.get(0));
    }

    public void updateSelection(WillModel willModel, int studentId)
    {
        List<Will> willList = new ArrayList<Will>();
        beginTransaction();
        for (int i = 1; i <= 3; i++)
        {
            // Remove those empty wills first
            if (StringUtils.isEmpty(willModel.getWillByLevel(i)))
                deleteStudentWillByLevel(studentId, i);
            else
            {
                // Keep those wills that needed to be updated or created
                String account = willModel.getWillByLevel(i);
                int teacherId = teacherDaoImpl.getTeacherIdByAccount(account);
                willList.add(new Will(new WillPK(studentId, teacherId), i));
            }
        }

        // These wills can be updated or created
        if (!CollectionUtils.isEmpty(willList))
            for (Will w : willList)
            {
                super.merge(w);
            }
        commitTransaction();

    }

    public List<Will> findAllWillsByTeacherId(int teacherId)
    {
        String queryStr = "select w from Will w where w.id.teacherId= :teacherId";
        return this.getEntityManager().createQuery(queryStr, Will.class)
                .setParameter("teacherId", teacherId).getResultList();
    }

    /**
     * @Method: findAllNotRejectedWillsByTeacherIdLevelAscending
     * @Description: Get one level wills which is not rejected by this teacher in ascending order
     * @param @param teacherId
     * @param @return
     * @return List<Will>
     * @throws
     */
    public List<Will> findAllNotRejectedWillsByTeacherIdAndLevel(int teacherId, int level)
    {
        String queryStr = "select w from Will w where w.id.teacherId= :teacherId and w.status!= :status and w.level= :level order by w.updateTime asc";
        return this.getEntityManager().createQuery(queryStr, Will.class)
                .setParameter("teacherId", teacherId)
                .setParameter("status", WillStatusEnum.REJECTED)
                .setParameter("level", level)
                .getResultList();
    }
}
