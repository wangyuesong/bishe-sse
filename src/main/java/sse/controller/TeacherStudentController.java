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

import sse.commandmodel.BasicJson;
import sse.enums.AttachmentStatusEnum;
import sse.enums.DocumentTypeEnum;
import sse.pageModel.CandidateStudentListModel;
import sse.pageModel.DocumentCommentListModel;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.StudentListModel;
import sse.service.impl.StudentDocumentServiceImpl;
import sse.service.impl.StudentDocumentServiceImpl.DocumentInfo;
import sse.service.impl.StudentDocumentServiceImpl.SimpleAttachmentInfo;
import sse.service.impl.TeacherStudentServiceImpl;
import sse.service.impl.TeacherStudentServiceImpl.StudentDetail;
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
        GenericDataGrid<StudentListModel> s = teacherStudentServiceImpl.getMyStudentsForPagingInGenericDataGrid(
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
        return studentDocumentServiceImpl.getAttachmentsOfAUserByTypeAndAttachmentStatus(studentId,
                // Get方法传中文乱码，暂时用拼音
                DocumentTypeEnum.getTypeByPinYin(type), AttachmentStatusEnum.FOREVER);
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
