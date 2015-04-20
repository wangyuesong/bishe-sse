package sse.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("Student")
public class Student extends User {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1793927958183962063L;
    // bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;

    @OneToMany(mappedBy = "creator")
    List<Document> documents;

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Student() {

    }

    public Student(int id, String account, String name, String password)
    {
        this.setId(id);
        this.setAccount(account);
        this.setName(name);
        this.setPassword(password);
    }

}
