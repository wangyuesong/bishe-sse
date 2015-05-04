package sse.entity;

import java.io.Serializable;
import javax.persistence.*;
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

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false, length = 500)
    private String description;

    public TimeNode() {

    }

    public TimeNode(String name, Date timeDate, String description)
    {
        this.name = name;
        this.time = timeDate;
        this.description = description;
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