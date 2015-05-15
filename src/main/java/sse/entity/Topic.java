package sse.entity;

import java.io.Serializable;

import javax.persistence.*;

import sse.enums.DocumentTypeEnum;
import sse.enums.TopicStatusEnum;
import sse.enums.TopicTypeEnum;

/**
 * The persistent class for the topic database table.
 * 
 */
@Entity
@Table(name = "TOPIC")
@NamedQuery(name = "Topic.findAll", query = "SELECT t FROM Topic t")
public class Topic extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(nullable = false, length = 500)
    private String mainName;

    @Column(nullable = false, length = 500)
    private String subName;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String outsider;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private TopicStatusEnum passStatus;

    @Column(name = "TEACHER_COMMENT", length = 1000)
    private String teacherComment;
    
    @OneToOne
    @JoinColumn(name = "STUDENT")
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private TopicTypeEnum topicType;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getOutsider() {
        return outsider;
    }

    public void setOutsider(String outsider) {
        this.outsider = outsider;
    }

    public TopicStatusEnum getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(TopicStatusEnum passStatus) {
        this.passStatus = passStatus;
    }

    public String getTeacherComment() {
        return teacherComment;
    }

    public void setTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
    }

    public TopicTypeEnum getTopicType() {
        return topicType;
    }

    public void setTopicType(TopicTypeEnum topicType) {
        this.topicType = topicType;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Topic(int id, String mainName, String subName, String description, String outsider,
            TopicStatusEnum passStatus, String teacherComment, TopicTypeEnum topicType) {
        super();
        this.id = id;
        this.mainName = mainName;
        this.subName = subName;
        this.description = description;
        this.outsider = outsider;
        this.passStatus = passStatus;
        this.teacherComment = teacherComment;
        this.topicType = topicType;
    }

    public Topic() {
        super();
    }

}