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
import sse.entity.Document;
import sse.entity.User;
import sse.enums.AttachmentStatusEnum;
import sse.enums.DocumentTypeEnum;
import sse.exception.SSEException;
import sse.pagemodel.DocumentCommentListModel;
import sse.pagemodel.DocumentListModel;
import sse.pagemodel.GenericDataGrid;
import sse.service.impl.ActionEventServiceImpl;
import sse.service.impl.DocumentSerivceImpl;
import sse.service.impl.DocumentSerivceImpl.AttachmentInfo;
import sse.service.impl.DocumentSerivceImpl.DocumentInfo;
import sse.service.impl.DocumentSerivceImpl.SimpleAttachmentInfo;
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
@RequestMapping(value = "/document/")
public class DocumentController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    public DocumentSerivceImpl documentServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private ActionEventServiceImpl actionEventServiceImpl;

    // 所有Read 方法
    /**
     * Description: 获得创建者id为creatorId的所有Document
     * 
     * @param creatorId
     * @param request
     * @param response
     * @return
     *         GenericDataGrid<DocumentListModel>
     */
    @ResponseBody
    @RequestMapping(value = "/getAllDocumentsByCreatorId")
    public GenericDataGrid<DocumentListModel> getAllDocumentsByCreatorId(int creatorId, HttpServletRequest request,
            HttpServletResponse response) {
        int page = 1;
        int pageSize = 10;
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") == null)
            return null;
        GenericDataGrid<DocumentListModel> documents = documentServiceImpl.findDocumentsForPagingByCreatorId(
                page, pageSize,
                null,
                null,
                creatorId);
        return documents;
    }

    /**
     * Description: 获得创建者id为studentId，type为type的Document的所有documentComment
     * 
     * @param studentId
     * @param type
     * @param request
     * @return
     *         GenericDataGrid<DocumentCommentListModel>
     */
    @ResponseBody
    @RequestMapping(value = "/getDocumentCommentsByStudentIdAndDocumentType", method = { RequestMethod.POST })
    public GenericDataGrid<DocumentCommentListModel> getDocumentCommentsByStudentIdAndDocumentType(int studentId,
            String type,
            HttpServletRequest request)
    {
        return documentServiceImpl
                .findDocumentCommentsForPagingByStudentIdAndDocumentType(studentId, type);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllTempAttachmentsByUserIdAndDocumentType")
    public List<SimpleAttachmentInfo> getAllTempAttachmentsByUserIdAndDocumentType(int userId, String type,
            HttpServletRequest request,
            HttpServletResponse response) {
        return documentServiceImpl.getAttachmentsOfStudentByUserIdDocumentTypeAndAttachmentStatus(userId,
                DocumentTypeEnum.getType(type), AttachmentStatusEnum.TEMP);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllForeverAttachmentsByUserIdAndDocumentType")
    public List<SimpleAttachmentInfo> getAllForeverAttachmentsByUserIdAndDocumentType(int userId, String type,
            HttpServletRequest request,
            HttpServletResponse response) {
        return documentServiceImpl.getAttachmentsOfStudentByUserIdDocumentTypeAndAttachmentStatus(userId,
                DocumentTypeEnum.getType(type), AttachmentStatusEnum.FOREVER);
    }

    // 所有CUR 方法

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
    public BasicJson makeComment(DocumentCommentFormModel model,
            HttpServletRequest request)
    {
        int documentId = documentServiceImpl.findDocumentIdByStudentIdAndDocumentType(
                Integer.parseInt(model.getStudentId()), model.getType());
        documentServiceImpl.makeDocumentComment(documentId, model.getContent(),
                Integer.parseInt(model.getCommentorId()));

        actionEventServiceImpl.createActionEvent(Integer.parseInt(model.getCommentorId()), "在" + model.getType()
                + "下留言");

        return new BasicJson(true, "留言成功", null);
    }

    /**
     * Description: 创建附件，该方法为文档已经创建时对文档附件的创建动作, 由于可能是教师上传的附件，因此需要一个creatorId一个ownerId，creatorId为上传附件者的id
     * 
     * @param creatorId
     * @param ownerId
     * @param documentType
     * @param request
     * @param response
     * @throws SSEException
     *             void
     */
    @RequestMapping(value = "/uploadForeverAttachments", method = { RequestMethod.POST })
    public void uploadForeverAttachments(int creatorId, int ownerId, String documentType,
            HttpServletRequest request,
            HttpServletResponse response)
            throws SSEException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // Attachment上传到Ftp
        AttachmentInfo info = documentServiceImpl.uploadAttachmentToFtp(creatorId, fileMap);
        // 创建Attachment纪录并将Attachemnt和Document关联
        documentServiceImpl.createForeverAttachmentEntryInDB(info, ownerId, documentType);
        DocumentInfo d = documentServiceImpl.getDocumentInfoByStudentIdAndDocumentType(ownerId, documentType);
        actionEventServiceImpl.createActionEvent(creatorId, "上传了附件" + info.getListName() + "到" + d.getName());
    }

    /**
     * Description: 创建附件，该方法为文档尚未创建时对文档附件的创建动作，需要confirmCreateDocument
     * 
     * @param request
     * @param response
     * @throws SSEException
     *             void
     */
    @RequestMapping(value = "/uploadTempAttachments", method = { RequestMethod.POST })
    public void uploadTempAttachments(int creatorId, HttpServletRequest request, HttpServletResponse response)
            throws SSEException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // Upload temporary doucments to ftp server
        AttachmentInfo info = documentServiceImpl.uploadAttachmentToFtp(creatorId, fileMap);
        // Create temp entry in DB Attachment table
        documentServiceImpl.createTempAttachmentEntryInDB(info);
    }

    /**
     * Description: 确认创建附件
     * 
     * @param documentModel
     * @param request
     * @return
     *         boolean
     */
    @ResponseBody
    @RequestMapping(value = "/confirmCreateDocument", method = { RequestMethod.POST })
    public void confirmCreateDocument(int creatorId, DocumentFormModel documentModel, HttpServletRequest request) {
        documentServiceImpl.confirmCreateDocumentAndAddDocumentToDB(creatorId, documentModel);
        actionEventServiceImpl.createActionEvent(creatorId, "创建了文档:" + documentModel.getDocument_name());
    }

    @ResponseBody
    @RequestMapping(value = "/cancelCreateDocument")
    public BasicJson cancelCreateDocument(int creatorId, String documentType, HttpServletRequest request,
            HttpServletResponse response)
            throws SSEException {
        try {
            documentServiceImpl.cancelCreateDocumentAndRemoveTempAttachmentsOnFTPServer(creatorId,
                    DocumentTypeEnum.getType("documentType"));
        } catch (IOException e) {
            logger.error("删除附件出错", e);
            throw new SSEException("删除附件出错", e);
        }
        return new BasicJson(true, "删除成功", null);
    }

    /**
     * Description: 通用下载附件方法
     * 
     * @param request
     * @param attachmentId
     * @param response
     * @throws SSEException
     *             void
     */
    @RequestMapping(value = "/downloadAttachmentByAttachmentId")
    public void downloadAttachmentByAttachmentId(int attachmentId, HttpServletRequest request,
            HttpServletResponse response)
            throws SSEException {
        response.setContentType("application/octet-stream");

        try {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename="
                            + new String(documentServiceImpl.findAttachmentListNameByAttachmentId(attachmentId)
                                    .getBytes("GB2312"), "iso8859-1"));
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            documentServiceImpl.downloadAttachment(attachmentId, response.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new SSEException("下载失败，请联系管理员", e);
        }

    }

    /**
     * Description: 通用删除附件方法，同时删除数据库记录
     * 
     * @param request
     * @param response
     * @return
     *         boolean
     */
    @ResponseBody
    @RequestMapping(value = "/deleteOneAttachmentByAttachmentId")
    public BasicJson deleteOneAttachmentByAttachmentId(int attachmentId, HttpServletRequest request,
            HttpServletResponse response) {
        actionEventServiceImpl.createActionEvent(((User) (request.getSession().getAttribute("USER"))).getId(),
                "删除了附件" + documentServiceImpl.findAttachmentListNameByAttachmentId(attachmentId));
        try {
            documentServiceImpl.deleteAttachmentOnFTPServerAndDBByAttachmentId(
                    attachmentId);
        } catch (Exception e) {
            logger.error("删除附件出错", e);
            e.printStackTrace();
            throw new SSEException("删除附件出错", e);
        }

        return new BasicJson(true, "删除附件成功", null);
    }

    /**
     * Description: 查找id为userId的学生是否有type类型的Document，如果没有则返回null，有则返回相关信息
     * 
     * @param userId
     * @param type
     * @param request
     * @return
     *         DocumentInfo
     */
    @ResponseBody
    @RequestMapping(value = "/checkIfHasSuchDocumentByUserIdAndType", method = { RequestMethod.POST })
    public DocumentInfo checkIfStudentHasSuchDocument(int userId, String type, HttpServletRequest request)
    {
        return documentServiceImpl.getDocumentInfoByStudentIdAndDocumentType(userId, type);
    }

    @ResponseBody
    @RequestMapping(value = "/updateDocumentDescription", method = { RequestMethod.POST })
    public BasicJson updateDocumentDescription(int userId, String documentType, String documentDescription,
            HttpServletRequest request)
    {
        documentServiceImpl.updateDocumentDescription(userId, documentType,
                documentDescription);

        actionEventServiceImpl.createActionEvent(((User) (request.getSession().getAttribute("USER"))).getId(),
                "更新了" + documentServiceImpl.getDocumentInfoByStudentIdAndDocumentType(userId, documentType).getName());
        return new BasicJson(true, "更新成功", null);
    }

}
