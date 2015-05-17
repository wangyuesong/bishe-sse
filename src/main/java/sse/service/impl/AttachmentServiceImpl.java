package sse.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import sse.commandmodel.BasicJson;
import sse.commandmodel.DocumentFormModel;
import sse.commandmodel.WillModel;
import sse.dao.impl.AttachmentDaoImpl;
import sse.dao.impl.DocumentDaoImpl;
import sse.dao.impl.StudentDaoImpl;
import sse.dao.impl.TeacherDaoImpl;
import sse.dao.impl.TopicDaoImpl;
import sse.dao.impl.UserDaoImpl;
import sse.dao.impl.WillDaoImpl;
import sse.entity.Attachment;
import sse.entity.Document;
import sse.entity.DocumentComment;
import sse.entity.Student;
import sse.entity.Teacher;
import sse.entity.User;
import sse.entity.Will;
import sse.enums.AttachmentStatusEnum;
import sse.enums.DocumentStatusEnum;
import sse.enums.DocumentTypeEnum;
import sse.exception.SSEException;
import sse.pagemodel.DocumentCommentListModel;
import sse.pagemodel.DocumentListModel;
import sse.pagemodel.GenericDataGrid;
import sse.service.impl.AttachmentServiceImpl.SimpleAttachmentInfo;
import sse.utils.FtpTool;

/**
 * @Project: sse
 * @Title: DocumentSerivceImpl.java
 * @Package sse.service.impl
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月13日 下午1:31:53
 * @version V1.0
 */
