package sse.permission;

import java.util.List;

import sse.entity.TimeNode;

/**
 * @Project: sse
 * @Title: PermissionCheckEntity.java
 * @Package sse.permission
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月13日 下午4:15:03
 * @version V1.0
 */
public class PermissionCheckEntity {

    private TimeNode timeNode;

    private List<String> bannedUrl;

    public TimeNode getTimeNode() {
        return timeNode;
    }

    public void setTimeNode(TimeNode timeNode) {
        this.timeNode = timeNode;
    }

    public List<String> getBannedUrl() {
        return bannedUrl;
    }

    public void setBannedUrl(List<String> bannedUrl) {
        this.bannedUrl = bannedUrl;
    }
    
}
