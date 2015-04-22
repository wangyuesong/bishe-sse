package sse.service.impl;

import java.util.HashMap;
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
import sse.jsonmodel.TeacherListModel;
import sse.pageModel.DataGrid;
import sse.pageModel.WillModel;
import sse.service.impl.AdminServiceImpl.MatchPair;

@Service
public class StudentServiceImpl {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    @Autowired
    private WillDaoImpl willDaoImpl;

    public DataGrid<TeacherListModel> findTeachersForPaging(int pageSize, int page, String sortCriteria, String order)
    {
        DataGrid<TeacherListModel> dg = new DataGrid<>();
        dg.setRows(teacherDaoImpl.findTeachersForPaging(pageSize, page, sortCriteria, order));
        dg.setTotal(teacherDaoImpl.findTeachersForCount());
        return dg;
    }

    public TeacherDetail findOneTeacherDetailByTeacherId(int teacherId)
    {
        Teacher t = teacherDaoImpl.findById(teacherId);
        TeacherDetail td = new TeacherDetail(t.getName(), t.getEmail(), t.getGender(), t.getPhone(), t.getCapacity()
                + "", t.getDegree(), t.getTitle(), t.getSelfDescription(), t.getCandidateTopics(), t.getDirection());
        return td;
    }

    public HashMap<String, String> findPreviousWills(int studentId)
    {
        return willDaoImpl.findPreviousSelectionByStudentId(studentId);
    }

    public void updateSelection(WillModel model, int studentId)
    {
        willDaoImpl.updateSelection(model, studentId);
    }

    /**
     * @Method: findCurrentMatchCondition
     * @Description: TODO
     * @param @return
     * @return List<MatchPair>
     * @throws
     */
    public List<MatchPair> findCurrentMatchCondition() {
        List<Student> allStudents = studentDaoImpl.findAll();
        List<MatchPair> matchPairs = new LinkedList<MatchPair>();
        for (Student s : allStudents)
        {
            if (s.getTeacher() != null)
                matchPairs.add(new MatchPair(s, s.getTeacher(), s.getMatchType()));
        }
        return matchPairs;
    }

    public static class TeacherDetail
    {

        private String name;
        private String email;
        private String gender;
        private String phone;
        private String capacity;
        private String degree;
        private String title;
        private String self_description;
        private String candidateTopics;
        private String direction;

        public TeacherDetail(String name, String email, String gender, String phone, String capacity, String degree,
                String title, String self_description, String candidateTopics, String direction) {
            super();
            this.name = name;
            this.email = email;
            this.gender = gender;
            this.phone = phone;
            this.capacity = capacity;
            this.degree = degree;
            this.title = title;
            this.self_description = self_description;
            this.candidateTopics = candidateTopics;
            this.direction = direction;
        }

        public String getCandidateTopics() {
            return candidateTopics;
        }

        public void setCandidateTopics(String candidateTopics) {
            this.candidateTopics = candidateTopics;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
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

        public String getCapacity() {
            return capacity;
        }

        public void setCapacity(String capacity) {
            this.capacity = capacity;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSelf_description() {
            return self_description;
        }

        public void setSelf_description(String self_description) {
            this.self_description = self_description;
        }

    }

}
