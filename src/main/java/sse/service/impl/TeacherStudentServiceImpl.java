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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.commandmodel.BasicJson;
import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.Student;
import sse.entity.Teacher;
import sse.entity.Will;
import sse.enums.MatchLevelEnum;
import sse.enums.MatchTypeEnum;
import sse.enums.WillStatusEnum;
import sse.pagemodel.CandidateStudentListModel;
import sse.pagemodel.GenericDataGrid;
import sse.pagemodel.StudentListModel;
import sse.utils.PaginationAndSortModel;

/**
 * @Project: sse
 * @Title: TeacherStudentServiceImpl.java
 * @Package sse.service.impl
 * @Description: TeacherStudentController调用的，教师相关学生的Service
 * @author YuesongWang
 * @date 2015年5月9日 上午11:28:32
 * @version V1.0
 */
@Service
public class TeacherStudentServiceImpl {

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    @Autowired
    private WillDaoImpl willDaoImpl;

    /**
     * Description: 返回某教师的所有学生
     * 
     * @param teacherId
     * @param pm
     * @return
     *         GenericDataGrid<StudentListModel>
     */
    public GenericDataGrid<StudentListModel> getMyStudentsForPagingInGenericDataGrid(int teacherId,
            PaginationAndSortModel pm)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("teacherId", teacherId);
        List<Student> myStudents = studentDaoImpl.findForPaging(
                "select s from Student s where s.teacher.id=:teacherId", params, pm.getPage(), pm.getRows(),
                pm.getSort(), pm.getOrder());
        int count = studentDaoImpl.findForCount("select s from Student s where s.teacher.id=:teacherId", params);
        List<StudentListModel> myStudentModels = new ArrayList<StudentListModel>();
        for (Student student : myStudents)
        {
            myStudentModels.add(new StudentListModel(student.getId(), student.getAccount(), student.getName(), student
                    .getEmail(), student.getPhone()));
        }
        return new GenericDataGrid<StudentListModel>(count, myStudentModels);
    }

    /**
     * Description: 找出第一志愿选择该教师的学生
     * 
     * @param teacherId
     * @param pm
     * @return
     *         GenericDataGrid<StudentListModel>
     */
    public GenericDataGrid<CandidateStudentListModel> getCandidateStudentsForPagingInDataGrid(int teacherId,
            PaginationAndSortModel pm)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("teacherId", teacherId);
        params.put("level", 1);
        List<Will> wills = willDaoImpl.findForPaging(
                "select w from Will w where w.teacherId=:teacherId and w.level=:level", params, pm.getPage(),
                pm.getRows(),
                pm.getSort(), pm.getOrder());
        List<CandidateStudentListModel> candidateStudents = new LinkedList<CandidateStudentListModel>();
        for (Will w : wills)
        {
            Student s = studentDaoImpl.findById(w.getStudentId());
            CandidateStudentListModel model = new CandidateStudentListModel(w.getId(), s.getId(), s.getAccount(),
                    s.getName(),
                    s.getEmail(), s.getPhone(), w.getStatus().getValue());
            candidateStudents.add(model);
        }
        int count = willDaoImpl.findForCount(
                "select w from Will w where w.teacherId=:teacherId and w.level=:level", params);
        return new GenericDataGrid<CandidateStudentListModel>(count, candidateStudents);
    }

    /**
     * Description: TODO
     * 
     * @param willId
     * @param decision
     * @return
     *         BasicJson
     */
    public BasicJson changeWillStatus(int willId, String decision) {
        // TODO Auto-generated method stub
        Will w = willDaoImpl.findById(willId);
        w.setStatus(WillStatusEnum.getType(decision));
        willDaoImpl.mergeWithTransaction(w);
        Student s = studentDaoImpl.findById(w.getStudentId());
        Teacher t = teacherDaoImpl.findById(w.getTeacherId());
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("studentId", s.getId());
        List<Will> wills = willDaoImpl.findForPaging("select w from Will w where w.studentId=:studentId", params);
        MatchLevelEnum matchLevel = MatchLevelEnum.调剂;
        for (Will one : wills)
        {
            if (one.getTeacherId() == t.getId())
                matchLevel = MatchLevelEnum.getTypeByIntLevel(one.getLevel());
        }
        if (StringUtils.equals("接受", decision)) {
            s.setMatchLevel(matchLevel);
            s.setMatchType(MatchTypeEnum.教师接受);
            t.addStudent(s);
        }
        if (StringUtils.equals("拒绝", decision)) {
            s.setMatchLevel(null);
            s.setMatchType(null);
            t.removeStudent(s);
        }
        teacherDaoImpl.mergeWithTransaction(t);
        // 需要手工merge一下Student，因为不能设置Teacher的List<Student>为CascadeType.ALL
        studentDaoImpl.mergeWithTransaction(s);
        return new BasicJson(true, "已" + decision, null);
    }

    /**
     * Description:
     * 
     * @param studentId
     * @return
     *         StudentDetail
     */
    public StudentDetail getStudentDetail(int studentId)
    {
        Student s = studentDaoImpl.findById(studentId);
        StudentDetail sd = new StudentDetail(s.getId(), s.getName(), s.getEmail(), s.getGender(), s.getPhone(),
                s.getSelfDescription());
        return sd;
    }

    public static class StudentDetail
    {
        private int id;
        private String name;
        private String email;
        private String gender;
        private String phone;
        private String selfDescription;

        public StudentDetail(int id, String name, String email, String gender, String phone, String selfDescription) {
            super();
            this.id = id;
            this.name = name;
            this.email = email;
            this.gender = gender;
            this.phone = phone;
            this.selfDescription = selfDescription;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getselfDescription() {
            return selfDescription;
        }

        public void setselfDescription(String selfDescription) {
            this.selfDescription = selfDescription;
        }

    }
}
