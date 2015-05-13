package sse.pageModel;

/**
 * @Project: sse
 * @Title: TimeNodeListModel.java
 * @Package sse.pageModel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月13日 下午4:59:55
 * @version V1.0
 */
public class TimeNodeListModel {
    int id;
    String name;
    String description;
    String time;
    String type;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TimeNodeListModel(int id, String name, String description, String time, String type) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.time = time;
        this.type = type;
    }

    public TimeNodeListModel() {
        super();
    }

}
