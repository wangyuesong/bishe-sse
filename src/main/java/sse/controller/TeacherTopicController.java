package sse.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.commandmodel.BasicJson;
import sse.enums.AttachmentStatusEnum;
import sse.enums.DocumentTypeEnum;
import sse.pagemodel.CandidateStudentListModel;
import sse.pagemodel.DocumentCommentListModel;
import sse.pagemodel.GenericDataGrid;
import sse.pagemodel.StudentListModel;
import sse.pagemodel.TimeNodeListModel;
import sse.pagemodel.TopicListModel;
import sse.pagemodel.TopicDetailModel;
import sse.service.impl.DocumentSerivceImpl;
import sse.service.impl.TeacherTopicServiceImpl;
import sse.service.impl.DocumentSerivceImpl.DocumentInfo;
import sse.service.impl.DocumentSerivceImpl.SimpleAttachmentInfo;
import sse.service.impl.TeacherStudentServiceImpl;
import sse.service.impl.TeacherStudentServiceImpl.StudentDetail;
import sse.utils.PaginationAndSortModel;
import sse.utils.SessionUtil;

@Controller
@RequestMapping(value = "/teacher/topic/")
public class TeacherTopicController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TeacherTopicController.class);

    @Autowired
    private TeacherTopicServiceImpl teacherTopicServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/getMyStudentTopics", method = { RequestMethod.POST })
    public GenericDataGrid<TopicListModel> getMyStudentTopics(int teacherId, HttpServletRequest request)
    {
        return teacherTopicServiceImpl.getMyStudentTopics(teacherId);
    }

    @ResponseBody
    @RequestMapping(value = "/getTopic", method = { RequestMethod.POST })
    public TopicDetailModel getTopic(int topicId)
    {
        return teacherTopicServiceImpl.getTopicDetail(topicId);
    }

    @ResponseBody
    @RequestMapping(value = "/saveTopic")
    public BasicJson saveTopic(String passStatus, String teacherComment, int topicId)
    {
        return teacherTopicServiceImpl.saveTopic(passStatus, teacherComment, topicId);
    }
}
