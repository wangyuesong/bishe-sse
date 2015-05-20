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
     * Description: 查找一个Document相关的信息
     * 
     * @param userId
     * @param type
     * @param request
     * @return
     *         DocumentInfo
     */
    @ResponseBody
    @RequestMapping(value = "/getDocumentInfoByUserIdAndType", method = { RequestMethod.POST })
    public DocumentInfo getDocumentInfoByUserIdAndType(int userId, String type, HttpServletRequest request)
    {
        return documentServiceImpl.getDocumentInfoByStudentIdAndDocumentType(userId, type);
    }

    @ResponseBody
    @RequestMapping(value = "/updateDocumentDescriptionByUserIdAndType", method = { RequestMethod.POST })
    public BasicJson updateDocumentDescription(int userId, String type, String documentDescription,
            HttpServletRequest request)
    {
        documentServiceImpl.updateDocumentDescription(userId, type,
                documentDescription);

        actionEventServiceImpl.createActionEvent(((User) (request.getSession().getAttribute("USER"))).getId(),
                "更新了" + documentServiceImpl.getDocumentInfoByStudentIdAndDocumentType(userId, type).getName() + "的描述");
        return new BasicJson(true, "更新成功", null);
    }

    @ResponseBody
    @RequestMapping(value = "/changeDocumentStatusByUserIdAndType", method = { RequestMethod.POST })
    public BasicJson changeDocumentStatusByUserIdAndType(int userId, String type, String documentStatus,
            HttpServletRequest request)
    {

        BasicJson returnJson = documentServiceImpl.changeDocumentStatusByUserIdAndType(userId, type, documentStatus);
        actionEventServiceImpl.createActionEvent(((User) (request.getSession().getAttribute("USER"))).getId(),
                "更新了" + documentServiceImpl.getDocumentInfoByStudentIdAndDocumentType(userId, type).getName() + "的状态");
        return returnJson;
    }

}
