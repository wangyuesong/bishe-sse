package sse.entity;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import sse.enums.AttachmentStatusEnum;
import sse.exception.SSEException;
import sse.utils.FtpTool;

/**
 * The persistent class for the document database table.
 * 
 */
@Entity
@Table(name = "ATTACHMENT")
@NamedQuery(name = "Attachment.findAll", query = "SELECT d FROM Attachment d")
public class Attachment extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(nullable = false, length = 500)
    private String realName;

    @Column(nullable = false, length = 500)
    private String listName;

    @Column(nullable = false, length = 100)
    private String size;

    @Column(nullable = false, length = 1500)
    private String url;

    @Column(nullable = false)
    private boolean finalVersion;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "DOCUMENT")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "SYSTEM_MESSAGE")
    private SystemMessage systemMessage;

    @ManyToOne
    @JoinColumn(nullable = false, name = "CREATOR")
    private User creator;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private AttachmentStatusEnum status;

    // 附件在数据库的记录删除前，将FTPServer上的也删除
    @PreRemove
    public void deleteAttachmentOnFtpServer()
    {
        FtpTool ftpTool = new FtpTool();
        String url = this.getUrl();
        try {
            ftpTool.deleteFile(url);
        } catch (IOException e) {
            throw new SSEException("删除附件错误", e);
        }
    }

    public SystemMessage getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(SystemMessage groupMessage) {
        this.systemMessage = groupMessage;
    }

    public AttachmentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AttachmentStatusEnum status) {
        this.status = status;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSize() {
        return size;
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

    public boolean isFinalVersion() {
        return finalVersion;
    }

    public void setFinalVersion(boolean finalVersion) {
        this.finalVersion = finalVersion;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

}