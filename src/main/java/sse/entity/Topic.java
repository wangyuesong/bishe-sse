package sse.entity;

import java.io.Serializable;
import javax.persistence.*;

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

	@Column(length = 1000)
	private String description;

	@Column(nullable = false, length = 500)
	private String name;

	@Column(length = 500)
	private String outsider;

	@Column(name = "PASS_STATUS", length = 45)
	private String passStatus;

	@Column(name = "TEACHER_COMMENT", length = 1000)
	private String teacherComment;

	// bi-directional many-to-one association to Topictype
	@ManyToOne
	@JoinColumn(name = "TOPICTYPE_ID", nullable = false)
	private Topictype topictype;

	public Topic() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOutsider() {
		return outsider;
	}

	public void setOutsider(String outsider) {
		this.outsider = outsider;
	}

	public String getPassStatus() {
		return passStatus;
	}

	public void setPassStatus(String passStatus) {
		this.passStatus = passStatus;
	}

	public String getTeacherComment() {
		return teacherComment;
	}

	public void setTeacherComment(String teacherComment) {
		this.teacherComment = teacherComment;
	}

	public Topictype getTopictype() {
		return topictype;
	}

	public void setTopictype(Topictype topictype) {
		this.topictype = topictype;
	}

}