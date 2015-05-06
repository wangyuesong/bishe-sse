/**  
 * @Project: sse
 * @Title: DocumentCommentListModel.java
 * @Package sse.pageModel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月6日 下午2:41:33
 * @version V1.0  
 */
/**
 * 
 */
package sse.pageModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yuesongwang
 *
 */
public class DocumentCommentListModel implements Comparable {

    private String content;
    private String createTime;
    private String commentor;

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

    public String getCommentor() {
        return commentor;
    }

    public void setCommentor(String commentor) {
        this.commentor = commentor;
    }

    public DocumentCommentListModel(String content, String createTime, String commentor) {
        super();
        this.content = content;
        this.createTime = createTime;
        this.commentor = commentor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DocumentCommentListModel dcm = (DocumentCommentListModel) o;
        Date otherObjDate = null;
        Date myDate = null;
        try {
            otherObjDate = sdf.parse(dcm.getCreateTime());
            myDate = sdf.parse(this.getCreateTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return myDate.compareTo(otherObjDate);
    }

//    public static void main(String args[])
//    {
//        DocumentCommentListModel d1 = new DocumentCommentListModel("", "2015-02-26 19:32:11", null);
//        DocumentCommentListModel d2 = new DocumentCommentListModel("", "2015-02-26 19:33:11", null);
//        if(d1.compareTo(d2) == 1)
//            System.out.println("D1 大");
//        else
//            System.out.println("D2 大");
//    }
}
