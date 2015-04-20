package sse.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
        @NamedQuery(name = "Will.findAllWillByStudentId", query = "select w from Will w where w.id.studentId = :studentId order by w.level ASC")
})
public class Will extends BaseModel implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8076987457788526355L;

    @EmbeddedId
    private WillPK id;

    @Column(nullable = false)
    private int level;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private WillStatusEnum status = WillStatusEnum.PENDING;

    public Will()
    {

    }

    public WillStatusEnum getStatus() {
        return status;
    }

    public void setStatus(WillStatusEnum status) {
        this.status = status;
    }

    public Will(WillPK id, int level)
    {
        this.id = id;
        this.level = level;
    }

    public WillPK getId() {
        return id;
    }

    public void setId(WillPK id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Will other = (Will) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}