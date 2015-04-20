package sse.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sse.dao.impl.AttachmentDaoImpl;
import sse.dao.impl.DocumentDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.Attachment;
import sse.entity.Document;
import sse.entity.User;
import sse.enums.AttachmentStatusEnum;
import sse.exception.SSEException;
import sse.jsonmodel.DocumentModel;
import sse.pageModel.DataGrid;
import sse.pageModel.WillModel;

@Service
public class DocumentServiceImpl {

    @Autowired
    private DocumentDaoImpl documentDaoImpl;

    @Autowired
    private AttachmentDaoImpl attachmentDaoImpl;

    @Autowired
    private WillDaoImpl willDaoImpl;

    public AttachmentInfo uploadTempAttachment(User currentUser, Map<String, MultipartFile> fileMap)
    {
        String serverIp = "127.0.0.1";
        int serverPort = 21;
        int fileType = FTP.BINARY_FILE_TYPE;
        String ftpUserName = "sseftp";
        String ftpPassword = "123";
        String realFileName = "";
        String listFileName = "";
        String fileLocationOnServer = "";
        String url = "";
        String baseDir = "";
        long fileSize = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet())
        {
            MultipartFile mf = entity.getValue();

            fileSize = mf.getSize();
            listFileName = mf.getOriginalFilename();
            realFileName = convertListNameToRealNameByAppendingTimeStamp(listFileName);
            baseDir = "/Users/sseftp/" + currentUser.getAccount() + currentUser.getName() + "/";
            url = "ftp://" + ftpUserName + ":" + ftpPassword + "@" + serverIp + ":" + serverPort + baseDir
                    + realFileName;
            fileLocationOnServer = baseDir + realFileName;
            InputStream oneFileInputStream = null;

            try {
                oneFileInputStream = new ByteArrayInputStream(mf.getBytes());
                FTPClient ftpClient = new FTPClient();
                // First connect
                ftpClient.connect(serverIp, serverPort);
                int reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply))
                {
                    ftpClient.disconnect();
                    throw new SSEException("FTP Server 不接受连接");
                }
                // Then login
                if (!ftpClient.login(ftpUserName, ftpPassword))
                {
                    ftpClient.disconnect();
                    throw new SSEException("用户名密码错误");
                }
                // Set file type and misc
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(fileType);
                ftpClient.enterLocalPassiveMode();
                ftpClient.makeDirectory(new String(baseDir.getBytes("UTF-8"), "iso-8859-1"));
                ftpClient.changeWorkingDirectory(baseDir);
                ftpClient.storeFile(new String(fileLocationOnServer.getBytes("UTF-8"), "iso-8859-1"),
                        oneFileInputStream);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                throw new SSEException("上传失败", e);
            } finally
            {
                try {
                    oneFileInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        AttachmentInfo info = new AttachmentInfo(currentUser, realFileName, listFileName, fileSize
                + "",
                AttachmentStatusEnum.TEMP, url);
        return info;
    }

    public void createTempAttachment(AttachmentInfo info)
    {
        Attachment a = new Attachment();
        a.setCreator(info.getCreator());
        a.setListName(info.getListName());
        a.setRealName(info.getRealName());
        a.setUrl(info.getUrl());
        a.setSize(info.getSize());
        a.setStatus(AttachmentStatusEnum.TEMP);
        attachmentDaoImpl.createTempAttachment(a);
    }

    public DataGrid<DocumentModel> findDocumentsForPagingByCreatorId(int page, int pageSize, String sort, String order,
            Integer creatorId)
    {
        DataGrid<DocumentModel> dg = new DataGrid<>();
        List<Document> documents = documentDaoImpl.findDocumentsForPagingByCreatorId(page, pageSize, sort, order,
                creatorId);
        List<DocumentModel> documentModels = new LinkedList<DocumentModel>();
        for (Document d : documents)
        {
            DocumentModel dm = new DocumentModel();
            dm.setCreator(d.getCreator().getName());
            dm.setDocumentCommentsCount(d.getDocumentComments().size());
            dm.setDocumentType(d.getDocumenttype().getTypeName());
            dm.setId(d.getId());
            dm.setLastModifiedBy(d.getLastModifiedBy().getName());
            dm.setName(d.getName());
            documentModels.add(dm);
        }
        dg.setRows(documentModels);
        dg.setTotal(documentDaoImpl.findDocumentsForCount());
        return dg;
    }

    public HashMap<String, String> findPreviousWills(int studentId)
    {
        return willDaoImpl.findPreviousSelectionByStudentId(studentId);
    }

    public void updateSelection(WillModel model, int studentId)
    {
        willDaoImpl.updateSelection(model, studentId);
    }

    public static class AttachmentInfo
    {

        public AttachmentInfo(User creator, String realName, String listName, String size, AttachmentStatusEnum status,
                String url) {
            super();
            this.creator = creator;
            this.realName = realName;
            this.listName = listName;
            this.size = size;
            this.status = status;
            this.url = url;
        }

        private User creator;
        private String realName;
        private String listName;
        private String size;
        private AttachmentStatusEnum status;
        private String url;

        public User getCreator() {
            return creator;
        }

        public void setCreator(User creator) {
            this.creator = creator;
        }

        public String getSize() {
            return size;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getListName() {
            return listName;
        }

        public void setListName(String listName) {
            this.listName = listName;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public AttachmentStatusEnum getStatus() {
            return status;
        }

        public void setStatus(AttachmentStatusEnum status) {
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    private String convertListNameToRealNameByAppendingTimeStamp(String listName)
    {
        int posOfDot = listName.lastIndexOf('.');
        String prefix = listName.substring(0, posOfDot);
        String postFix = listName.substring(posOfDot, listName.length());
        prefix += "_" + new Date();
        prefix += postFix;
        return prefix;
    }
}
