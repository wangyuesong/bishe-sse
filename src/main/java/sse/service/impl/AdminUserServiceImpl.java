package sse.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.commandmodel.BasicJson;
import sse.commandmodel.MatchPair;
import sse.dao.impl.StudentDaoImpl;
import sse.entity.Student;
import sse.entity.TimeNode;
import sse.enums.TimeNodeTypeEnum;
import sse.exception.SSEException;
import sse.pagemodel.GenericDataGrid;
import sse.pagemodel.StudentCURDModel;
import sse.pagemodel.TimeNodeListModel;
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

    /**
     * Description: 返回所有Student
     * 
     * @param pam
     * @return
     *         GenericDataGrid<StudentCURDModel>
     */
    public GenericDataGrid<StudentCURDModel> findAllStudents(PaginationAndSortModel pam) {
        // 目前还仅支持用学排序，因为Model和Entity中的字段名不一致
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
}
