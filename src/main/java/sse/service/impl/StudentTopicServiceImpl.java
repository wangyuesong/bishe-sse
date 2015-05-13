package sse.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import sse.commandmodel.DocumentFormModel;
import sse.commandmodel.WillModel;
import sse.dao.impl.AttachmentDaoImpl;
import sse.dao.impl.DocumentDaoImpl;
import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.dao.impl.TopicDaoImpl;
import sse.dao.impl.UserDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.Attachment;
import sse.entity.Document;
import sse.entity.DocumentComment;
import sse.entity.Student;
import sse.entity.Teacher;
import sse.entity.Topic;
import sse.entity.User;
import sse.entity.Will;
import sse.enums.AttachmentStatusEnum;
import sse.enums.DocumentTypeEnum;
import sse.enums.TopicStatusEnum;
import sse.enums.TopicTypeEnum;
import sse.exception.SSEException;
import sse.pageModel.DocumentCommentListModel;
import sse.pageModel.DocumentListModel;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.TopicModel;
import sse.utils.FtpTool;

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
        Topic t = studentDaoImpl.findById(studentId).getTopic();
        if (t == null)
            t = new Topic();
        t.setMainName(tm.getMainName());
        t.setSubName(tm.getSubName());
        t.setDescription(tm.getDescription());
        t.setOutsider(tm.getOutsider());
        t.setTopicType(TopicTypeEnum.getType(tm.getTopicType()));
        topicDaoImpl.mergeWithTransaction(t);
    }

}
