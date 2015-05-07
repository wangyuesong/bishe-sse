package sse.entity;

import java.io.Serializable;

import javax.persistence.*;

import sse.enums.DocumentTypeEnum;
import sse.enums.TimeNodeEnum;

import java.util.Date;

/**
 * The persistent class for the document database table.
 * 
 */
@Entity
@Table(name = "TIME_NODE")
@NamedQuery(name = "TimeNode.findAll", query = "SELECT d FROM TimeNode d")
public class TimeNode extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private TimeNodeEnum name;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false, length = 500)
    private String description;

    public TimeNode() {

    }

    public TimeNode(int id, TimeNodeEnum name, Date time, String description) {
        super();
        this.id = id;
        this.name = name;
        this.time = time;
        this.description = description;
    }

    public TimeNodeEnum getName() {
        return name;
    }

    public void setName(TimeNodeEnum name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}