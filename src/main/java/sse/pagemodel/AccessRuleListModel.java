package sse.pagemodel;

/**
 * @Project: sse
 * @Title: AccessRuleListModel.java
 * @Package sse.pageModel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月14日 上午1:29:23
 * @version V1.0
 */
public class AccessRuleListModel {
    String url;
    String name;
    // 下面这个属性仅用在getStudentAccessRulesByTimeNodeId中
    boolean banned;

    public String getUrl() {
        return url;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccessRuleListModel(String url, String name) {
        super();
        this.url = url;
        this.name = name;
    }

    public AccessRuleListModel(String url, String name, boolean banned) {
        super();
        this.url = url;
        this.name = name;
        this.banned = banned;
    }

}
