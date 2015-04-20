package sse.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WillPK implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5757101669065528662L;

    @Column(nullable = false)
    private Integer studentId;

    @Column(nullable = false)
    private Integer teacherId;

    public WillPK()
    {

    }

    public WillPK(int studentId, int teacherId) {
        super();
        this.studentId = studentId;
        this.teacherId = teacherId;
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

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final WillPK other = (WillPK) obj;
        
        if (studentId == null) {
            if (other.studentId != null)
                return false;
        } else if (!studentId.equals(other.studentId))
            return false;
        
        if (teacherId == null) {
            if (other.teacherId != null)
                return false;
        } else if (!teacherId.equals(other.teacherId))
            return false;
        return true;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + studentId;
        result = prime * result + teacherId;
        return result;
    }
}
