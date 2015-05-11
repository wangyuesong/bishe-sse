package sse.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the document database table.
 * 
 */
@Entity
@Table(name = "DOCUMENT_COMMENT")
@NamedQuery(name = "DocumentComment.findAll", query = "SELECT d FROM DocumentComment d")
public class DocumentComment extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 2000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "USER", nullable = false)
    User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PARENT_DOCUMENT", nullable = false)
    Document document;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public DocumentComment() {
        super();
    }

    public DocumentComment(String content, User user, Document document) {
        super();
        this.content = content;
        this.user = user;
        this.document = document;
    }

    public DocumentComment() {
        super();
    }

}