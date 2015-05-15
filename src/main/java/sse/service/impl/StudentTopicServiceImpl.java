package sse.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TopicDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.Student;
import sse.entity.Topic;
import sse.enums.TopicStatusEnum;
import sse.enums.TopicTypeEnum;
import sse.pagemodel.TopicModel;

/**
 * @author yuesongwang
 *
 */
@Service
public class StudentTopicServiceImpl {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StudentTopicServiceImpl.class);

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    @Autowired
    private TopicDaoImpl topicDaoImpl;

    @Autowired
    private WillDaoImpl willDaoImpl;

    /**
     * Description: 根据学生id查到其topic，若无则返回null
     * 
     * @param studentId
     * @return
     *         TopicModel
     */
    public TopicModel getTopicByStudentId(int studentId)
    {
        Topic t = studentDaoImpl.findById(studentId).getTopic();
        if (t == null)
            return null;
        TopicModel tm = new TopicModel(t.getId(), t.getDescription(), t.getMainName(), t.getSubName(), t.getOutsider(),
                t.getPassStatus().getValue(), t.getTeacherComment(), t.getTopicType().getValue());
        return tm;
    }

    public void saveTopic(TopicModel tm, int studentId)
    {
        Student s = studentDaoImpl.findById(studentId);
        Topic t = s.getTopic();
        if (t == null)
        {
            t = new Topic();
            t.setPassStatus(TopicStatusEnum.待审核);
        }
        t.setMainName(tm.getMainName());
        t.setSubName(tm.getSubName());
        t.setDescription(tm.getDescription());
        t.setOutsider(tm.getOutsider());
        t.setTopicType(TopicTypeEnum.getType(tm.getTopicType()));
        s.setTopic(t);
        studentDaoImpl.mergeWithTransaction(s);
    }

}
