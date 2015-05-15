package sse.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import sse.enums.TimeNodeTypeEnum;

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
    private TimeNodeTypeEnum type;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false, length = 500)
    private String description;

    // 时间节点被删除，则规则全部级连删除
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "TIMENODE_ACCESSRULE", inverseJoinColumns = { @JoinColumn(name = "ACCESS_RULE_ID", referencedColumnName = "ID") },
            joinColumns = { @JoinColumn(name = "TIME_NODE_ID", referencedColumnName = "ID") })
    List<AccessRule> accessRules;

    public void addAccessRule(AccessRule r)
    {
        this.getAccessRules().add(r);
        r.getTimeNodes().add(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TimeNodeTypeEnum getType() {
        return type;
    }

    public void setType(TimeNodeTypeEnum type) {
        this.type = type;
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

    public List<AccessRule> getAccessRules() {
        if (accessRules == null)
            accessRules = new ArrayList<AccessRule>();
        return accessRules;
    }

    public void setAccessRules(List<AccessRule> accessRules) {
        this.accessRules = accessRules;
    }

    public TimeNode(int id, TimeNodeTypeEnum type, String name, Date time, String description,
            List<AccessRule> accessRules) {
        super();
        this.id = id;
        this.type = type;
        this.name = name;
        this.time = time;
        this.description = description;
        this.accessRules = accessRules;
    }

    public TimeNode(int id, TimeNodeTypeEnum type, String name, Date time, String description) {
        super();
        this.id = id;
        this.type = type;
        this.name = name;
        this.time = time;
        this.description = description;
    }

    public TimeNode(TimeNodeTypeEnum type, String name, Date time, String description) {
        super();
        this.type = type;
        this.name = name;
        this.time = time;
        this.description = description;
    }

    public TimeNode(TimeNodeTypeEnum type, String name, Date time, String description, List<AccessRule> accessRules) {
        super();
        this.type = type;
        this.name = name;
        this.time = time;
        this.description = description;
        this.accessRules = accessRules;
    }

    public TimeNode() {
        super();
    }

}