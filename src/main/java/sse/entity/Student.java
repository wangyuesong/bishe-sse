package sse.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import sse.enums.MatchTypeEnum;

@Entity
@DiscriminatorValue("Student")
public class Student extends User {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1793927958183962063L;
    // bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "TEACHER")
    private Teacher teacher;

    // Record how this student got matched with teacher
    @Enumerated(EnumType.STRING)
    @Column(length = 30, name = "MATCH_TYPE")
    private MatchTypeEnum matchType;

    public MatchTypeEnum getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchTypeEnum matchType) {
        this.matchType = matchType;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
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
