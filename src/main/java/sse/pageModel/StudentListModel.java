package sse.pageModel;

/**
 * @Project: sse
 * @Title: StudentListModel.java
 * @Package sse.pageModel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月9日 上午11:32:14
 * @version V1.0
 */
public class StudentListModel {

    int id;
    String account;
    String name;
    String email;
    String phone;

    public StudentListModel(int id, String account, String name, String email, String phone) {
        super();
        this.id = id;
        this.account = account;
        this.name = name;
        this.email = email;
        this.phone = phone;
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