@Service
public class AttachmentServiceImpl {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AttachmentServiceImpl.class);

    @Autowired
    private DocumentDaoImpl documentDaoImpl;

    @Autowired
    private AttachmentDaoImpl attachmentDaoImpl;

    @Autowired
    private TeacherDaoImpl teacherDaoImpl;

    @Autowired
    private UserDaoImpl userDaoImpl;

    @Autowired
    private StudentDaoImpl studentDaoImpl;

    @Autowired
    private TopicDaoImpl topicDaoImpl;

    public AttachmentServiceImpl() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private WillDaoImpl willDaoImpl;

    /**
     * Description: 根据userId 和 Document type和附件的状态(Temp, Forever)找到一个学生的所有附件，包括教师为其上传的
     * 
     * @param userId
     * @param type
     * @param status
     * @return
     *         List<SimpleAttachmentInfo>
     */
    public List<SimpleAttachmentInfo> getAttachmentsOfStudentByUserIdDocumentTypeAndAttachmentStatus(int userId,
            DocumentTypeEnum type,
            AttachmentStatusEnum status)
    {
        List<SimpleAttachmentInfo> infoList = new LinkedList<SimpleAttachmentInfo>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("status", status);
        params.put("documentCreatorId", userId);
        params.put("documentType", type);
        List<Attachment> attachments = attachmentDaoImpl
                .findForPaging(
                        "select a from Attachment a where a.status = :status and a.document.creator.id = :documentCreatorId and a.document.documentType=:documentType",
                        params);
        for (Attachment a : attachments)
        {
            SimpleAttachmentInfo sai = new SimpleAttachmentInfo();
            DateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
            sai.setId(a.getId());
            sai.setListName(a.getListName());
            sai.setUploadTime(dateFormat.format(a.getCreateTime()));
            sai.setCreatorName(a.getCreator().getName());
            infoList.add(sai);
        }
        return infoList;
    }

    /**
     * Description: 下载附件，将附件的流写入参数out中
     * 
     * @param attachmentId
     * @param out
     *            void
     */
    public void downloadAttachment(int attachmentId, OutputStream out)
    {
        FtpTool ftpTool = new FtpTool();
        try {
            String url = attachmentDaoImpl.findById(attachmentId).getUrl();
            InputStream in = ftpTool.retrieveFileStream(url);
            int replyCode = ftpTool.getReplyCode();
            byte[] outputByte = new byte[4096];
            while (in.read(outputByte, 0, 4096) != -1)
            {
                out.write(outputByte, 0, 4096);
            }
            boolean success = ftpTool.completePendingCommand();
            in.close();
            out.flush();
            out.close();
            if (!success)
                throw new SSEException("下载失败");
        } catch (IOException e) {
            throw new SSEException("下载失败", e);
        }

    }

    public String findAttachmentListNameByAttachmentId(int attchmentId)
    {
        return attachmentDaoImpl.findById(attchmentId).getListName();
    }

    /**
     * Description: 上传一个附件到FtpServer,并且返回该附件的相关信息
     * 
     * @param userId
     * @param fileMap
     * @return
     * @throws SSEException
     *             AttachmentInfo
     */
    public AttachmentInfo uploadAttachmentToFtp(int userId, Map<String, MultipartFile> fileMap)
            throws SSEException
    {
        User creator = userDaoImpl.findById(userId);
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
            baseDir = "/Users/sseftp/" + creator.getAccount() + creator.getName() + "/";
            // url = "ftp://" + ftpUserName + ":" + ftpPassword + "@" + serverIp + ":" + serverPort + baseDir
            // + realFileName;
            url = baseDir + realFileName;
            fileLocationOnServer = baseDir + realFileName;
            InputStream oneFileInputStream = null;
            try {
                oneFileInputStream = new ByteArrayInputStream(mf.getBytes());
                FtpTool ftpTool = new FtpTool();
                ftpTool.makeDirectory(baseDir);
                ftpTool.changeWorkingDirectory(baseDir);
                ftpTool.storeFile(fileLocationOnServer,
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
        AttachmentInfo info = new AttachmentInfo(creator, realFileName, listFileName, fileSize
                + "", url);
        return info;
    }

    /**
     * Description: 在数据库中创建一个永久的Attachment,需要和文档建立关系，根据ownerId和documentType来查到已经存在的文档
     * 
     * @param info
     * @param documentId
     *            void
     */
    public void createForeverAttachmentEntryInDB(AttachmentInfo info, int ownerId, String documentType)
    {
        Attachment a = new Attachment();
        a.setCreator(info.getCreator());
        a.setListName(info.getListName());
        a.setRealName(info.getRealName());
        a.setUrl(info.getUrl());
        a.setSize(info.getSize());
        a.setStatus(AttachmentStatusEnum.FOREVER);
        Document document = documentDaoImpl.findDocumentByStudentIdAndType(ownerId,
                DocumentTypeEnum.getType(documentType));
        document.addAttachment(a);
        // Document已经设置级联保存和更新，因此在此处加入新的Attachment，只要把attachment加入document的attachemntList中再更新document，attachment会自动插入
        documentDaoImpl.mergeWithTransaction(document);
    }

    /**
     * Description: 在数据库中创建一个暂时的Attachment，无所属Document
     * 
     * @param info
     *            void
     */
    public void createTempAttachmentEntryInDB(AttachmentInfo info)
    {
        Attachment a = new Attachment();
        a.setCreator(info.getCreator());
        a.setListName(info.getListName());
        a.setRealName(info.getRealName());
        a.setUrl(info.getUrl());
        a.setSize(info.getSize());
        a.setStatus(AttachmentStatusEnum.TEMP);
        attachmentDaoImpl.persistWithTransaction(a);
    }

    public boolean deleteAttachmentOnFTPServerAndDBByAttachmentId(int attachmentId) throws SocketException,
            IOException
    {
        Attachment attachment = attachmentDaoImpl.findById(attachmentId);
        // Delete file on ftp server
        FtpTool ftpTool = new FtpTool();
        String url = attachment.getUrl();
        boolean result = ftpTool.deleteFile(url);
        if (!result)
            return result;
        // Delete Attachment entry in db
        attachmentDaoImpl.removeWithTransaction(attachment);
        return true;
    }

    private String convertListNameToRealNameByAppendingTimeStamp(String listName)
    {
        int posOfDot = listName.lastIndexOf('.');
        String prefix = listName.substring(0, posOfDot);
        String postFix = listName.substring(posOfDot, listName.length());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        prefix += "_" + sdf.format(new Date());
        prefix += postFix;
        return prefix;
    }

    public static class SimpleAttachmentInfo
    {
        private int id;
        private String listName;
        private String uploadTime;
        private String creatorName;

        public SimpleAttachmentInfo() {
            super();
        }

        public SimpleAttachmentInfo(int id, String listName, String uploadTime, String creatorName) {
            super();
            this.id = id;
            this.listName = listName;
            this.uploadTime = uploadTime;
            this.creatorName = creatorName;
        }

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

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

    public static class AttachmentInfo
    {

        public AttachmentInfo(User creator, String realName, String listName, String size,
                String url) {
            super();
            this.creator = creator;
            this.realName = realName;
            this.listName = listName;
            this.size = size;
            this.url = url;
        }

        private User creator;
        private String realName;
        private String listName;
        private String size;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    /**
     * Description: 获得Admin的所有Attachment信息
     * 
     * @return
     *         List<SimpleAttachmentInfo>
     */
    public List<SimpleAttachmentInfo> getTempAttachmentsOfAdmin() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("role", "Administrator");
        params.put("status", AttachmentStatusEnum.TEMP);
        List<Attachment> attachments = attachmentDaoImpl
                .findForPaging("select a from Attachment a where a.creator.role=:role and a.status=:status", params);
        List<SimpleAttachmentInfo> attachmentInfos = new ArrayList<SimpleAttachmentInfo>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Attachment a : attachments)
            attachmentInfos.add(new SimpleAttachmentInfo(a.getId(), a.getListName(), sdf.format(a.getCreateTime()),
                    "管理员"));
        return attachmentInfos;
    }

}
