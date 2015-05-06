package sse.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sse.commandmodel.BasicJson;
import sse.commandmodel.DocumentFormModel;
import sse.entity.DocumentComment;
import sse.entity.User;
import sse.exception.SSEException;
import sse.pageModel.DocumentCommentListModel;
import sse.pageModel.DocumentListModel;
import sse.pageModel.GenericDataGrid;
import sse.service.impl.StudentDocumentServiceImpl;
import sse.service.impl.StudentDocumentServiceImpl.AttachmentInfo;
import sse.service.impl.StudentDocumentServiceImpl.DocumentInfo;
import sse.service.impl.StudentDocumentServiceImpl.SimpleAttachmentInfo;

/**
 * @author yuesongwang
 *
 */
@Controller
@RequestMapping(value = "/student/document")
public class StudentDocumentController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StudentDocumentController.class);

    @Autowired
    public StudentDocumentServiceImpl studentDocumentServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/getAllDocuments", method = { RequestMethod.GET, RequestMethod.POST })
    public GenericDataGrid<DocumentListModel> getAllDocuments(HttpServletRequest request, HttpServletResponse response) {
        int page = 1;
        int pageSize = 10;
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") == null)
            return null;
        GenericDataGrid<DocumentListModel> documents = studentDocumentServiceImpl.findDocumentsForPagingByCreatorId(
                page, pageSize,
                null,
                null,
                ((User) session.getAttribute("USER")).getId());
        return documents;
    }

    @RequestMapping(value = "/uploadAttachements", method = { RequestMethod.POST })
    public void uploadAttachements(HttpServletRequest request, HttpServletResponse response) throws SSEException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");

        // Upload temporary doucments to ftp server
        AttachmentInfo info = studentDocumentServiceImpl.uploadTempAttachment(currentUser, fileMap);
        // Create temp entry in DB Attachment table
        studentDocumentServiceImpl.createTempAttachmentEntryInDB(info);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllTempAttachments", method = { RequestMethod.GET, RequestMethod.POST })
    public List<SimpleAttachmentInfo> getAllTempAttachmentsByUserId(HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") == null)
            return new LinkedList<SimpleAttachmentInfo>();
        else
            return studentDocumentServiceImpl.getAllTempAttachmentOfAUser((User) session.getAttribute("USER"));
    }

    // Ajax
    @ResponseBody
    @RequestMapping(value = "/deleteOneTempAttachmentByAttachmentId", method = { RequestMethod.GET })
    public boolean deleteOneTempAttachmentByAttachmentId(HttpServletRequest request,
            HttpServletResponse response) {
        User u = (User) (request.getSession().getAttribute("USER"));
        String attachmentId = request.getParameter("attachmentId");
        if (u != null && attachmentId != null)
            try {
                studentDocumentServiceImpl.deleteAttachmentOnFTPServerAndDBByAttachmentId(u,
                        Integer.parseInt(attachmentId));
            } catch (Exception e) {
                logger.error("删除附件出错", e);
                e.printStackTrace();
                throw new SSEException("删除附件出错", e);
            }
        return true;
    }

    @ResponseBody
    @RequestMapping(value = "/confirmCreateDocument", method = { RequestMethod.POST })
    public boolean confirmCreateDocument(@ModelAttribute DocumentFormModel documentModel, HttpServletRequest request) {
        User u = (User) (request.getSession().getAttribute("USER"));
        if (u != null)
            studentDocumentServiceImpl.confirmCreateDocumentAndAddDocumentToDB(u, documentModel);
        return true;
    }

    @ResponseBody
    @RequestMapping(value = "/cancelCreateDocument", method = { RequestMethod.POST })
    public boolean cancelCreateDocument(HttpServletRequest request, HttpServletResponse response) throws SSEException {
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") == null)
        {
            User u = (User) (request.getSession().getAttribute("USER"));
            try {
                studentDocumentServiceImpl.cancelCreateDocumentAndRemoveTempAttachmentsOnFTPServer(u);
            } catch (IOException e) {
                logger.error("删除附件出错", e);
                throw new SSEException("删除附件出错", e);
            }
        }
        return true;
    }

    @ResponseBody
    @RequestMapping(value = "/checkIfHasSuchDocument", method = { RequestMethod.POST })
    public DocumentInfo checkIfStudentHasSuchDocument(HttpServletRequest request)
    {
        String type = request.getParameter("type");
        User student = ((User) (request.getSession().getAttribute("USER")));
        return studentDocumentServiceImpl.getDocumentInfoByStudentIdAndDocumentType(student.getId(), type);
    }

    @ResponseBody
    @RequestMapping(value = "/updateDocumentDescription", method = { RequestMethod.POST })
    public BasicJson updateDocumentDescription(String document_type, String document_description,
            HttpServletRequest request)
    {
        User student = ((User) (request.getSession().getAttribute("USER")));
        studentDocumentServiceImpl.updateDocumentDescription(student.getId(), document_type,
                document_description);
        return new BasicJson(true, "更新成功", null);
    }

    @ResponseBody
    @RequestMapping(value = "/getDocumentComments", method = { RequestMethod.POST })
    public List<DocumentCommentListModel> getDocumentComments(String type, HttpServletRequest request)
    {
        User student = ((User) (request.getSession().getAttribute("USER")));
        return studentDocumentServiceImpl.findDocumentComments(student.getId(), type);

    }
}
