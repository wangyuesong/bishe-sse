package sse.pagemodel;

/**
 * @Project: sse
 * @Title: StudentListModel.java
 * @Package sse.pageModel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月9日 上午11:32:14
 * @version V1.0
 */
public class SystemMessageListModel {

    int id;
    String title;
    String content;
    String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public SystemMessageListModel(int id, String title, String content, String createTime) {
        super();
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

}
