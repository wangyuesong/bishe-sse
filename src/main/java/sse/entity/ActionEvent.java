package sse.entity;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the topic database table.
 * 
 */
@Entity
@Table(name = "ACTION_EVENT")
public class ActionEvent extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "ACTOR")
    private User actor;

    @ManyToOne
    @JoinColumn(name = "LISTENER")
    private User listener;

    @Column(length = 300, name = "DESCRIPTION")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    public User getListener() {
        return listener;
    }

    public void setListener(User listener) {
        this.listener = listener;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActionEvent(User actor, User listener, String description) {
        super();
        this.actor = actor;
        this.listener = listener;
        this.description = description;
    }

    public ActionEvent() {
        super();
    }

}