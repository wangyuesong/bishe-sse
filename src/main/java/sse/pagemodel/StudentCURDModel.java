package sse.pagemodel;

/**
 * @Project: sse
 * @Title: StudentCURDModel.java
 * @Package sse.pagemodel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月15日 下午4:39:30
 * @version V1.0
 */
public class StudentCURDModel {
    private int id;
    private String account;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String selfDescription;
    private String gender;
    private String gpa;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSelfDescription() {
        return selfDescription;
    }

    public void setSelfDescription(String selfDescription) {
        this.selfDescription = selfDescription;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public StudentCURDModel(int id, String account, String name, String email, String phone, String password,
            String selfDescription, String gender, String gpa) {
        super();
        this.id = id;
        this.account = account;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.selfDescription = selfDescription;
        this.gender = gender;
        this.gpa = gpa;
    }

}
