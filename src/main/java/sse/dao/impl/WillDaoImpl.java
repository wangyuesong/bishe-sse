package sse.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import sse.commandmodel.WillModel;
import sse.dao.base.GenericDao;
import sse.entity.Will;
import sse.entity.WillPK;
import sse.enums.WillStatusEnum;

/**
 * @Project: sse
 * @Title: WillDaoImpl.java
 * @Package sse.dao.impl
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月8日 上午10:43:15
 * @version V1.0
 */
@Repository
public class WillDaoImpl extends GenericDao<Integer, Will> {

    @Autowired
    TeacherDaoImpl teacherDaoImpl;

    public List<Will> findPreviousSelectionsByStudentId(int studentId)
    {
        List<Will> wills = this.getEntityManager().createNamedQuery("Will.findAllWillByStudentId", Will.class)
                .setParameter("studentId", studentId).getResultList();
        return wills;
    }

    /**
     * Description: 更新学生的志愿
     * 
     * @param @param willModel
     * @return void
     */
    public void updateSelection(WillModel willModel)
    {
        int studentId = Integer.parseInt(willModel.getStudentId());
        List<Will> willList = new ArrayList<Will>();
        beginTransaction();
        for (int i = 1; i <= 3; i++)
        {
            // Remove those empty wills first
            if (StringUtils.isEmpty(willModel.getWillByLevel(i)))
                deleteStudentWillByLevelWithoutTransaction(studentId, i);
            else
            {
                // Keep those wills that needed to be updated or created
                String teacherId = willModel.getWillByLevel(i);
                willList.add(new Will(studentId, Integer.parseInt(teacherId), i));
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

    /**
     * Description: TODO
     * 
     * @param @param studentId
     * @param @return
     * @return List<Will>
     */
    public List<Will> findWillsByStudentId(int studentId)
    {
        String queryStr = "select w from Will w where w.id.studentId= :studentId";
        return this.getEntityManager().createQuery(queryStr, Will.class)
                .setParameter("studentId", studentId).getResultList();
    }

    /**
     * Description: TODO
     * 
     * @param @param teacherId
     * @param @return
     * @return List<Will>
     */
    public List<Will> findWillsByTeacherId(int teacherId)
    {
        String queryStr = "select w from Will w where w.id.teacherId= :teacherId";
        return this.getEntityManager().createQuery(queryStr, Will.class)
                .setParameter("teacherId", teacherId).getResultList();
    }

    /**
     * Description: Get one level wills which is not rejected by this teacher in ascending order
     * 
     * @param @param teacherId
     * @param @param level
     * @param @return
     * @return List<Will>
     */
    public List<Will> findAllNotRejectedWillsByTeacherIdAndLevel(int teacherId, int level)
    {
        String queryStr = "select w from Will w where w.id.teacherId= :teacherId and w.status!= :status and w.level= :level order by w.updateTime asc";
        return this.getEntityManager().createQuery(queryStr, Will.class)
                .setParameter("teacherId", teacherId)
                .setParameter("status", WillStatusEnum.接受)
                .setParameter("level", level)
                .getResultList();
    }

    /**
     * Description: 删除id为studentId的志愿登记为level的志愿,没有transaction
     * 
     * @param @param studentId
     * @param @param level
     * @return void
     */
    public void deleteStudentWillByLevelWithoutTransaction(int studentId, int level)
    {
        String queryStr = "select w from Will w where w.level=:level and w.id.studentId= :studentId";
        List<Will> wills = this.getEntityManager().createQuery(queryStr, Will.class)
                .setParameter("studentId", studentId).setParameter("level", level).getResultList();
        if (!CollectionUtils.isEmpty(wills))
            super.remove(wills.get(0));
    }

    /**
     * @Method: updateStudentWillByLevel
     * @Description: 创建或更新id为studentId的学生的志愿等级为i的志愿为willByLevel,没有transaction
     * @param @param studentId
     * @param @param i
     * @param @param teacherId
     * @return void
     */
    public void updateStudentWillByLevel(int studentId, int i, int teacherId) {
        String queryStr = "select w from Will w where w.level=:level and w.id.studentId= :studentId";
        List<Will> wills = this.getEntityManager().createQuery(queryStr, Will.class)
                .setParameter("studentId", studentId).setParameter("level", i).getResultList();
        // 如果之前有这个level的will
        if (!CollectionUtils.isEmpty(wills))
        {
            Will w = wills.get(0);
            if (w.getStudentId() != studentId || w.getTeacherId() != teacherId)
            {
                w.setStudentId(studentId);
                w.setTeacherId(teacherId);
                super.mergeWithTransaction(w);
                // super.removeWithTransaction(w);
                // super.persistWithTransaction(new Will(new WillPK(studentId, teacherId), i));
            }
        }
        // 如果没有这个level的will，直接创建
        else {
            super.persistWithTransaction(new Will(studentId, teacherId, i));
        }
    }
}
