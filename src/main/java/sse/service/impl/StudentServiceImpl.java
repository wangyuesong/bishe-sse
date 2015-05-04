package sse.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import sse.commandmodel.MatchPair;
import sse.commandmodel.WillModel;
import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.Student;
import sse.entity.Teacher;
import sse.entity.Will;
import sse.enums.MatchLevelEnum;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.TeacherListModel;
import sse.utils.ClassTool;

@Service
public class StudentServiceImpl {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    @Autowired
    private WillDaoImpl willDaoImpl;

    public GenericDataGrid<TeacherListModel> findTeachersForPagingInGenericDataGrid(int pageSize, int page,
            String sortCriteria,
            String order)
    {
        GenericDataGrid<TeacherListModel> dg = new GenericDataGrid<>();
        List<Teacher> teacherList = teacherDaoImpl.findTeachersForPaging(pageSize, page, sortCriteria, order);
        List<TeacherListModel> teacherModelList = new LinkedList<TeacherListModel>();
        ClassTool<Teacher, TeacherListModel> classTool = new ClassTool(Teacher.class, TeacherListModel.class);
        for (Teacher t : teacherList)
        {
            teacherModelList.add(classTool.convertJPAEntityToPOJO(t));
        }
        dg.setRows(teacherModelList);
        dg.setTotal(teacherDaoImpl.findTeachersForCount());
        return dg;
    }

    public TeacherDetail findOneTeacherDetailByTeacherIdInTeacherDetail(int teacherId)
    {
        Teacher t = teacherDaoImpl.findById(teacherId);
        TeacherDetail td = new TeacherDetail(t.getName(), t.getEmail(), t.getGender(), t.getPhone(), t.getCapacity()
                + "", t.getDegree(), t.getTitle(), t.getSelfDescription(), t.getCandidateTopics(), t.getDirection());
        return td;
    }

    public HashMap<String, String> findPreviousWillsInHashMap(int studentId)
    {
        List<Will> wills = willDaoImpl.findPreviousSelectionsByStudentId(studentId);
        if (!CollectionUtils.isEmpty(wills))
        {
            HashMap<String, String> returnMap = new HashMap<String, String>();
            for (Will w : wills)
            {
                Teacher t = teacherDaoImpl.findById(w.getId().getTeacherId());
                returnMap.put("" + w.getLevel(), t.getAccount());
            }
            return returnMap;
        }
        else
            return null;
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
    public List<MatchPair> findCurrentMatchConditions() {
        List<Student> allStudents = studentDaoImpl.findAll();
        List<MatchPair> matchPairs = new LinkedList<MatchPair>();
        for (Student s : allStudents)
        {
            if (s.getTeacher() != null)
                matchPairs.add(new MatchPair(s, s.getTeacher(), s.getMatchLevel(), s.getMatchType()));
            else
                matchPairs.add(new MatchPair(s, null, null, null));
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
