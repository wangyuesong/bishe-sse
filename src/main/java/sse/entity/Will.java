package sse.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import sse.enums.WillStatusEnum;

/**
 * The persistent class for the topic database table.
 * 
 */
@Entity
@Table(name = "WILL")
@NamedQueries(
{
        @NamedQuery(name = "Will.findAll", query = "SELECT w FROM Will w"),
        @NamedQuery(name = "Will.findAllWillByStudentId", query = "select w from Will w where w.student.id = :studentId order by w.level ASC")
})
public class Will extends BaseModel implements Serializable, Comparable<Will> {
    /**
     * 
     */
    private static final long serialVersionUID = -8076987457788526355L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Teacher teacher;

    @Column(nullable = false)
    private int level;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private WillStatusEnum status = WillStatusEnum.待定;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public WillStatusEnum getStatus() {
        return status;
    }

    public void setStatus(WillStatusEnum status) {
        this.status = status;
    }

    public Will(int id, Student student, Teacher teacher, int level, WillStatusEnum status) {
        super();
        this.id = id;
        this.student = student;
        this.teacher = teacher;
        this.level = level;
        this.status = status;
    }

    public Will() {
        super();
    }

    public Will(Student student, Teacher teacher, int level) {
        super();
        this.student = student;
        this.teacher = teacher;
        this.level = level;
    }

    public Will(Student student, Teacher teacher, int level, WillStatusEnum status) {
        super();
        this.student = student;
        this.teacher = teacher;
        this.level = level;
        this.status = status;
    }

    @Override
    public int compareTo(Will o) {
        
        return 0;
    }

}