package sse.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.net.ftp.FTP;
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
import sse.utils.FtpUtil;

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
        FtpUtil ftpUtil = new FtpUtil("127.0.0.1", 21, "sseftp", "123", "/Users/sseftp/Desktop/",
                FTP.BINARY_FILE_TYPE);
        ftpUtil.connectServer();
        ftpUtil.enterLocalPassiveMode();
        boolean isAllFileUploadedSuccesssful = false;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet())
        {
            System.out.println("Key:" + entity.getKey());
            System.out.println("Value:" + entity.getValue());
            MultipartFile mf = entity.getValue();
            InputStream oneFileInputStream;

            try {
                oneFileInputStream = new ByteArrayInputStream(mf.getBytes());
                // Try
//                OutputStream fileOutputStream = new FileOutputStream(new File("/Users/yuesongwang/Desktop/abcabc"));
//                int read = 0;
//                byte[] bytes = new byte[1024];
//                while ((read = oneFileInputStream.read(bytes)) != -1)
//                {
//                    fileOutputStream.write(bytes, 0, read);
//                }
//                System.out.println("Done");
//                fileOutputStream.close();
                 ftpUtil.uploadFileToFtpServerByStream("", "Wangyuesong+" + RandomUtils.nextInt(),
                 oneFileInputStream);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        isAllFileUploadedSuccesssful = true;
        System.out.println("here");
    }
}
