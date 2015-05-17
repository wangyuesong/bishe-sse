package sse.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import sse.service.impl.ActionEventServiceImpl;
import sse.service.impl.AttachmentServiceImpl;
import sse.service.impl.AttachmentServiceImpl.AttachmentInfo;
import sse.service.impl.AttachmentServiceImpl.SimpleAttachmentInfo;
import sse.service.impl.DocumentSerivceImpl;
import sse.service.impl.DocumentSerivceImpl.DocumentInfo;
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
@RequestMapping(value = "/attachment/")
public class AttachmentController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AttachmentController.class);

    @Autowired
    public AttachmentServiceImpl attachmentServiceImpl;

    @Autowired
    public DocumentSerivceImpl documentServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private ActionEventServiceImpl actionEventServiceImpl;

    /**
     * Description: 获得某个user的类型为type的文档的所有附件信息
     * 
     * @param userId
     * @param type
     * @param request
     * @param response
     * @return
     *         List<SimpleAttachmentInfo>
     */
    @ResponseBody
    @RequestMapping(value = "/getAllForeverAttachmentsByUserIdAndDocumentType")
    public List<SimpleAttachmentInfo> getAllForeverAttachmentsByUserIdAndDocumentType(int userId, String type,
            HttpServletRequest request,
            HttpServletResponse response) {
        return attachmentServiceImpl.getAttachmentsOfStudentByUserIdDocumentTypeAndAttachmentStatus(userId,
                DocumentTypeEnum.getType(type), AttachmentStatusEnum.FOREVER);
    }

    /** 
     * Description: TODO
     * @param request
     * @param response
     * @return
     * List<SimpleAttachmentInfo>
     */
    @ResponseBody
    @RequestMapping(value = "/getAllTempAttachmentsOfAdmin")
    public List<SimpleAttachmentInfo> getAllTempAttachments(HttpServletRequest request,
            HttpServletResponse response) {
        return attachmentServiceImpl.getTempAttachmentsOfAdmin();
    }

    /**
     * Description: 创建附件
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
        AttachmentInfo info = attachmentServiceImpl.uploadAttachmentToFtp(creatorId, fileMap);
        // 创建Attachment纪录并将Attachemnt和Document关联
        attachmentServiceImpl.createForeverAttachmentEntryInDB(info, ownerId, documentType);
        DocumentInfo d = documentServiceImpl.getDocumentInfoByStudentIdAndDocumentType(ownerId, documentType);
        actionEventServiceImpl.createActionEvent(creatorId, "上传了附件" + info.getListName() + "到" + d.getName());
    }

    /**
     * Description: 创建附件，附件状态为Temp，该方法为系统消息尚未创建时对文档附件的创建动作，需要confirmCreateDocument来完成创建
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
        AttachmentInfo info = attachmentServiceImpl.uploadAttachmentToFtp(creatorId, fileMap);
        // Create temp entry in DB Attachment table
        attachmentServiceImpl.createTempAttachmentEntryInDB(info);
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

    /**
     * Description: 取消创建附件
     * 
     * @param creatorId
     * @param documentType
     * @param request
     * @param response
     * @return
     * @throws SSEException
     *             BasicJson
     */
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
}
