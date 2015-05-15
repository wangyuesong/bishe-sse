package sse.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the document database table.
 * 
 */
@Entity
@Table(name = "ACCESS_RULE")
@NamedQuery(name = "AccessRule.findAll", query = "SELECT d FROM AccessRule d")
public class AccessRule extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 500, nullable = false)
    private String url;

    @Column(length = 50, nullable = false, name = "MENU_NAME")
    private String menuName;

    @Column(length = 100, nullable = false)
    private String role;

    @ManyToMany(mappedBy = "accessRules")
    private List<TimeNode> timeNodes;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<TimeNode> getTimeNodes() {
        if (timeNodes == null)
            timeNodes = new ArrayList<TimeNode>();
        return timeNodes;
    }

    public void setTimeNodes(List<TimeNode> timeNodes) {
        this.timeNodes = timeNodes;
    }

    public AccessRule(String url, String menuName, String role) {
        super();
        this.url = url;
        this.menuName = menuName;
        this.role = role;
    }

    public AccessRule(String url, String role, List<TimeNode> timeNodes) {
        super();
        this.url = url;
        this.role = role;
        this.timeNodes = timeNodes;
    }

    public AccessRule() {
        super();
    }

}