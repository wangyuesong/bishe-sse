package sse.entity;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import sse.enums.DocumentTypeEnum;

/**
 * The persistent class for the document database table.
 * 
 */
@Entity
@Table(name = "DOCUMENT")
@NamedQuery(name = "Document.findAll", query = "SELECT d FROM Document d")
public class Document extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 5000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private DocumentTypeEnum documentType;

    @ManyToOne
    @JoinColumn(name = "LAST_MODIFIED_BY")
    private User lastModifiedBy;

    @ManyToOne
    @JoinColumn(nullable = false, name = "CREATOR")
    private User creator;

    @OneToMany(mappedBy = "document", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<DocumentComment> documentComments;

    @OneToMany(mappedBy = "document", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Attachment> documentAttachments;

    /** 
     * Description: 级联增加操作
     * @param a
     * void
     */
    public void addAttachment(Attachment a)
    {
        a.setDocument(this);
        documentAttachments.add(a);
    }

    /** 
     * Description: 同上
     * @param dc
     * void
     */
    public void addDocumentComment(DocumentComment dc)
    {
        dc.setDocument(this);
        documentComments.add(dc);
    }

    public Document() {
        super();
    }

    public Document(int id, String name, String content, DocumentTypeEnum documenttype, User lastModifiedBy,
            User creator) {
        super();
        this.id = id;
        this.name = name;
        this.content = content;
        this.documentType = documenttype;
        this.lastModifiedBy = lastModifiedBy;
        this.creator = creator;
    }

    public List<Attachment> getDocumentAttachments() {
        return documentAttachments;
    }

    public void setDocumentAttachments(List<Attachment> documentAttachments) {
        this.documentAttachments = documentAttachments;
    }

    public List<DocumentComment> getDocumentComments() {
        return documentComments;
    }

    public void setDocumentComments(List<DocumentComment> documentComments) {
        this.documentComments = documentComments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentTypeEnum getDocumenttype() {
        return documentType;
    }

    public void setDocumenttype(DocumentTypeEnum documenttype) {
        this.documentType = documenttype;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

}