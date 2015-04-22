package sse.service.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.Student;
import sse.entity.Teacher;
import sse.entity.Will;
import sse.enums.MatchTypeEnum;
import sse.jsonmodel.TeacherListModel;
import sse.jsonmodel.TeacherSelectModel;
import sse.utils.ClassTool;

@Service
public class AdminServiceImpl {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private WillDaoImpl willDaoImpl;

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    public List<TeacherSelectModel> findAllTeachersForSelect()
    {
        List<Teacher> teacherList = teacherDaoImpl.findAll();
        List<TeacherSelectModel> teacherSelectModels = new LinkedList<TeacherSelectModel>();
        for (Teacher t : teacherList)
        {
            TeacherSelectModel ts = new TeacherSelectModel(t.getId(), t.getAccount(), t.getName(), t.getCapacity());
            teacherSelectModels.add(ts);
        }
        return teacherSelectModels;
    }

    public List<MatchPair> doMatch()
    {
        List<Teacher> allTeachers = teacherDaoImpl.findAll();
        List<Teacher> teachersToBeMatch = new LinkedList<Teacher>();
        List<Will> willList = new LinkedList<Will>();
        List<MatchPair> matchPairs = new LinkedList<MatchPair>();
        // Eliminate those teachers whose capacity is full
        for (Teacher t : allTeachers)
        {
            List<Student> students = t.getStudents();
            if (students.size() < t.getCapacity())
            {
                teachersToBeMatch.add(t);
            }
        }

        // Level match
        for (int i = 1; i <= 3; i++)
        {
            Iterator<Teacher> preIter = teachersToBeMatch.iterator();
            while (preIter.hasNext())
            {
                Teacher t = preIter.next();
                // If after previous match, the teacher has already get fully matched, remove the teacher from the
                // future algorithm
                if ((t.getCapacity() - t.getStudents().size()) == findCurrentMatchCountByTeacherId(matchPairs,
                        t.getId()))
                    preIter.remove();
            }

            for (Teacher t : teachersToBeMatch)
            {
                // 对于第i级别的志愿，从志愿表中选出所有还没有被老师拒绝的i等级志愿
                willList = willDaoImpl.findAllNotRejectedWillsByTeacherIdAndLevel(t.getId(), i);
                // 删除这些志愿中所有已经和老师建立了关系的同学的志愿（老师已接受其第一志愿）
                willList = removeTheWillsFromStudentsWhoAlreadyGotMatched(willList);
                // 如果某同学在第i－1级别的匹配中已经和老师进行了匹配，则将该同学的所有will从willList中删除
                for (MatchPair matchPair : matchPairs)
                    removeMatchedStudentsFromWillListByStudentId(matchPair.getStudentId(), willList);
                // Teacher's capacity is bigger than level i student's will
                if (willList.size() <= (t.getCapacity() - t.getStudents().size()))
                {
                    for (Will w : willList)
                        matchPairs.add(new MatchPair(studentDaoImpl.findById(w.getId().getStudentId()), teacherDaoImpl
                                .findById(w.getId().getTeacherId()), MatchTypeEnum.getTypeByIntLevel(i)));
                }
                // Teacher's capacity is smaller than level i student's will
                else
                {
                    List<Will> subWillList = willList.subList(0, t.getCapacity() - t.getStudents().size());
                    for (Will w : subWillList)
                        matchPairs.add(new MatchPair(studentDaoImpl.findById(w.getId().getStudentId()), teacherDaoImpl
                                .findById(w.getId().getTeacherId()), MatchTypeEnum.getTypeByIntLevel(i)));
                }
            }
        }
        for (MatchPair p : matchPairs)
        {
            System.out.println(studentDaoImpl.findById(p.getStudentId()).getName() + " match "
                    + teacherDaoImpl.findById(p.getTeacherId()).getName() + " by level " + p.getMatchLevel());
        }
        return matchPairs;

    }

    /**
     * @Method: removeTheWillsFromStudentsWhoAlreadyGotMatched
     * @Description: 删除已经和老师建立匹配关系的学生的志愿
     * @param @param willList
     * @param @return
     * @return List<Will>
     * @throws
     */
    private List<Will> removeTheWillsFromStudentsWhoAlreadyGotMatched(List<Will> willList) {
        List<Student> studentsWhoHaveATeacher = studentDaoImpl.findStudentsWhoHaveATeacher();
        List<Integer> studentIds = new LinkedList<Integer>();
        for (Student s : studentsWhoHaveATeacher)
            studentIds.add(s.getId());
        Iterator<Will> iter = willList.iterator();
        while (iter.hasNext())
        {
            Will w = iter.next();
            if (studentIds.contains(w.getId().getStudentId()))
                iter.remove();
        }
        return willList;
    }

    /**
     * @Method: findCurrentMatchCountByTeacherId
     * @Description: Find current match count by teacher's id. IF current match count + teachers current students count
     *               >= capacity, remove this teacher from future algorithm
     * @param @param matchs
     * @param @param teacherId
     * @param @return
     * @return int
     * @throws
     */
    private int findCurrentMatchCountByTeacherId(List<MatchPair> matchs, int teacherId)
    {
        int count = 0;
        for (MatchPair matchPair : matchs)
            count += (matchPair.getTeacherId() == teacherId) ? 1 : 0;
        return count;
    }

    /**
     * @Method: removeMatchedStudentsFromWillListByStudentId
     * @Description: Remove those who get matched during one level match from will list
     * @param @param studentId
     * @param @param willList
     * @return void
     * @throws
     */
    private void removeMatchedStudentsFromWillListByStudentId(int studentId, List<Will> willList) {
        for (int i = 0; i < willList.size(); i++)
        {
            if (willList.get(i).getId().getStudentId() == studentId)
                willList.remove(i);
        }
    }

    public static class MatchPair
    {

        private int studentId;
        private String studentAccount;
        private String studentName;
        private int teacherId;
        private String teacherAccount;
        private String teacherName;
        private MatchTypeEnum matchLevel;

        public MatchPair(Student student, Teacher t, MatchTypeEnum macthLevels)
        {
            this.studentId = student.getId();
            this.studentAccount = student.getAccount();
            this.studentName = student.getName();
            if (t != null)
            {
                this.teacherAccount = t.getAccount();
                this.teacherId = t.getId();
                this.teacherName = t.getName();
            }
            this.matchLevel = macthLevels;
        }

        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }

        public String getStudentAccount() {
            return studentAccount;
        }

        public void setStudentAccount(String studentAccount) {
            this.studentAccount = studentAccount;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public int getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(int teacherId) {
            this.teacherId = teacherId;
        }

        public String getTeacherAccount() {
            return teacherAccount;
        }

        public void setTeacherAccount(String teacherAccount) {
            this.teacherAccount = teacherAccount;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public MatchTypeEnum getMatchLevel() {
            return matchLevel;
        }

        public void setMatchLevel(MatchTypeEnum matchLevel) {
            this.matchLevel = matchLevel;
        }

    }
}
