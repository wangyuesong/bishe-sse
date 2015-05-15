/**  
 * @Project: sse
 * @Title: TeacherModel.java
 * @Package sse.jsonmodel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年4月16日 下午4:50:36
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
public class TeacherListModel {

    private int id;
    private String name;
    private String account;
    private int capacity;
    private String gender;
    private String email;
    private String phone;

    public TeacherListModel(int id, String name, String account, int capacity, String gender, String email, String phone) {
        super();
        this.id = id;
        this.name = name;
        this.account = account;
        this.capacity = capacity;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
