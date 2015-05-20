package sse.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import sse.commandmodel.BasicJson;
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
import sse.enums.WillStatusEnum;
import sse.excelmodel.MatchPairExcelModel;
import sse.excelmodel.WillExcelModel;
import sse.pagemodel.GenericDataGrid;
import sse.pagemodel.TeacherSelectModel;
import sse.utils.ExcelUtil;
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
    public List<MatchPair> doMatch(String sortCriteria)
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
                // I在前一轮匹配中，如果某个教师已经满员，则将其派出在后面的算法中，使用iterator来完成边遍历边删除的动作
                if ((t.getCapacity() - t.getStudents().size()) == findCurrentMatchCountByTeacherId(matchPairs,
                        t.getId()))
                    preIter.remove();
            }

            for (Teacher t : teachersToBeMatch)
            {
                // 对于第i级别的志愿，从志愿表中选出所有还没有被老师拒绝的i等级志愿
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("teacherId", t.getId());
                params.put("status", WillStatusEnum.待定);
                params.put("level", i);
                // 找到所有第i level的，属于teacherId的教师的，状态为待定的志愿，并加入一些排序条件
                willList = willDaoImpl
                        .findForPaging(
                                "select w from Will w where w.teacher.id= :teacherId and w.status= :status and w.level= :level",
                                params, sortCriteria, "asc");
                // 删除这些志愿中所有已经和老师建立了关系的同学的志愿（老师已接受其第一志愿）
                willList = removeTheWillsFromStudentsWhoAlreadyGotMatched(willList);
                // 如果某同学在第i－1级别的匹配中已经和老师进行了匹配，则将该同学的所有will从willList中删除
                // 想知道i－1有哪些同学被接受了，只有遍历之前的matchPairs
                for (MatchPair matchPair : matchPairs)
                    removeMatchedStudentsFromWillListByStudentId(matchPair.getStudentId(), willList);

                // Teacher's capacity is bigger than level i student's will
                if (willList.size() <= (t.getCapacity() - t.getStudents().size()))
                {
                    for (Will w : willList)
                        matchPairs.add(new MatchPair(studentDaoImpl.findById(w.getStudent().getId()), teacherDaoImpl
                                .findById(w.getTeacher().getId()), MatchLevelEnum.getTypeByIntLevel(i),
                                MatchTypeEnum.系统分配));
                }
                // Teacher's capacity is smaller than level i student's will
                else
                {
                    List<Will> subWillList = willList.subList(0, t.getCapacity() - t.getStudents().size());
                    for (Will w : subWillList)
                        matchPairs.add(new MatchPair(studentDaoImpl.findById(w.getStudent().getId()), teacherDaoImpl
                                .findById(w.getTeacher().getId()), MatchLevelEnum.getTypeByIntLevel(i),
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
            if (studentIds.contains(w.getStudent().getId()))
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
            if (willList.get(i).getStudent().getId() == Integer.parseInt(studentId))
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
                if (StringUtils.isEmpty(levelWill))
                {
                    willDaoImpl.beginTransaction();
                    willDaoImpl
                            .deleteStudentWillByLevelWithoutTransaction(Integer.parseInt(wm.getStudentId()), i);
                    willDaoImpl.commitTransaction();
                }
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
     */
    public GenericDataGrid<WillModel> getWillListForDatagrid(int page, int pageSize) {
        List<Student> studentList = studentDaoImpl.findForPaging("select s from Student s order by s.account");
        List<WillModel> willModelList = new ArrayList<WillModel>();
        for (Student s : studentList)
        {
            // 有可能此时已经更新了Will，从Student中取出Will之前需要将Student更新一下
            studentDaoImpl.refresh(s);
            List<Will> wills = s.getWills();
            if (CollectionUtils.isEmpty(wills))
                willModelList.add(new WillModel(s.getId() + "", s.getName(), s.getAccount()));
            else
            {
                WillModel willModel = new WillModel(s.getId() + "", s.getName(), s.getAccount());
                for (Will oneWill : wills)
                {
                    willModel.setWillByLevel(oneWill.getLevel(), oneWill.getTeacher().getId() + "");
                    willModel.setWillTeacherNameLevel(oneWill.getLevel(), oneWill.getTeacher().getName());
                }
                willModelList.add(willModel);
            }
        }
        GenericDataGrid<WillModel> willDataGrid = new GenericDataGrid<WillModel>();
        // // 分页
        List<WillModel> subWillModelList = willModelList.subList((page - 1) * pageSize,
                page * pageSize > willModelList.size() ? willModelList.size() : page * pageSize);
        willDataGrid.setRows(subWillModelList);
        willDataGrid.setTotal(willModelList.size());
        return willDataGrid;
    }

    /**
     * @Method: updateRelationshipBetweenTeacherAndStudent
     * @Description: 更新学生和教师之间的关系
     * @param @param matchPairs
     * @return void
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
                    if (w.getTeacher().getId() == t.getId())
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
                if (mp2.getTeacherId().equals(mp.getStudentId()))
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

    /**
     * Description: private，检测id为studentid的学生是否已经属于Teacher t
     * 
     * @param t
     * @param studentId
     * @return
     *         boolean
     */
    private boolean isThisStudentExistInATeacherSStudentList(Teacher t, int studentId)
    {
        return t.getStudents().contains(studentDaoImpl.findById(studentId));
    }

    /**
     * Description: TODO
     * 
     * @return
     *         Workbook
     */
    public Workbook exportWillListInExcel() {
        List<Will> willList = willDaoImpl.findForPaging("select w from Will w order by w.student.id,w.level ASC");
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
            if (preWill.getStudent().getId() != w.getStudent().getId()) {
                willModelList.add(tempModel);
                tempModel = new WillModel();
            }
            Student s = studentDaoImpl.findById(w.getStudent().getId());
            tempModel.setStudentId(s.getId() + "");
            tempModel.setStudentAccount(s.getAccount());
            tempModel.setStudentName(s.getName());
            tempModel.setWillByLevel(w.getLevel(), w.getTeacher().getId() + "");
            tempModel
                    .setWillTeacherNameLevel(w.getLevel(), teacherDaoImpl.findById(w.getTeacher().getId()).getName());
            preWill = w;
        }
        if (tempModel.getStudentId() != null)
            willModelList.add(tempModel);
        List<WillExcelModel> excelModels = new ArrayList<WillExcelModel>();
        for (WillModel w : willModelList)
        {
            excelModels.add(new WillExcelModel(w.getStudentName(), w.getStudentAccount(), w.getFirstWillTeacherName(),
                    w.getSecondWillTeacherName(), w.getThirdWillTeacherName()));
        }
        ExcelUtil<WillExcelModel> excelUtil = new ExcelUtil<WillExcelModel>();
        Workbook wb = new HSSFWorkbook();
        excelUtil.wrtieWorkbook(wb, WillExcelModel.getColNames(), "志愿表", excelModels);
        return wb;
    }

    /**
     * Description: 将现在的匹配情况导出到Workbook中
     * 
     * @return
     *         Workbook
     */
    public Workbook exportCurrentMatchConditionInExcel()
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
        List<MatchPairExcelModel> excelModels = new ArrayList<MatchPairExcelModel>();
        for (MatchPair p : matchPairs)
        {
            excelModels.add(new MatchPairExcelModel(p.getStudentAccount(), p.getStudentName(), p.getTeacherAccount(), p
                    .getTeacherName(),
                    p.getMatchLevel().getValue(), p.getMatchType().toString()));
        }
        ExcelUtil<MatchPairExcelModel> excelUtil = new ExcelUtil<MatchPairExcelModel>();
        Workbook wb = new HSSFWorkbook();
        excelUtil.wrtieWorkbook(wb, MatchPairExcelModel.getColNames(), "匹配情况", excelModels);
        return wb;
    }
}
