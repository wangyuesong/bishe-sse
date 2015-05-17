package sse.pagemodel;

/**
 * @Project: sse
 * @Title: TopicListModel.java
 * @Package sse.pagemodel
 * @Description: 用于teacher_review_topic.jsp
 * @author YuesongWang
 * @date 2015年5月16日 下午10:22:50
 * @version V1.0
 */
public class TopicListModel {
    int id;
    int studentId;
    String studentName;
    String mainName;
    String subName;
    String createTime;
    String updateTime;
    String passStatus;

    public int getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(String passStatus) {
        this.passStatus = passStatus;
    }

    public TopicListModel(int id, int studentId, String studentName, String mainName, String subName,
            String createTime, String updateTime, String passStatus) {
        super();
        this.id = id;
        this.studentName = studentName;
        this.studentId = studentId;
        this.mainName = mainName;
        this.subName = subName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.passStatus = passStatus;
    }

}
