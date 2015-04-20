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

@Service
public class AdminServiceImpl {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private WillDaoImpl willDaoImpl;

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    public void doMatch()
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
                // If for now the teacher has already get fully matched, remove the teacher from the future algorithm
                if ((t.getCapacity() - t.getStudents().size()) == findCurrentMatchCountByTeacherId(matchPairs,
                        t.getId()))
                    preIter.remove();
            }

            for (Teacher t : teachersToBeMatch)
            {
                willList = willDaoImpl.findAllNotRejectedWillsByTeacherIdAndLevel(t.getId(), i);
                // Remove those who get matched during one level match from will list
                for (MatchPair matchPair : matchPairs)
                    removeMatchedStudentsFromWillListByStudentId(matchPair.getStudentId(), willList);
                // Teacher's capacity is bigger than level 2 student's will
                if (willList.size() <= (t.getCapacity() - t.getStudents().size()))
                {
                    for (Will w : willList)
                        matchPairs.add(new MatchPair(w.getId().getStudentId(), w.getId().getTeacherId(), i));
                }
                // Teacher's capacity is smaller than level 2 student's will
                else
                {
                    List<Will> subWillList = willList.subList(0, t.getCapacity() - t.getStudents().size());
                    for (Will w : subWillList)
                        matchPairs.add(new MatchPair(w.getId().getStudentId(), w.getId().getTeacherId(), i));
                }
            }

        }

        for (MatchPair p : matchPairs)
        {
            System.out.println(studentDaoImpl.findById(p.getStudentId()).getName() + " match "
                    + teacherDaoImpl.findById(p.getTeacherId()).getName() + " by level " + p.getMatchLevel());
        }

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

    private static class MatchPair
    {

        private int studentId;
        private int teacherId;
        private int matchLevel;

        public MatchPair(int studentId, int teacherId, int matchLevel) {
            super();
            this.studentId = studentId;
            this.teacherId = teacherId;
            this.matchLevel = matchLevel;
        }

        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }

        public int getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(int teacherId) {
            this.teacherId = teacherId;
        }

        public int getMatchLevel() {
            return matchLevel;
        }

        public void setMatchLevel(int matchLevel) {
            this.matchLevel = matchLevel;
        }

    }
}
