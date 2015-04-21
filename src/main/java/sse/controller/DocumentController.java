package sse.controller;

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

import sse.entity.Document;
import sse.entity.User;
import sse.enums.DocumentTypeEnum;
import sse.exception.SSEException;
import sse.jsonmodel.DocumentFormModel;
import sse.jsonmodel.DocumentListModel;
import sse.pageModel.DataGrid;
import sse.service.impl.DocumentServiceImpl;
import sse.service.impl.DocumentServiceImpl.AttachmentInfo;
import sse.service.impl.DocumentServiceImpl.SimpleAttachmentInfo;

/**
 * @author yuesongwang
 *
 */
@Controller
@RequestMapping(value = "/document")
public class DocumentController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    public DocumentServiceImpl documentServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/getAllDocuments", method = { RequestMethod.GET, RequestMethod.POST })
    public DataGrid<DocumentListModel> getAllDocuments(HttpServletRequest request, HttpServletResponse response) {
        int page = 1;
        int pageSize = 10;
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") == null)
            return null;
        DataGrid<DocumentListModel> documents = documentServiceImpl.findDocumentsForPagingByCreatorId(page, pageSize,
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
        AttachmentInfo info = documentServiceImpl.uploadTempAttachment(currentUser, fileMap);
        // Create temp entry in DB Attachment table
        documentServiceImpl.createTempAttachmentEntryInDB(info);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllTempAttachmentsByUserId", method = { RequestMethod.GET, RequestMethod.POST })
    public List<SimpleAttachmentInfo> getAllTempAttachmentsByUserId(HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") == null)
            return new LinkedList<SimpleAttachmentInfo>();
        else
            return documentServiceImpl.getAllTempAttachmentOfAUser((User) session.getAttribute("USER"));
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
                documentServiceImpl.deleteAttachmentOnFTPServerAndDB(u, Integer.parseInt(attachmentId));
            } catch (Exception e) {
                logger.error("删除附件出错", e);
                e.printStackTrace();
                throw new SSEException("删除附件出错", e);
            }
        return true;
    }

    @ResponseBody
    @RequestMapping(value = "/getAllDocumentTypes", method = { RequestMethod.GET })
    public List<String> getAllDocumentTypes() {
        return DocumentTypeEnum.getAllTypeValues();
    }

    @ResponseBody
    @RequestMapping(value = "/confirmCreateDocument", method = { RequestMethod.POST })
    public boolean confirmCreateDocument(@ModelAttribute DocumentFormModel documentModel, HttpServletRequest request) {
        User u = (User) (request.getSession().getAttribute("USER"));
        if (u != null)
            documentServiceImpl.confirmCreateDocumentAndAddDocumentToDB(u, documentModel);
        return true;
    }

    @RequestMapping(value = "/cancelCreateDocument", method = { RequestMethod.POST })
    public void cancelCreateDocument(HttpServletRequest request, HttpServletResponse response) {
    }
}
