package sse.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;

import sse.enums.DocumentTypeEnum;
import sse.enums.MatchLevelEnum;
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
    @Column(length = 30, name = "MATCH_LEVEL")
    private MatchLevelEnum matchLevel;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, name = "MATCH_TYPE")
    private MatchTypeEnum matchType;

    @OneToOne(mappedBy = "student", cascade = { CascadeType.ALL })
    private Topic topic;

    @OneToMany(mappedBy = "creator", cascade = { CascadeType.ALL })
    private List<Document> documents;

    @OneToMany(mappedBy = "student", cascade = { CascadeType.ALL })
    private List<Will> wills;

    @OneToMany(mappedBy = "student", cascade = { CascadeType.ALL })
    private List<WeeklyReport> weeklyReports;

    public List<WeeklyReport> getWeeklyReports() {
        return weeklyReports;
    }

    public void setWeeklyReports(List<WeeklyReport> weeklyReports) {
        this.weeklyReports = weeklyReports;
    }

    public Topic getTopic() {
        return topic;
    }

    @PrePersist
    public void createMyDocuments() {
        List<String> values = DocumentTypeEnum.getAllTypeValues();
        for (String value : values)
        {
            Document d = new Document(this.getName() + "的" + value, DocumentTypeEnum.getType(value)
                    , this, this);
            this.getDocuments().add(d);
        }
    }

    public List<Will> getWills() {
        return wills;
    }

    public void setWills(List<Will> wills) {
        this.wills = wills;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public MatchLevelEnum getMatchLevel() {
        return matchLevel;
    }

    public void setMatchLevel(MatchLevelEnum matchLevel) {
        this.matchLevel = matchLevel;
    }

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

    public List<Document> getDocuments() {
        if (documents == null)
            documents = new ArrayList<Document>();
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
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
