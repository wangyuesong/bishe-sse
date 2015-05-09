package sse.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.entity.User;
import sse.pageModel.DocumentCommentListModel;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.StudentListModel;
import sse.service.impl.StudentDocumentServiceImpl;
import sse.service.impl.StudentDocumentServiceImpl.DocumentInfo;
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
    public DocumentInfo getOneStudentDocuments(HttpServletRequest request, int studentId, String type)
    {
        return studentDocumentServiceImpl.getDocumentInfoByStudentIdAndDocumentType(studentId, type);
    }

    @ResponseBody
    @RequestMapping(value = "/getDocumentComments")
    public List<DocumentCommentListModel> getDocumentComments(HttpServletRequest request, int studentId, String type)
    {
        return studentDocumentServiceImpl.findDocumentComments(studentId, type);

    }

}
