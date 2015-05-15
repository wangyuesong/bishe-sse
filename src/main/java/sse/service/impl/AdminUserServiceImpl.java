package sse.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.commandmodel.BasicJson;
import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.entity.Student;
import sse.entity.Teacher;
import sse.pagemodel.GenericDataGrid;
import sse.pagemodel.StudentCURDModel;
import sse.pagemodel.TeacherCURDModel;
import sse.utils.PaginationAndSortModel;

/**
 * @Project: sse
 * @Title: AdminUserServiceImpl.java
 * @Package sse.service.impl
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月15日 下午4:42:52
 * @version V1.0
 */
@Service
public class AdminUserServiceImpl {

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    /**
     * Description: 返回所有Student
     * 
     * @param pam
     * @return
     *         GenericDataGrid<StudentCURDModel>
     */
    public GenericDataGrid<StudentCURDModel> findAllStudents(PaginationAndSortModel pam) {
        List<Student> allStudents = studentDaoImpl.findForPaging("select s from Student s", null, pam.getPage(),
                pam.getRows(),
                "s." + pam.getSort(), pam.getOrder());
        int total = studentDaoImpl.findForCount("select s from Student s", null);

        List<StudentCURDModel> models = new LinkedList<StudentCURDModel>();
        for (Student s : allStudents)
        {
            models.add(new StudentCURDModel(s.getId(), s.getAccount(), s.getName(), s.getEmail(), s.getPhone(), s
                    .getPassword(), s.getSelfDescription(), s.getGender(), s.getGpa()));
        }
        GenericDataGrid<StudentCURDModel> genericDataGrid = new GenericDataGrid<StudentCURDModel>();
        genericDataGrid.setRows(models);
        genericDataGrid.setTotal(total);
        return genericDataGrid;
    }

    /**
     * Description: TODO
     * 
     * @param models
     * @return
     *         BasicJson
     */
    public BasicJson changeStudents(List<StudentCURDModel> models) {
        for (StudentCURDModel m : models)
        {
            Student t;
            // 新建
            if (m.getId() == 0)
                t = new Student();
            else
                t = studentDaoImpl.findById(m.getId());
            t.setAccount(m.getAccount());
            t.setName(m.getName());
            t.setEmail(m.getEmail());
            t.setPhone(m.getPhone());
            t.setPassword(m.getPassword());
            t.setSelfDescription(m.getSelfDescription());
            t.setGender(m.getGender());
            t.setGpa(m.getGpa());
            studentDaoImpl.mergeWithTransaction(t);
        }
        return new BasicJson(true, "保存成功", null);
    }

    /**
     * Description: TODO
     * 
     * @param studentId
     * @return
     *         BasicJson
     */
    public BasicJson deleteStudent(int studentId) {
        studentDaoImpl.removeWithTransaction(studentDaoImpl.findById(studentId));
        return new BasicJson(true, "删除成功", null);
    }

    /**
     * Description: TODO
     * 
     * @param models
     * @return
     *         BasicJson
     */
    public BasicJson changeTeachers(List<TeacherCURDModel> models) {
        for (TeacherCURDModel m : models)
        {
            Teacher t;
            // 新建
            if (m.getId() == 0)
                t = new Teacher();
            else
                t = teacherDaoImpl.findById(m.getId());
            t.setAccount(m.getAccount());
            t.setName(m.getName());
            t.setEmail(m.getEmail());
            t.setPhone(m.getPhone());
            t.setPassword(m.getPassword());
            t.setSelfDescription(m.getSelfDescription());
            t.setGender(m.getGender());
            t.setCapacity(m.getCapcacity());
            t.setDegree(m.getDegree());
            t.setTitle(m.getTitle());
            t.setDirection(m.getDirection());
            t.setCandidateTopics(m.getCandidateTopics());
            teacherDaoImpl.mergeWithTransaction(t);
        }
        return new BasicJson(true, "保存成功", null);
    }

    /**
     * Description: TODO
     * 
     * @param paginationAndSortModel
     * @return
     *         GenericDataGrid<TeacherCURDModel>
     */
    public GenericDataGrid<TeacherCURDModel> findAllTeachers(PaginationAndSortModel pam) {
        List<Teacher> allTeachers = teacherDaoImpl.findForPaging("select t from Teacher t", null, pam.getPage(),
                pam.getRows(),
                "t." + pam.getSort(), pam.getOrder());
        int total = teacherDaoImpl.findForCount("select t from Teacher t", null);

        List<TeacherCURDModel> models = new LinkedList<TeacherCURDModel>();
        for (Teacher t : allTeachers)
        {
            models.add(new TeacherCURDModel(t.getId(), t.getAccount(), t.getName(), t.getEmail(), t.getPhone(), t
                    .getPassword(), t.getSelfDescription(), t.getGender(), t.getCapacity(), t.getDegree(),
                    t.getTitle(), t.getDirection(), t.getCandidateTopics()));
        }
        GenericDataGrid<TeacherCURDModel> genericDataGrid = new GenericDataGrid<TeacherCURDModel>();
        genericDataGrid.setRows(models);
        genericDataGrid.setTotal(total);
        return genericDataGrid;
    }

    /**
     * Description: TODO
     * 
     * @param teacherId
     * @return
     *         BasicJson
     */
    public BasicJson deleteTeacher(int teacherId) {
        teacherDaoImpl.removeWithTransaction(teacherDaoImpl.findById(teacherId));
        return new BasicJson(true, "删除成功", null);
    }
}
