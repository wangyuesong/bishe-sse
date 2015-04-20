package sse.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sse.entity.User;
import sse.jsonmodel.DocumentModel;
import sse.pageModel.DataGrid;
import sse.service.impl.DocumentServiceImpl;
import sse.service.impl.DocumentServiceImpl.AttachmentInfo;

/**
 * @author yuesongwang
 *
 */
@Controller
@RequestMapping(value = "/document")
public class DocumentController {

    @Autowired
    public DocumentServiceImpl documentServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/getAllDocuments", method = { RequestMethod.GET, RequestMethod.POST })
    public DataGrid<DocumentModel> getAllDocuments(HttpServletRequest request, HttpServletResponse response) {
        int page = 1;
        int pageSize = 10;
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") == null)
            return null;
        DataGrid<DocumentModel> documents = documentServiceImpl.findDocumentsForPagingByCreatorId(page, pageSize, null,
                null,
                ((User) session.getAttribute("USER")).getId());
        return documents;
    }

    @RequestMapping(value = "/uploadAttachements", method = { RequestMethod.POST })
    public void uploadAttachements(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("USER");

        AttachmentInfo info = documentServiceImpl.uploadTempAttachment(currentUser, fileMap);
        documentServiceImpl.createTempAttachment(info);
        
    }
}
