package sse.entity;

import java.io.Serializable;

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
@Table(name = "WEEEKLY_REPORT")
@NamedQuery(name = "WeeklyReport.findAll", query = "SELECT d FROM WeeklyReport d")
public class WeeklyReport extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 500)
    private String workDone;

    @Column(length = 500)
    private String workToBeDone;

    @Column(length = 500)
    private String encounteredProblems;

    @Column
    private int hoursSpentPerWeek;

    @JoinColumn(name = "student")
    @ManyToOne
    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkDone() {
        return workDone;
    }

    public void setWorkDone(String workDone) {
        this.workDone = workDone;
    }

    public String getWorkToBeDone() {
        return workToBeDone;
    }

    public void setWorkToBeDone(String workToBeDone) {
        this.workToBeDone = workToBeDone;
    }

    public String getEncounteredProblems() {
        return encounteredProblems;
    }

    public void setEncounteredProblems(String encounteredProblems) {
        this.encounteredProblems = encounteredProblems;
    }

    public int getHoursSpentPerWeek() {
        return hoursSpentPerWeek;
    }

    public void setHoursSpentPerWeek(int hoursSpentPerWeek) {
        this.hoursSpentPerWeek = hoursSpentPerWeek;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}