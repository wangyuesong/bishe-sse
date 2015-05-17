/**  
 * @Project: sse
 * @Title: ActionEventModel.java
 * @Package sse.pageModel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月7日 下午3:47:30
 * @version V1.0  
 */
/**
 * 
 */
package sse.pagemodel;

/**
 * @author yuesongwang
 *
 */
public class ActionEventListModel {

    int id;
    String actor;
    String create_time;
    String description;

    public ActionEventListModel(int id, String actor, String create_time, String description) {
        super();
        this.id = id;
        this.actor = actor;
        this.create_time = create_time;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
