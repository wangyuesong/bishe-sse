package sse.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sse.commandmodel.BasicJson;
import sse.commandmodel.DocumentCommentFormModel;
import sse.commandmodel.DocumentFormModel;
import sse.enums.AttachmentStatusEnum;
import sse.enums.DocumentTypeEnum;
import sse.exception.SSEException;
import sse.pagemodel.DocumentCommentListModel;
import sse.pagemodel.DocumentListModel;
import sse.pagemodel.GenericDataGrid;
import sse.pagemodel.TopicDetailModel;
import sse.service.impl.DocumentSerivceImpl;
import sse.service.impl.DocumentSerivceImpl.AttachmentInfo;
import sse.service.impl.DocumentSerivceImpl.DocumentInfo;
import sse.service.impl.DocumentSerivceImpl.SimpleAttachmentInfo;
import sse.service.impl.StudentTopicServiceImpl;
import sse.service.impl.UserServiceImpl;

/**
 * @Project: sse
 * @Title: DocumentController.java
 * @Package sse.controller
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月13日 上午10:41:39
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/student/topic")
public class StudentTopicController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StudentTopicController.class);

    @Autowired
    public StudentTopicServiceImpl studentTopicServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/getTopic", method = { RequestMethod.POST })
    public TopicDetailModel getTopic(int studentId)
    {
        return studentTopicServiceImpl.getTopicByStudentId(studentId);
    }

    @ResponseBody
    @RequestMapping(value = "/saveTopic")
    public BasicJson saveTopic(TopicDetailModel tm, int studentId)
    {
        studentTopicServiceImpl.saveTopic(tm, studentId);
        return new BasicJson(true, "保存成功", null);
    }
}
