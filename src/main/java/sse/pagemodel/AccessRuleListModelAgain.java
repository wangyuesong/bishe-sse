package sse.pagemodel;

/**
 * @Project: sse
 * @Title: AccessRuleListModelAgain.java
 * @Package sse.pagemodel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月18日 下午2:44:01
 * @version V1.0
 */
public class AccessRuleListModelAgain {
    private String text;
    private String group;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public AccessRuleListModelAgain(String text, String group) {
        super();
        this.text = text;
        this.group = group;
    }

}
