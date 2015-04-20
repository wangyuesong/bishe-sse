package sse.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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

    // bi-directional many-to-one association to Documenttype
    @ManyToOne
    @JoinColumn(name = "DOCUMENTTYPE_ID", nullable = false)
    private Documenttype documenttype;

    @ManyToOne
    @JoinColumn(name = "LAST_MODIFIED_BY")
    private User lastModifiedBy;

    @ManyToOne
    @JoinColumn(nullable = false, name = "CREATOR")
    private Student creator;

    @OneToMany(mappedBy = "document")
    private List<DocumentComment> documentComments;

    public List<DocumentComment> getDocumentComments() {
        return documentComments;
    }

    public void setDocumentComments(List<DocumentComment> documentComments) {
        this.documentComments = documentComments;
    }

    public void setCreator(Student creator) {
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

    public Documenttype getDocumenttype() {
        return documenttype;
    }

    public void setDocumenttype(Documenttype documenttype) {
        this.documenttype = documenttype;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Student getCreator() {
        return creator;
    }

}