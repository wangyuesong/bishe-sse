package sse.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.entity.User;
import sse.enums.AttachmentStatusEnum;
import sse.enums.DocumentTypeEnum;
import sse.pageModel.DocumentCommentListModel;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.StudentListModel;
import sse.service.impl.StudentDocumentServiceImpl;
import sse.service.impl.StudentDocumentServiceImpl.DocumentInfo;
import sse.service.impl.StudentDocumentServiceImpl.SimpleAttachmentInfo;
import sse.service.impl.TeacherStudentServiceImpl;
import sse.utils.PaginationAndSortModel;
import sse.utils.SessionUtil;

@Controller
@RequestMapping(value = "/teacher/student/")
public class TeacherStudentController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TeacherStudentController.class);

    @Autowired
    private TeacherStudentServiceImpl teacherStudentServiceImpl;

    @Autowired
    private StudentDocumentServiceImpl studentDocumentServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/getMyStudentsInDatagrid")
    public GenericDataGrid<StudentListModel> getMyStudentsInDatagrid(HttpServletRequest request)
    {
        PaginationAndSortModel pam = new PaginationAndSortModel(request);
        int teacherId = SessionUtil.getUserFromSession(request).getId();
        GenericDataGrid<StudentListModel> s = teacherStudentServiceImpl.findMyStudentsForPagingInGenericDataGrid(
                teacherId, pam);
        return s;
    }

    /**
     * Description: 根据参数学生id studentId和文档类型type来获取学生的某个文档，如果该文档尚未创建，返回null
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
    public List<DocumentCommentListModel> getDocumentComments(HttpServletRequest request, int studentId, String type)
    {
        return studentDocumentServiceImpl.findDocumentComments(studentId, type);

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
    @RequestMapping(value = "/getAllForeverAttachments", method = { RequestMethod.GET, RequestMethod.POST })
    public List<SimpleAttachmentInfo> getAllForeverAttachments(int studentId, String type, HttpServletRequest request,
            HttpServletResponse response) {
        return studentDocumentServiceImpl.getAttachmentsOfAUserByTypeAndAttachmentStatus(studentId,
                DocumentTypeEnum.getType(type), AttachmentStatusEnum.FOREVER);
    }

}