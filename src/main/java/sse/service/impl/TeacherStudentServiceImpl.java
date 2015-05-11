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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.Student;
import sse.entity.Will;
import sse.pageModel.CandidateStudentListModel;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.StudentListModel;
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
                "select w from Will w where w.id.teacherId=:teacherId and w.level=:level", params, pm.getPage(),
                pm.getRows(),
                pm.getSort(), pm.getOrder());
        List<CandidateStudentListModel> candidateStudents = new LinkedList<CandidateStudentListModel>();
        for (Will w : wills)
        {
            Student s = studentDaoImpl.findById(w.getId().getStudentId());
            CandidateStudentListModel model = new CandidateStudentListModel(w.getId(), s.getId(), s.getAccount(),
                    s.getName(),
                    s.getEmail(), s.getPhone(), w.getStatus().getValue());
            candidateStudents.add(model);
        }
        int count = willDaoImpl.findForCount(
                "select w from Will w where w.id.teacherId=:teacherId and w.level=:level", params);
        return new GenericDataGrid<CandidateStudentListModel>(count, candidateStudents);
    }
}
