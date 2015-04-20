package sse.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the topictype database table.
 * 
 */
@Entity
@Table(name="TOPIC_TYPE")
@NamedQuery(name="Topictype.findAll", query="SELECT t FROM Topictype t")
public class Topictype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="TYPE_NAME", nullable=false, length=45)
	private String typeName;

	//bi-directional many-to-one association to Topic
	@OneToMany(mappedBy="topictype")
	private List<Topic> topics;

	public Topictype() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<Topic> getTopics() {
		return this.topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

	public Topic addTopic(Topic topic) {
		getTopics().add(topic);
		topic.setTopictype(this);

		return topic;
	}

	public Topic removeTopic(Topic topic) {
		getTopics().remove(topic);
		topic.setTopictype(null);

		return topic;
	}

}