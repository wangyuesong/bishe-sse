package sse.entity;

import java.io.Serializable;

import javax.persistence.*;

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

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String size;

    @Column(nullable = false, length = 1500)
    private String url;

    @Column(nullable = false)
    private boolean finalVersion;

    @ManyToOne
    @JoinColumn(name = "DOCUMENT", nullable = false)
    private Document document;

    @Column(name = "LAST_MODIFIED_BY")
    private int lastModifiedBy;

    @Column(nullable = false, name = "CREATOR")
    private int creator;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public int getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(int lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

}