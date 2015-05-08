package sse.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import sse.utils.PaginationAndSortModel;

/**
 * @Project: sse
 * @Title: AdminWillServiceImpl.java
 * @Package sse.service.impl
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月8日 上午11:16:22
 * @version V1.0
 */
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
     * Description: 匹配算法
     * 
     * @return
     *         List<MatchPair>
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
     * Description: 删除已经和老师建立匹配关系的学生的志愿
     * 
     * @param willList
     * @return
     *         List<Will>
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
     * Description: Find current match count by teacher's id. IF current match count + teachers current students count
     * >= capacity, remove this teacher from future algorithm
     * 
     * @param matchs
     * @param teacherId
     * @return
     *         int
     */
    private int findCurrentMatchCountByTeacherId(List<MatchPair> matchs, int teacherId)
    {
        int count = 0;
        for (MatchPair matchPair : matchs)
            count += (Integer.parseInt(matchPair.getTeacherId()) == teacherId) ? 1 : 0;
        return count;
    }

    public void exportCurrentMatchConditionInExcel()
    {

        List<Student> allStudents = studentDaoImpl.findForPaging("select s from Student s", null, "s.account", "ASC");
        List<MatchPair> matchPairs = new LinkedList<MatchPair>();
        for (Student s : allStudents)
        {
            if (s.getTeacher() != null)
                matchPairs.add(new MatchPair(s, s.getTeacher(), s.getMatchLevel(), s.getMatchType()));
            else
                matchPairs.add(new MatchPair(s, null, null, null));
        }
        int count = matchPairs.size();
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("匹配情况");
        short rowNum = 0;
        short colNum = 0;
        Row row;
        for (rowNum = 0; rowNum < count; rowNum++)
        {
            MatchPair currentMatchPair = matchPairs.get(rowNum);
            row = sheet.createRow(rowNum);
            row.createCell(colNum++).setCellValue(currentMatchPair.getStudentAccount());
            row.createCell(colNum++).setCellValue(currentMatchPair.getStudentName());
            row.createCell(colNum++).setCellValue(currentMatchPair.getTeacherAccount());
            row.createCell(colNum++).setCellValue(currentMatchPair.getTeacherName());
            row.createCell(colNum++).setCellValue(
                    currentMatchPair.getMatchLevel() == null ? "" : currentMatchPair.getMatchLevel().getValue());
            row.createCell(colNum++).setCellValue(
                    currentMatchPair.getMatchType() == null ? "" : currentMatchPair.getMatchType().getValue());
            colNum = 0;
        }
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream("/Users/yuesongwang/Desktop/Try.xlsx");
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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

    /**
     * @Method: findCurrentMatchCondition
     * @Description: 把当前的匹配状态作为Datagrid返回，Entity:Will Model:MatchPair
     * @param @return
     * @return List<MatchPair>
     * @throws
     */
    public GenericDataGrid<MatchPair> findCurrentMatchConditionsForDatagrid(PaginationAndSortModel pam) {
        // 目前还仅支持用学排序，因为Model和Entity中的字段名不一致
        List<Student> allStudents = studentDaoImpl.findForPaging("select s from Student s", null, pam.getPage(),
                pam.getRows(),
                "s.account", pam.getOrder());
        int total = studentDaoImpl.findForCount("select s from Student s", null);

        List<MatchPair> matchPairs = new LinkedList<MatchPair>();
        for (Student s : allStudents)
        {
            if (s.getTeacher() != null)
                matchPairs.add(new MatchPair(s, s.getTeacher(), s.getMatchLevel(), s.getMatchType()));
            else
                matchPairs.add(new MatchPair(s, null, null, null));
        }
        GenericDataGrid<MatchPair> genericDataGrid = new GenericDataGrid<MatchPair>();
        genericDataGrid.setRows(matchPairs);
        genericDataGrid.setTotal(total);
        return genericDataGrid;
    }

    /**
     * @Method: getWillList
     * @Description: 获取志愿表作为Datagrid返回，要做一些格式的转换，将一个学生的三个志愿合并成一条WillModel Entity:Will Model:WillModel
     * @param @return
     * @return List<MatchPair>
     * @throws
     */
    public GenericDataGrid<WillModel> getWillListForDatagrid(int page, int pageSize) {
        List<Will> willList = willDaoImpl.findForPaging("select w from Will w order by w.id.studentId,w.level ASC");
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
        willDataGrid.setTotal(willModelList.size());
        return willDataGrid;
    }
}
