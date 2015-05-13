package sse.commandmodel;

/**
 * @Project: sse
 * @Title: DocumentCommentFormModel.java
 * @Package sse.commandmodel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月13日 上午10:58:29
 * @version V1.0
 */
public class DocumentCommentFormModel {
    String studentId;
    String commentorId;
    String content;
    String type;

    public DocumentCommentFormModel() {
        super();
    }

    public DocumentCommentFormModel(String studentId, String commentorId, String content, String type) {
        super();
        this.studentId = studentId;
        this.commentorId = commentorId;
        this.content = content;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCommentorId() {
        return commentorId;
    }

    public void setCommentorId(String commentorId) {
        this.commentorId = commentorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
