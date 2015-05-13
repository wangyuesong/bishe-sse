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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sse.commandmodel.BasicJson;
import sse.commandmodel.DocumentFormModel;
import sse.entity.User;
import sse.enums.AttachmentStatusEnum;
import sse.enums.DocumentTypeEnum;
import sse.exception.SSEException;
import sse.pageModel.DocumentCommentListModel;
import sse.pageModel.DocumentListModel;
import sse.pageModel.GenericDataGrid;
import sse.pageModel.TopicModel;
import sse.service.impl.StudentDocumentServiceImpl;
import sse.service.impl.StudentDocumentServiceImpl.AttachmentInfo;
import sse.service.impl.StudentDocumentServiceImpl.DocumentInfo;
import sse.service.impl.StudentDocumentServiceImpl.SimpleAttachmentInfo;
import sse.service.impl.UserServiceImpl;
import sse.utils.SessionUtil;

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

    @Autowired
    private UserServiceImpl userServiceImpl;

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

    @ResponseBody
    @RequestMapping(value = "/getDocumentComments", method = { RequestMethod.POST })
    public GenericDataGrid<DocumentCommentListModel> getDocumentComments(int studentId, String type,
            HttpServletRequest request)
    {
        return studentDocumentServiceImpl
                .findDocumentCommentsForPagingByStudentIdAndDocumentType(studentId, type);
    }

    /**
     * Description: 增加Document评论
     * 
     * @param studentId
     * @param commentorId
     * @param type
     * @param content
     * @param request
     * @return
     *         BasicJson
     */
    @ResponseBody
    @RequestMapping(value = "/makeComment", method = { RequestMethod.POST })
    public BasicJson makeComment(String studentId, String commentorId, String type, String content,
            HttpServletRequest request)
    {
        int documentId = studentDocumentServiceImpl.findDocumentIdByStudentIdAndDocumentType(
                Integer.parseInt(studentId), type);
        studentDocumentServiceImpl.makeDocumentComment(documentId, content, Integer.parseInt(commentorId));
        return new BasicJson(true, "留言成功", null);
    }

    /**
     * Description: 创建附件，该方法为文档已经创建时对文档附件的创建动作
     * 
     * @param creatorId
     * @param ownerId
     * @param documentType
     * @param request
     * @param response
     * @throws SSEException
     *             void
     */
    @RequestMapping(value = "/uploadAttachements", method = { RequestMethod.POST })
    public void uploadAttachements(int creatorId, int ownerId, String documentType,
            HttpServletRequest request,
            HttpServletResponse response)
            throws SSEException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // Attachment上传到Ftp
        AttachmentInfo info = studentDocumentServiceImpl.uploadAttachmentToFtp(creatorId, fileMap);
        // 创建Attachment纪录并将Attachemnt和Document关联
        studentDocumentServiceImpl.createForeverAttachmentEntryInDB(info, ownerId, documentType);
    }

    /**
     * Description: 创建附件，该方法为文档尚未创建时对文档附件的创建动作，需要confirmCreateDocument
     * 
     * @param request
     * @param response
     * @throws SSEException
     *             void
     */
    @RequestMapping(value = "/uploadTempAttachements", method = { RequestMethod.POST })
    public void uploadTempAttachements(int userId, HttpServletRequest request, HttpServletResponse response)
            throws SSEException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // Upload temporary doucments to ftp server
        AttachmentInfo info = studentDocumentServiceImpl.uploadAttachmentToFtp(userId, fileMap);
        // Create temp entry in DB Attachment table
        studentDocumentServiceImpl.createTempAttachmentEntryInDB(info);
    }

    @RequestMapping(value = "/downloadAttachment")
    public void downloadAttachment(HttpServletRequest request, int attachmentId, HttpServletResponse response)
            throws SSEException {
        response.setContentType("application/octet-stream");

        try {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename="
                            + new String(studentDocumentServiceImpl.findAttachmentListNameByAttachmentId(attachmentId)
                                    .getBytes("GB2312"), "iso8859-1"));
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            studentDocumentServiceImpl.downloadAttachment(attachmentId, response.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new SSEException("下载失败，请联系管理员", e);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/getAllTempAttachments")
    public List<SimpleAttachmentInfo> getAllTempAttachments(String type, HttpServletRequest request,
            HttpServletResponse response) {
        return studentDocumentServiceImpl.getAttachmentsOfAUserByTypeAndAttachmentStatus(SessionUtil
                .getUserFromSession(request).getId(),
                DocumentTypeEnum.getType(type), AttachmentStatusEnum.TEMP);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllForeverAttachments")
    public List<SimpleAttachmentInfo> getAllForeverAttachments(String type, HttpServletRequest request,
            HttpServletResponse response) {
        return studentDocumentServiceImpl.getAttachmentsOfAUserByTypeAndAttachmentStatus(SessionUtil
                .getUserFromSession(request).getId(),
                // Get方法传中文乱码，暂时用拼音
                DocumentTypeEnum.getTypeByPinYin(type), AttachmentStatusEnum.FOREVER);
    }

    /**
     * Description: 删除FTP上的某附件，同时删除数据库记录
     * 
     * @param request
     * @param response
     * @return
     *         boolean
     */
    @ResponseBody
    @RequestMapping(value = "/deleteOneAttachmentByAttachmentId", method = { RequestMethod.GET })
    public boolean deleteOneAttachmentByAttachmentId(HttpServletRequest request,
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
    public boolean cancelCreateDocument(String documentType, HttpServletRequest request, HttpServletResponse response)
            throws SSEException {
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") == null)
        {
            User u = (User) (request.getSession().getAttribute("USER"));
            try {
                studentDocumentServiceImpl.cancelCreateDocumentAndRemoveTempAttachmentsOnFTPServer(u,
                        DocumentTypeEnum.getType("documentType"));
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
    @RequestMapping(value = "/getTopic", method = { RequestMethod.POST })
    public TopicModel getTopic(int studentId)
    {
        return studentDocumentServiceImpl.getTopicByStudentId(studentId);
    }

    @ResponseBody
    @RequestMapping(value = "/saveTopic")
    public BasicJson saveTopic(TopicModel tm, int studentId)
    {
        studentDocumentServiceImpl.saveTopic(tm, studentId);
        return new BasicJson(true, "保存成功", null);
    }
}
