package sse.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "MENU")
@NamedQuery(name = "Menu.findAll", query = "SELECT m FROM Menu m")
public class Menu implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5188855476652071339L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column
    private Integer parentId;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 2000)
    private String url;

    @Column(nullable = false, length = 45)
    private String role;

    public Menu() {

    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Menu(String name, Integer parentId, String role, String url) {
        this.name = name;
        this.role = role;
        this.url = url;
        this.parentId = parentId;
    }

    public Menu(int id, String name, Integer parentId, String role, String url) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.url = url;
        this.parentId = parentId;
    }
}
