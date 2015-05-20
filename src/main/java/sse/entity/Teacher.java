package sse.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author yuesongwang
 *
 */
@Entity
@DiscriminatorValue("Teacher")
public class Teacher extends User {
    /**
	 * 
	 */
    private static final long serialVersionUID = 400018318739519613L;

    // bi-directional many-to-one association to User
    @OneToMany(mappedBy = "teacher", cascade = { CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE })
    private List<Student> students;

    @Column(length = 45)
    private String title;

    @Column
    private int capacity;

    @Column(length = 45)
    private String degree;

    @Column(length = 500)
    private String direction;

    @Column(length = 1000, name = "CANDIDATE_TOPICS")
    private String candidateTopics;

    @OneToMany(mappedBy = "teacher", cascade = { CascadeType.ALL })
    private List<Will> wills;

    public void addStudent(Student s)
    {
        s.setTeacher(this);
        getStudents().add(s);
    }

    public void removeStudent(Student s)
    {
        students.remove(s);
        s.setTeacher(null);
    }

    public String getCandidateTopics() {
        return candidateTopics;
    }

    public void setCandidateTopics(String candidateTopics) {
        this.candidateTopics = candidateTopics;
    }

    public List<Will> getWills() {
        return wills;
    }

    public void setWills(List<Will> wills) {
        this.wills = wills;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<Student> getStudents() {
        if (students == null)
            students = new ArrayList<Student>();
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Teacher() {

    }

    /**
     * @param id
     * @param account
     * @param name
     * @param password
     * @param capacity
     */
    public Teacher(int id, String account, String name, String password, int capacity)
    {
        this.setId(id);
        this.setAccount(account);
        this.setName(name);
        this.setPassword(password);
        this.setCapacity(capacity);
    }

}
