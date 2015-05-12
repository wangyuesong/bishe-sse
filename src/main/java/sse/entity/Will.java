package sse.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
        @NamedQuery(name = "Will.findAllWillByStudentId", query = "select w from Will w where w.studentId = :studentId order by w.level ASC")
})
public class Will extends BaseModel implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8076987457788526355L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(nullable = false)
    private int studentId;

    @Column(nullable = false)
    private int teacherId;

    @Column(nullable = false)
    private int level;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private WillStatusEnum status = WillStatusEnum.待定;

    public Will() {
        super();
    }

    public Will(int studentId, int teacherId, int level, WillStatusEnum status) {
        super();
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.level = level;
        this.status = status;
    }

    public Will(int studentId, int teacherId, int level) {
        super();
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
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

}