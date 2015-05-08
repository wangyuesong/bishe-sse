package sse.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import sse.dao.base.GenericDao;
import sse.entity.Student;

/**
 * @author yuesongwang
 *
 */
@Repository
public class StudentDaoImpl extends GenericDao<Integer, Student>
{
    public List<Student> findStudentsWhoHaveATeacher()
    {
        return this.findForPaging("select s from Student s where s.teacher is not NULL");
    }
}
