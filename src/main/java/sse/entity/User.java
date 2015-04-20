package sse.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name = "USER")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ROLE", discriminatorType = DiscriminatorType.STRING)
public abstract class User extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(nullable = false, length = 45)
    private String account;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 45)
    private String password;

    @Column(length = 1000)
    private String email;

    @Column(length = 45)
    private String gender;

    @Column(length = 45)
    private String gpa;

    @Column(length = 1000)
    private String phone;

    @Column(nullable = false, length = 45)
    private String role;

    @Column(name = "SELF_DESCRIPTION", length = 1000)
    private String selfDescription;

    // // bi-directional many-to-one association to Document
    // @OneToMany(mappedBy = "user")
    // private List<Document> documents;
    @OneToMany(mappedBy = "creator")
    private List<Attachment> attachments;

    @OneToMany(mappedBy = "creator")
    private List<Document> documents;

    @OneToMany(mappedBy = "user")
    private List<DocumentComment> documentComments;

    public User() {
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<DocumentComment> getDocumentComments() {
        return documentComments;
    }

    public void setDocumentComments(List<DocumentComment> documentComments) {
        this.documentComments = documentComments;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGpa() {
        return this.gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSelfDescription() {
        return this.selfDescription;
    }

    public void setSelfDescription(String selfDescription) {
        this.selfDescription = selfDescription;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    // public List<Document> getDocuments() {
    // return this.documents;
    // }
    //
    // public void setDocuments(List<Document> documents) {
    // this.documents = documents;
    // }

}