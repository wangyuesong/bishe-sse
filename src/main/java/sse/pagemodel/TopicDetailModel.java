package sse.pagemodel;

/**
 * @Project: sse
 * @Title: TopicModel.java
 * @Package sse.pageModel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月12日 上午12:37:01
 * @version V1.0
 */
public class TopicDetailModel {
    int id;
    String description;
    String mainName;
    String subName;
    String outsider;
    String passStatus;
    String teacherComment;
    String topicType;

    public TopicDetailModel() {
        super();
    }

    public int getId() {
        return id;
    }

    public TopicDetailModel(int id, String description, String mainName, String subName, String outsider,
            String passStatus,
            String teacherComment, String topicType) {
        super();
        this.id = id;
        this.description = description;
        this.mainName = mainName;
        this.subName = subName;
        this.outsider = outsider;
        this.passStatus = passStatus;
        this.teacherComment = teacherComment;
        this.topicType = topicType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getOutsider() {
        return outsider;
    }

    public void setOutsider(String outsider) {
        this.outsider = outsider;
    }

    public String getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(String passStatus) {
        this.passStatus = passStatus;
    }

    public String getTeacherComment() {
        return teacherComment;
    }

    public void setTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

}
