package sse.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

@Entity
@Table(name = "SYSTEM_MESSAGE")
@NamedQuery(name = "SystemMessage.findAll", query = "SELECT m FROM SystemMessage m")
public class SystemMessage extends BaseModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4350945084351891427L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 500)
    private String title;

    @Column(length = 5000)
    private String content;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH }, mappedBy = "groupMessage")
    private List<Attachment> attachments;

    public SystemMessage() {
        super();
    }

    public SystemMessage(int id, String title, String content) {
        super();
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
