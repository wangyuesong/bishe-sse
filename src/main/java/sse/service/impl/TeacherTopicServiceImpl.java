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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.commandmodel.BasicJson;
import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.dao.impl.TopicDaoImpl;
import sse.entity.Student;
import sse.entity.Teacher;
import sse.entity.Topic;
import sse.enums.TopicStatusEnum;
import sse.pagemodel.GenericDataGrid;
import sse.pagemodel.TopicDetailModel;
import sse.pagemodel.TopicListModel;

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
public class TeacherTopicServiceImpl {

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    @Autowired
    private TopicDaoImpl topicDaoImpl;

    /**
     * Description: 列出教师的所有学生的选题,不支持排序分页
     * 
     * @param teacherId
     * @param pam
     * @return
     *         GenericDataGrid<TopicListModel>
     */
    public GenericDataGrid<TopicListModel> getMyStudentTopics(int teacherId) {
        Teacher t = teacherDaoImpl.findById(teacherId);
        teacherDaoImpl.refresh(t);
        List<Student> students = t.getStudents();
        List<Topic> topics = new ArrayList<Topic>();
        
        for (Student s : students)
        {
            Topic p = s.getTopic();
            if (p != null)
                topics.add(p);
        }
        List<TopicListModel> listModels = new ArrayList<TopicListModel>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Topic p : topics)
        {
            listModels.add(new TopicListModel(p.getId(), p.getStudent().getId(), p.getStudent().getName(), p
                    .getMainName(), p.getSubName(), sdf
                    .format(p.getCreateTime()), sdf.format(p.getUpdateTime()), p.getPassStatus().getValue()));
        }
        int count = listModels.size();
        return new GenericDataGrid<TopicListModel>(count, listModels);
    }

    public TopicDetailModel getTopicDetail(int topicId)
    {
        Topic t = topicDaoImpl.findById(topicId);
        TopicDetailModel tm = new TopicDetailModel(t.getId(), t.getDescription(), t.getMainName(), t.getSubName(),
                t.getOutsider(), t.getPassStatus().getValue(), t.getTeacherComment(), t.getTopicType().getValue());
        return tm;
    }

    /**
     * Description: TODO
     * 
     * @param tm
     * @param topicId
     *            void
     */
    public BasicJson saveTopic(String passStatus, String teacherComment, int topicId) {
        Topic t = topicDaoImpl.findById(topicId);
        t.setPassStatus(TopicStatusEnum.getType(passStatus));
        t.setTeacherComment(teacherComment);
        topicDaoImpl.mergeWithTransaction(t);
        return new BasicJson(true, "保存成功", null);
    }
}
