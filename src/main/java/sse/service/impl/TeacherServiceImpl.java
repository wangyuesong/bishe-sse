/**  
 * @Project: sse
 * @Title: TeacherServiceImpl.java
 * @Package sse.service.impl
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月4日 上午11:02:56
 * @version V1.0  
 */
/**
 * 
 */
package sse.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.commandmodel.BasicJson;
import sse.commandmodel.MatchPair;
import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.Student;
import sse.entity.Teacher;
import sse.entity.Will;
import sse.enums.MatchLevelEnum;
import sse.enums.MatchTypeEnum;

/**
 * @author yuesongwang
 *
 */
@Service
public class TeacherServiceImpl {

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    @Autowired
    private WillDaoImpl willDaoImpl;

    /**
     * @Method: updateRelationshipBetweenTeacherAndStudent
     * @Description: 更新学生和教师之间的关系
     * @param @param matchPairs
     * @return void
     * @throws
     */
    public void updateRelationshipBetweenTeacherAndStudent(List<MatchPair> matchPairs)
    {
        for (MatchPair pair : matchPairs)
        {
            String studentId = pair.getStudentId();
            String teacherId = pair.getTeacherId();
            Student s = studentDaoImpl.findById(Integer.parseInt(studentId));
            if (StringUtils.equals(teacherId, ""))
            {
                Teacher t = s.getTeacher();
                if (t != null)
                {
                    t.getStudents().remove(s);
                    teacherDaoImpl.mergeWithTransaction(t);
                }
                s.setTeacher(null);
                s.setMatchType(null);
                s.setMatchLevel(null);
                studentDaoImpl.mergeWithTransaction(s);
            }
            else
            {
                Teacher t = teacherDaoImpl.findById(Integer.parseInt(teacherId));
                t.getStudents().add(s);
                teacherDaoImpl.mergeWithTransaction(t);
                s.setTeacher(t);
                List<Will> wills = willDaoImpl.findWillsByStudentId(s.getId());
                boolean isWillCandiate = false;
                for (Will w : wills)
                    if (w.getId().getTeacherId() == t.getId())
                    {
                        s.setMatchLevel(MatchLevelEnum.getTypeByIntLevel(w.getLevel()));
                        isWillCandiate = true;
                        break;
                    }
                if (!isWillCandiate)
                    s.setMatchLevel(MatchLevelEnum.调剂);
                s.setMatchType(pair.getMatchType());
                studentDaoImpl.mergeWithTransaction(s);
            }
        }
    }

    /**
     * @Method: doCapacityCheck
     * @Description: 检查当前匹配下，教师是否超员
     *               此处有问题，前台有可能传入根本没有改变过的row，导致容量计算失效，因此需要isThisStudentExistInATeacherSStudentList
     * @param @param matchPairs
     * @param @return
     * @return BasicJson
     * @throws
     */
    public BasicJson doCapacityCheck(List<MatchPair> matchPairs)
    {
        // 首先过滤一下没有匹配教师的行
        List<MatchPair> pairsThatContainsTeacher = new ArrayList<MatchPair>();
        for (MatchPair p : matchPairs) {
            if (p.getTeacherId() != null && !p.getTeacherId().equals("")) {
                pairsThatContainsTeacher.add(p);
            }
        }
        String returnMessage = "";
        boolean returnStatus = true;
        while (!pairsThatContainsTeacher.isEmpty())
        {
            Iterator<MatchPair> iter = pairsThatContainsTeacher.iterator();
            MatchPair mp = iter.next();
            iter.remove();
            Teacher t = teacherDaoImpl.findById(Integer.parseInt(mp.getTeacherId()));
            // 纪录该教师的容量
            int capacity = t.getCapacity();
            // 纪录当前已经匹配到该教师的人数.将这个学生也算上
            int count = 0;
            // 先查一下这个学生是不是早已经存在于教师的studentlist中了
            if (!isThisStudentExistInATeacherSStudentList(t, Integer.parseInt(mp.getStudentId())))
                count = t.getStudents().size() + 1;
            // 已经存在的话，就不在count上增加了
            else {
                count = t.getStudents().size();
            }
            while (iter.hasNext())
            {
                MatchPair mp2 = iter.next();
                if (mp2.getTeacherId().equals(mp.getTeacherId()))
                {
                    iter.remove();
                    // 同样需要检查该学生是否早已存在于教师的studentList
                    if (!isThisStudentExistInATeacherSStudentList(t, Integer.parseInt(mp.getStudentId())))
                        count++;
                }
            }
            if (count > capacity)
            {
                returnStatus = false;
                returnMessage += "教师:" + mp.getTeacherName() + "-容量已满,容量:" + capacity + ",实际:" + count + "\n";
            }
        }
        return new BasicJson(returnStatus, returnMessage, null);
    }

    private boolean isThisStudentExistInATeacherSStudentList(Teacher t, int studentId)
    {
        return t.getStudents().contains(studentDaoImpl.findById(studentId));
    }
}
