/**  
 * @Project: sse
 * @Title: DocumentFormModel.java
 * @Package sse.jsonmodel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年4月21日 下午6:28:13
 * @version V1.0  
 */
/**
 * 
 */
package sse.jsonmodel;

/**
 * @author yuesongwang
 *
 */
public class DocumentFormModel {

    private String document_name;
    private String document_type;
    private String document_description;

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public String getDocument_description() {
        return document_description;
    }

    public void setDocument_description(String document_description) {
        this.document_description = document_description;
    }

}
