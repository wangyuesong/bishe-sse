package sse.pagemodel;

/**
 * @Project: sse
 * @Title: CandidateStudentListModel.java
 * @Package sse.pageModel
 * @Description: 用于teacher_select_student.jsp,展示第一志愿选择该教师的学生
 * @author YuesongWang
 * @date 2015年5月11日 下午8:42:55
 * @version V1.0
 */
public class CandidateStudentListModel {
    int willId;
    int studentId;
    String account;
    String name;
    String email;
    String phone;
    String status;

    public int getWillId() {
        return willId;
    }

    public void setWillId(int willId) {
        this.willId = willId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CandidateStudentListModel(int willId, int studentId, String account, String name, String email,
            String phone, String status) {
        super();
        this.willId = willId;
        this.studentId = studentId;
        this.account = account;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

}
