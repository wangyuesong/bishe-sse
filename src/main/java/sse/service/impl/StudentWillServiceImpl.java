package sse.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import sse.commandmodel.WillModel;
import sse.dao.impl.ActionEventDaoImpl;
import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.ActionEvent;
import sse.entity.Teacher;
import sse.entity.Will;
import sse.pageModel.ActionEventModel;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.TeacherListModel;
import sse.utils.DateTimeUtil;
import sse.utils.PaginationAndSortModel;

@Service
public class StudentWillServiceImpl {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StudentWillServiceImpl.class);

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    @Autowired
    private ActionEventDaoImpl actionEventDaoImpl;

    @Autowired
    private WillDaoImpl willDaoImpl;

    public GenericDataGrid<TeacherListModel> findTeachersForPagingInGenericDataGrid(PaginationAndSortModel pam)
    {
        GenericDataGrid<TeacherListModel> dg = new GenericDataGrid<>();
        List<Teacher> teacherList = teacherDaoImpl.findForPaging("select t from Teacher t",
                new HashMap<String, Object>(), pam.getPage(),
                pam.getRows(), pam.getSort(), pam.getOrder());
        List<TeacherListModel> teacherModelList = new LinkedList<TeacherListModel>();
        for (Teacher t : teacherList)
        {
            TeacherListModel tl = new TeacherListModel(t.getId(), t.getName(), t.getAccount(), t.getCapacity(),
                    t.getGender(),
                    t.getEmail(),
                    t.getPhone());
            teacherModelList.add(tl);
        }
        dg.setRows(teacherModelList);
        dg.setTotal(teacherDaoImpl.findForCount("select t from Teacher t", null));
        return dg;
    }

    public List<TeacherListModel> findTeachersForList()
    {
        List<Teacher> teacherList = teacherDaoImpl.findAll();
        List<TeacherListModel> teacherModelList = new LinkedList<TeacherListModel>();
        for (Teacher t : teacherList)
        {
            TeacherListModel tl = new TeacherListModel(t.getId(), t.getName(), t.getAccount(), t.getCapacity(),
                    t.getGender(),
                    t.getEmail(),
                    t.getPhone());
            teacherModelList.add(tl);
        }
        return teacherModelList;
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
                returnMap.put("" + w.getLevel(), w.getTeacherId() + "");
            }
            return returnMap;
        }
        else
            return null;
    }

    public void updateSelection(WillModel model)
    {
        willDaoImpl.updateSelection(model);
    }

    /**
     * @Method: findTeachersActionEventsForPagingInGenericDataGrid
     * @Description: 查看自己老师最近的所有操作，只能看到老师的公共操作或者和自己的有关的操作
     * @param @param pam
     * @param @return
     * @return GenericDataGrid<ActionEventModel>
     * @throws
     */
    public GenericDataGrid<ActionEventModel> findTeachersActionEventsForPagingInGenericDataGrid(int teacherId,
            int studentId,
            PaginationAndSortModel pam) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("actor", teacherId);
        params.put("listener", studentId);
        List<ActionEvent> actionEvents = actionEventDaoImpl
                .findForPaging(
                        "select ae from ActionEvent ae where ae.actor.id=:actor and (ae.listener.id=:listener or ae.listener is NULL)",
                        params, pam.getPage(),
                        pam.getRows(),
                        "ae." + pam.getSort(), pam.getOrder());

        List<ActionEventModel> actionEventModels = new ArrayList<ActionEventModel>();
        for (ActionEvent e : actionEvents)
        {
            ActionEventModel aem = new ActionEventModel(e.getId(), e.getActor().getName(), DateTimeUtil.formatToMin(e
                    .getActor()
                    .getCreateTime()), e.getDescription());
            actionEventModels.add(aem);
        }
        GenericDataGrid<ActionEventModel> dataGrid = new GenericDataGrid<ActionEventModel>();
        dataGrid.setRows(actionEventModels);
        dataGrid.setTotal(actionEventDaoImpl
                .findForCount(
                        "select ae from ActionEvent ae where ae.actor.id=:actor and (ae.listener.id=:listener or ae.listener is NULL)",
                        params));
        return dataGrid;
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
