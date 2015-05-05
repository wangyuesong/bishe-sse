package sse.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import sse.commandmodel.DocumentFormModel;
import sse.commandmodel.WillModel;
import sse.dao.impl.AttachmentDaoImpl;
import sse.dao.impl.DocumentDaoImpl;
import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.Attachment;
import sse.entity.Document;
import sse.entity.Student;
import sse.entity.Teacher;
import sse.entity.User;
import sse.entity.Will;
import sse.enums.AttachmentStatusEnum;
import sse.enums.DocumentTypeEnum;
import sse.exception.SSEException;
import sse.pageModel.DocumentListModel;
import sse.pageModel.GenericDataGrid;

/**
 * @author yuesongwang
 *
 */
@Service
public class StudentDocumentServiceImpl {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StudentDocumentServiceImpl.class);

    @Autowired
    private DocumentDaoImpl documentDaoImpl;

    @Autowired
    private AttachmentDaoImpl attachmentDaoImpl;

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    public StudentDocumentServiceImpl() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private WillDaoImpl willDaoImpl;

    // FTP Server configuration
    String serverIp = "127.0.0.1";
    int serverPort = 21;
    int fileType = FTP.BINARY_FILE_TYPE;
    String ftpUserName = "sseftp";
    String ftpPassword = "123";

    public List<SimpleAttachmentInfo> getAllTempAttachmentOfAUser(User currentUser)
    {
        List<SimpleAttachmentInfo> infoList = new LinkedList<SimpleAttachmentInfo>();
        List<Attachment> attachments = attachmentDaoImpl.findTempAttachmentsByUserId(currentUser.getId());
        if (org.springframework.util.CollectionUtils.isEmpty(attachments))
            return infoList;
        else
        {
            for (Attachment a : attachments)
            {
                SimpleAttachmentInfo sai = new SimpleAttachmentInfo();
                DateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                sai.setId(a.getId());
                sai.setListName(a.getListName());
                sai.setUploadTime(dateFormat.format(a.getCreateTime()));
                infoList.add(sai);
            }
        }
        return infoList;
    }

    public AttachmentInfo uploadTempAttachment(User currentUser, Map<String, MultipartFile> fileMap)
            throws SSEException
    {
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
                FTPClient ftpClient = connectAndLoginFtpServer();
                ftpClient.makeDirectory(new String(baseDir.getBytes("UTF-8"), "iso-8859-1"));
                ftpClient.changeWorkingDirectory(baseDir);
                ftpClient.storeFile(convertUTF8ToISO(fileLocationOnServer),
                        oneFileInputStream);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                throw new SSEException("上传失败", e);
            } finally
            {
                try {
                    oneFileInputStream.close();
                } catch (IOException e) {
                    throw new SSEException("上传失败", e);
                }

            }
        }
        AttachmentInfo info = new AttachmentInfo(currentUser, realFileName, listFileName, fileSize
                + "",
                AttachmentStatusEnum.TEMP, url);
        return info;
    }

    /**
     * @Method: connectAndLoginFtpServer
     * @Description: TODO
     * @param @return
     * @param @throws SocketException
     * @param @throws IOException
     * @return FTPClient
     * @throws
     */
    private FTPClient connectAndLoginFtpServer() throws SocketException, IOException {
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
        return ftpClient;
    }

    public void createTempAttachmentEntryInDB(AttachmentInfo info)
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

    public boolean deleteAttachmentOnFTPServerAndDBByAttachmentId(User user, int attachmentId) throws SocketException,
            IOException
    {
        Attachment attachment = attachmentDaoImpl.findById(attachmentId);
        String realName = attachment.getRealName();

        // Delete file on ftp server
        FTPClient ftpClient = connectAndLoginFtpServer();
        String baseDir = "/Users/sseftp/" + user.getAccount() + user.getName() + "/";
        boolean result = ftpClient.deleteFile(convertUTF8ToISO(baseDir + realName));
        if (!result)
            return result;
        // Delete Attachment entry in db
        attachmentDaoImpl.removeWithTransaction(attachment);
        return true;
    }

    /**
     * @Method: confirmCreateDocumentAndAddDocumentToDB
     * @Description: 创建新的Document，并且将所有状态为Temp的属于该用户的附件都变为Forever，与新创建的Document建立关系
     * @param @param u
     * @param @param documentModel
     * @return void
     * @throws
     */
    public void confirmCreateDocumentAndAddDocumentToDB(User u, DocumentFormModel documentModel) {
        if (u != null)
        {
            // Create document
            Document document = new Document();
            document.setCreator(u);
            document.setDocumenttype(DocumentTypeEnum.getType(documentModel.getDocument_type()));
            document.setName(documentModel.getDocument_name());
            document.setContent(documentModel.getDocument_description());
            document.setLastModifiedBy(u);
            // Firstly find all the temp attachment belongs to this user
            documentDaoImpl.persistWithTransaction(document);
            List<Attachment> tempAttachmentList = attachmentDaoImpl.findTempAttachmentsByUserId(u.getId());
            if (tempAttachmentList != null)
                for (Attachment attachment : tempAttachmentList)
                {
                    // Change the status of attachment to FOREVER
                    attachment.setStatus(AttachmentStatusEnum.FOREVER);
                    attachment.setDocument(document);
                    if (org.springframework.util.CollectionUtils.isEmpty(document.getDocumentAttachments()))
                        document.setDocumentAttachments(new LinkedList<Attachment>());
                    attachmentDaoImpl.mergeWithTransaction(attachment);
                }
        }
    }

    /**
     * @Method: findDocumentsForPagingByCreatorId
     * @Description: 列出由自己创建的所有的文档
     * @param @param page
     * @param @param pageSize
     * @param @param sort
     * @param @param order
     * @param @param creatorId
     * @param @return
     * @return DataGrid<DocumentListModel>
     * @throws
     */
    public GenericDataGrid<DocumentListModel> findDocumentsForPagingByCreatorId(int page, int pageSize, String sort,
            String order,
            Integer creatorId)
    {
        GenericDataGrid<DocumentListModel> dg = new GenericDataGrid<>();
        List<Document> documents = documentDaoImpl.findDocumentsForPagingByCreatorId(page, pageSize, sort, order,
                creatorId);
        List<DocumentListModel> documentModels = new LinkedList<DocumentListModel>();
        for (Document d : documents)
        {
            DocumentListModel dm = new DocumentListModel();
            dm.setCreator(d.getCreator().getName());
            dm.setDocumentCommentsCount(d.getDocumentComments().size());
            dm.setDocumentType(d.getDocumenttype().getValue());
            dm.setId(d.getId());
            dm.setLastModifiedBy(d.getLastModifiedBy().getName());
            dm.setName(d.getName());
            documentModels.add(dm);
        }
        dg.setRows(documentModels);
        dg.setTotal(documentDaoImpl.findDocumentsForCount());
        return dg;
    }

    /**
     * @throws IOException
     * @Method: cancelCreateDocumentAndRemoveTempAttachmentsOnFTPServer
     * @Description: 用户点击红叉，取消创建Document，因此要删除所有状态为Temp的附件
     * @param @param u
     * @return void
     * @throws
     */
    public void cancelCreateDocumentAndRemoveTempAttachmentsOnFTPServer(User user) throws IOException {
        List<Attachment> attachments = attachmentDaoImpl.findTempAttachmentsByUserId(user.getId());
        // Delete file on ftp server
        FTPClient ftpClient = connectAndLoginFtpServer();
        String baseDir = "/Users/sseftp/" + user.getAccount() + user.getName() + "/";
        for (Attachment a : attachments)
        {
            ftpClient.deleteFile(convertUTF8ToISO(baseDir + a.getRealName()));
            attachmentDaoImpl.removeWithTransaction(a);
        }
    }

    public HashMap<String, String> findPreviousWills(int studentId)
    {
        List<Will> wills = willDaoImpl.findPreviousSelectionsByStudentId(studentId);
        if (!CollectionUtils.isEmpty(wills))
        {
            HashMap<String, String> returnMap = new HashMap<String, String>();
            for (Will w : wills)
            {
                Teacher t = teacherDaoImpl.findById(w.getId().getTeacherId());
                returnMap.put("" + w.getLevel(), t.getAccount());
            }
            return returnMap;
        }
        else
            return null;
    }

    public void updateSelection(WillModel model)
    {
        willDaoImpl.updateSelection(model);
    }

    private String convertUTF8ToISO(String utf8String)
    {
        try {
            return new String(utf8String.getBytes("UTF-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            logger.error("不支持的字符集", e);
        }
        return null;
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

    public static class SimpleAttachmentInfo
    {
        private int id;
        private String listName;
        private String uploadTime;

        public String getUploadTime() {
            return uploadTime;
        }

        public void setUploadTime(String uploadTime) {
            this.uploadTime = uploadTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getListName() {
            return listName;
        }

        public void setListName(String listName) {
            this.listName = listName;
        }

    }

    /**
     * @Method: checkIfStudentHasSuchDocument
     * @Description: 检查该学生是否已经创建了这样的文档
     * @param @param id
     * @param @param type
     * @return void
     * @throws
     */
    public boolean checkIfStudentHasSuchDocument(int id, String type) {
        Student s = studentDaoImpl.findById(id);
        List<Document> documents = s.getDocuments();
        for (Document d : documents)
        {
            if (DocumentTypeEnum.getType(type) != DocumentTypeEnum.UNKNOWN)
                return true;
        }
        return false;
    }
}
