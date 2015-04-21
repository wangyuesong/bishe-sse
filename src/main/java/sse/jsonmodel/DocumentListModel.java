/**  
 * @Project: sse
 * @Title: DocumentModel.java
 * @Package sse.jsonmodel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年4月16日 下午7:19:37
 * @version V1.0  
 */
/**
 * 
 */
package sse.jsonmodel;

public class DocumentListModel {
    private int id;
    private String name;
    private String lastModifiedBy;
    private String creator;
    private String documentType;
    private int documentCommentsCount;

    public int getDocumentCommentsCount() {
        return documentCommentsCount;
    }

    public void setDocumentCommentsCount(int documentCommentsCount) {
        this.documentCommentsCount = documentCommentsCount;
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

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

}
