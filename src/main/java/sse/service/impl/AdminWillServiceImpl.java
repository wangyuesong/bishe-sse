package sse.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.commandmodel.MatchPair;
import sse.commandmodel.WillModel;
import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.Student;
import sse.entity.Teacher;
import sse.entity.Will;
import sse.enums.MatchLevelEnum;
import sse.enums.MatchTypeEnum;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.TeacherSelectModel;

@Service
public class AdminWillServiceImpl {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AdminWillServiceImpl.class);

    @Autowired
    private WillDaoImpl willDaoImpl;

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    public List<TeacherSelectModel> findAllTeachersInSelectModelList()
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

    public String findTeacherAccountById(int id)
    {
        return teacherDaoImpl.findById(id).getAccount();
    }

    /**
     * @Method: doMatch
     * @Description: TODO
     * @param @return
     * @return List<MatchPair>
     * @throws
     */
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
                                .findById(w.getId().getTeacherId()), MatchLevelEnum.getTypeByIntLevel(i),
                                MatchTypeEnum.系统分配));
                }
                // Teacher's capacity is smaller than level i student's will
                else
                {
                    List<Will> subWillList = willList.subList(0, t.getCapacity() - t.getStudents().size());
                    for (Will w : subWillList)
                        matchPairs.add(new MatchPair(studentDaoImpl.findById(w.getId().getStudentId()), teacherDaoImpl
                                .findById(w.getId().getTeacherId()), MatchLevelEnum.getTypeByIntLevel(i),
                                MatchTypeEnum.系统分配));
                }
            }
        }
        for (MatchPair p : matchPairs)
        {
            System.out.println(studentDaoImpl.findById(Integer.parseInt(p.getStudentId())).getName() + " match "
                    + teacherDaoImpl.findById(Integer.parseInt(p.getTeacherId())).getName() + " by level "
                    + p.getMatchLevel());
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
            count += (Integer.parseInt(matchPair.getTeacherId()) == teacherId) ? 1 : 0;
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
    private void removeMatchedStudentsFromWillListByStudentId(String studentId, List<Will> willList) {
        for (int i = 0; i < willList.size(); i++)
        {
            if (willList.get(i).getId().getStudentId() == Integer.parseInt(studentId))
                willList.remove(i);
        }
    }

    /**
     * @Method: getWillList
     * @Description: 获取志愿表
     * @param @return
     * @return List<MatchPair>
     * @throws
     */
    public GenericDataGrid<WillModel> getWillList(int page, int pageSize) {
        List<Will> willList = willDaoImpl.findForList("select w from Will w order by w.id.studentId,w.level ASC");
        // 转换Will为一种可以在页面上展示的WillModel
        List<WillModel> willModelList = new ArrayList<WillModel>();
        Will preWill = null;
        WillModel tempModel = null;
        for (Will w : willList)
        {
            if (preWill == null)
            {
                tempModel = new WillModel();
                preWill = w;
            }
            if (preWill.getId().getStudentId() != w.getId().getStudentId()) {
                willModelList.add(tempModel);
                tempModel = new WillModel();
            }
            Student s = studentDaoImpl.findById(w.getId().getStudentId());
            tempModel.setStudentId(s.getId() + "");
            tempModel.setStudentAccount(s.getAccount());
            tempModel.setStudentName(s.getName());
            tempModel.setWillByLevel(w.getLevel(), w.getId().getTeacherId() + "");
            tempModel
                    .setWillTeacherNameLevel(w.getLevel(), teacherDaoImpl.findById(w.getId().getTeacherId()).getName());
            preWill = w;
        }
        if (tempModel.getStudentId() != null)
            willModelList.add(tempModel);
        GenericDataGrid<WillModel> willDataGrid = new GenericDataGrid<WillModel>();
        // 分页
        List<WillModel> subWillModelList = willModelList.subList((page - 1) * pageSize,
                page * pageSize > willModelList.size() ? willModelList.size() : page * pageSize);
        willDataGrid.setRows(subWillModelList);
        willDataGrid.setTotal((long) willModelList.size());
        return willDataGrid;
    }

    /**
     * @Method: updateWills
     * @Description: 更新志愿表
     * @param @param wills
     * @return void
     * @throws
     */
    public void updateWills(ArrayList<WillModel> willModels) {
        for (WillModel wm : willModels)
        {
            for (int i = 1; i < 4; i++)
            {
                String levelWill = wm.getWillByLevel(i);
                if (levelWill == null)
                    willDaoImpl.deleteStudentWillByLevelWithoutTransaction(Integer.parseInt(wm.getStudentId()), i);
                else
                    willDaoImpl.updateStudentWillByLevel(Integer.parseInt(wm.getStudentId()), i,
                            Integer.parseInt(wm.getWillByLevel(i)));
            }
        }
    }
}
