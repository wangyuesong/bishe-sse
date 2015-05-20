package sse.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.commandmodel.BasicJson;
import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.dao.impl.UserDaoImpl;
import sse.entity.Student;
import sse.entity.Teacher;
import sse.entity.User;
import sse.pagemodel.GenericDataGrid;
import sse.utils.PropertiesUtil;

@Service
public class PersonalInfoServiceImpl {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PersonalInfoServiceImpl.class);

    @Autowired
    private UserDaoImpl userDaoImpl;

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    /**
     * Description: TODO
     * 
     * @param userId
     * @return
     *         GenericDataGrid<PersonalInfoModel>
     */
    public GenericDataGrid<PersonalInfoEntryModel> getPersonalInfo(int userId) {
        User u = userDaoImpl.findById(userId);

        List<PersonalInfoEntryModel> models = new ArrayList<PersonalInfoEntryModel>();
        if (StringUtils.equals(u.getRole(), "Student"))
        {
            Student s = (Student) u;
            models.add(new PersonalInfoEntryModelWithSimpleEditor("姓名", s.getName(), "基本信息", "text"));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("性别", s.getGender(), "基本信息", "text"));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("密码", s.getPassword(), "基本信息", "text"));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("手机", s.getPhone(), "基本信息", "numberbox"));
            models.add(new PersonalInfoEntryModelWithComplexEditor("Email", s.getEmail(), "基本信息",
                    constructEmailValidator()));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("绩点", s.getGpa(), "其他信息", null));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("自我介绍", s.getSelfDescription(), "其他信息", "textarea"));

        }
        if (StringUtils.equals(u.getRole(), "Teacher"))
        {
            Teacher t = (Teacher) u;
            models.add(new PersonalInfoEntryModelWithSimpleEditor("姓名", t.getName(), "基本信息", "text"));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("性别", t.getGender(), "基本信息", "text"));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("密码", t.getPassword(), "基本信息", "text"));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("手机", t.getPhone(), "基本信息", "numberbox"));
            models.add(new PersonalInfoEntryModelWithComplexEditor("Email", t.getEmail(), "基本信息",
                    constructEmailValidator()));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("指导容量", t.getCapacity() + "", "基本信息", "numberspinner"));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("学历", t.getDegree(), "其他信息", "text"));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("职称", t.getTitle(), "其他信息", "text"));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("自我介绍", t.getSelfDescription(), "其他信息", "textarea"));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("专业方向", t.getDirection(), "其他信息", "textarea"));
            models.add(new PersonalInfoEntryModelWithSimpleEditor("备选题目", t.getCandidateTopics(), "其他信息", "textarea"));
        }

        int count = models.size();
        return new GenericDataGrid<PersonalInfoEntryModel>(count, models);
    }

    private HashMap<String, Object> constructEmailValidator()
    {
        HashMap<String, Object> emailValidator = new HashMap<String, Object>();
        HashMap<String, String> emailValidatorOptions = new HashMap<String, String>();
        emailValidatorOptions.put("validType", "email");
        emailValidator.put("type", "validatebox");
        emailValidator.put("options", emailValidatorOptions);
        return emailValidator;
    }

    /**
     * Description: 保存学生信息
     * 
     * @param studentId
     * @param models
     * @return
     *         GenericDataGrid<PersonalInfoEntryModel>
     */
    public BasicJson saveStudentPersonalInfo(int studentId,
            List<PersonalInfoEntryModel> models) {
        Student s = studentDaoImpl.findById(studentId);
        for (PersonalInfoEntryModel model : models)
        {
            // 从Model中的中文字段翻译到Entity中的字段
            String columnName = PropertiesUtil.readProperty("/entity_model_dictionary.properties", model.getName());
            sse.utils.ClassUtil<Student> util = new sse.utils.ClassUtil<Student>();
            util.callSetMethod(s, columnName, model.getValue());
        }
        studentDaoImpl.mergeWithTransaction(s);
        // 更改了Student不够！需要将其对应的UserEntity 也同步
        userDaoImpl.refresh(userDaoImpl.findById(s.getId()));
        return new BasicJson(true, "修改完成", null);
    }

    public BasicJson saveTeacherPersonalInfo(int teacherId,
            List<PersonalInfoEntryModel> models) {
        Teacher t = teacherDaoImpl.findById(teacherId);
        for (PersonalInfoEntryModel model : models)
        {
            // 从Model中的中文字段翻译到Entity中的字段
            String columnName = PropertiesUtil.readProperty("/entity_model_dictionary.properties", model.getName());
            // 反射注入属性
            sse.utils.ClassUtil<Teacher> util = new sse.utils.ClassUtil<Teacher>();
            util.callSetMethod(t, columnName, model.getValue());
        }
        teacherDaoImpl.mergeWithTransaction(t);
        // 更改了Student不够！需要将其对应的UserEntity 也同步
        userDaoImpl.refresh(userDaoImpl.findById(t.getId()));
        return new BasicJson(true, "修改完成", null);
    }

    public static class PersonalInfoEntryModel {
        String name;
        String value;
        String group;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public PersonalInfoEntryModel(String name, String value, String group) {
            super();
            this.name = name;
            this.value = value;
            this.group = group;
        }
    }

    public static class PersonalInfoEntryModelWithComplexEditor extends PersonalInfoEntryModel {

        private HashMap<String, Object> editor;

        public HashMap<String, Object> getEditor() {
            return editor;
        }

        public void setEditor(HashMap<String, Object> editor) {
            this.editor = editor;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public PersonalInfoEntryModelWithComplexEditor(String name, String value, String group,
                HashMap<String, Object> editor) {
            super(name, value, group);
            this.editor = editor;
        }
    }

    public static class PersonalInfoEntryModelWithSimpleEditor extends PersonalInfoEntryModel {

        private String editor;

        public String getEditor() {
            return editor;
        }

        public void setEditor(String editor) {
            this.editor = editor;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public PersonalInfoEntryModelWithSimpleEditor(String name, String value, String group,
                String editor) {
            super(name, value, group);
            this.editor = editor;
        }
    }

}
