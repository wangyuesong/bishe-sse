package sse.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.commandmodel.BasicJson;
import sse.commandmodel.PaginationAndSortModel;
import sse.enums.AttachmentStatusEnum;
import sse.enums.DocumentTypeEnum;
import sse.pagemodel.ActionEventListModel;
import sse.pagemodel.CandidateStudentListModel;
import sse.pagemodel.DocumentCommentListModel;
import sse.pagemodel.GenericDataGrid;
import sse.pagemodel.StudentListModel;
import sse.service.impl.DocumentSerivceImpl;
import sse.service.impl.DocumentSerivceImpl.DocumentInfo;
import sse.service.impl.DocumentSerivceImpl.SimpleAttachmentInfo;
import sse.service.impl.TeacherStudentServiceImpl;
import sse.service.impl.TeacherStudentServiceImpl.StudentDetail;
import sse.utils.SessionUtil;

@Controller
@RequestMapping(value = "/teacher/student/")
public class TeacherStudentController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TeacherStudentController.class);

    @Autowired
    private TeacherStudentServiceImpl teacherStudentServiceImpl;

    @Autowired
    private DocumentSerivceImpl studentDocumentServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/getMyStudentsInDatagrid")
    public GenericDataGrid<StudentListModel> getMyStudentsInDatagrid(HttpServletRequest request)
    {
        PaginationAndSortModel pam = new PaginationAndSortModel(request);
        int teacherId = SessionUtil.getUserFromSession(request).getId();
        GenericDataGrid<StudentListModel> s = teacherStudentServiceImpl.getMyStudentsForPagingInGenericDataGrid(
                teacherId, pam);
        return s;
    }

    @ResponseBody
    @RequestMapping(value = "/getStudentActionEventsByStudentId")
    public GenericDataGrid<ActionEventListModel> getStudentActionEventsByStudentId(int studentId,
            HttpServletRequest request,
            HttpServletResponse response) {
        PaginationAndSortModel pam = new PaginationAndSortModel(request);
        return teacherStudentServiceImpl.getStudentActionEventsByStudentId(studentId, pam);
    }

    /**
     * Description: 根据参数学生id studentId和文档类型type来获取学生的某个文档
     * 
     * @param request
     * @param studentId
     * @param type
     * @return
     *         DocumentInfo
     */
    @ResponseBody
    @RequestMapping(value = "/getOneStudentDocument")
    public DocumentInfo getOneStudentDocuments(HttpServletRequest request, String studentId, String type)
    {
        String typae = type;
        int studentIdI = Integer.parseInt(studentId);
        DocumentInfo info = studentDocumentServiceImpl.getDocumentInfoByStudentIdAndDocumentType(studentIdI, type);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/getDocumentComments")
    public GenericDataGrid<DocumentCommentListModel> getDocumentComments(HttpServletRequest request, int studentId,
            String type)
    {
        return studentDocumentServiceImpl.findDocumentCommentsForPagingByStudentIdAndDocumentType(studentId, type);
    }

    /**
     * Description:教师根据学生Id和所需文档类型type获取该文档的所有附件
     * 
     * @param type
     * @param request
     * @param response
     * @return
     *         List<SimpleAttachmentInfo>
     */
    @ResponseBody
    @RequestMapping(value = "/getAllForeverAttachments")
    public List<SimpleAttachmentInfo> getAllForeverAttachments(String type, int studentId, HttpServletRequest request,
            HttpServletResponse response) {
        return studentDocumentServiceImpl.getAttachmentsOfStudentByUserIdDocumentTypeAndAttachmentStatus(studentId,
                DocumentTypeEnum.getType(type), AttachmentStatusEnum.FOREVER);
    }

    @ResponseBody
    @RequestMapping(value = "/getCandidateStudents")
    public GenericDataGrid<CandidateStudentListModel> getCandidateStudents(int teacherId, HttpServletRequest request,
            HttpServletResponse response) {
        PaginationAndSortModel pm = new PaginationAndSortModel(request);
        return teacherStudentServiceImpl.getCandidateStudentsForPagingInDataGrid(teacherId, pm);
    }

    @ResponseBody
    @RequestMapping(value = "/showOneStudentDetail")
    public StudentDetail showOneStudentDetail(String studentId)
    {
        return teacherStudentServiceImpl.getStudentDetail(Integer.parseInt(studentId));
    }

    @ResponseBody
    @RequestMapping(value = "/changeWillStatus")
    public BasicJson changeWillStatus(int willId, String decision, HttpServletRequest request,
            HttpServletResponse response) {
        return teacherStudentServiceImpl.changeWillStatus(willId, decision);
    }

}
