/**  
 * @Project: sse
 * @Title: TeacherSelectModel.java
 * @Package sse.jsonmodel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年4月22日 下午6:57:23
 * @version V1.0  
 */
/**
 * 
 */
package sse.pageModel;

/**
 * @author yuesongwang
 *
 */
public class TeacherSelectModel {

    private int id;
    private String account;
    private String name;
    private int capacity;

    public TeacherSelectModel(int id, String account, String name, int capacity) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
