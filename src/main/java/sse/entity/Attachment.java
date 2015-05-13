package sse.entity;

import java.io.Serializable;

import javax.persistence.*;

import sse.enums.AttachmentStatusEnum;
import sse.enums.WillStatusEnum;

import java.sql.Timestamp;

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

    @ManyToOne()
    @JoinColumn(name = "SYSTEM_MESSAGE")
    private SystemMessage groupMessage;

    @ManyToOne
    @JoinColumn(nullable = false, name = "CREATOR")
    private User creator;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private AttachmentStatusEnum status;

    public SystemMessage getGroupMessage() {
        return groupMessage;
    }

    public void setGroupMessage(SystemMessage groupMessage) {
        this.groupMessage = groupMessage;
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