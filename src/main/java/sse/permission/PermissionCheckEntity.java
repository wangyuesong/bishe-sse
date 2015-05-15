package sse.permission;

import java.util.LinkedList;
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
public class PermissionCheckEntity implements Comparable {

    private TimeNode timeNode;

    private List<String> bannedUrls;

    public TimeNode getTimeNode() {
        return timeNode;
    }

    public void setTimeNode(TimeNode timeNode) {
        this.timeNode = timeNode;
    }

    public List<String> getBannedUrls() {
        if (bannedUrls == null)
            bannedUrls = new LinkedList<String>();
        return bannedUrls;
    }

    public void setBannedUrls(List<String> bannedUrl) {
        this.bannedUrls = bannedUrl;
    }

    /**
     * Description: 检测规则
     * 
     * @param requestUrl
     * @return
     *         boolean
     */
    public boolean doPermissionCheck(String requestUrl)
    {
        for (String bannedUrl : getBannedUrls())
            if (requestUrl.contains(bannedUrl))
                return false;
        return true;
    }

    /**
     * Description: PermissionCheckEntity排序规则，用于在List中按时间序列由小到大排序
     * 
     * @param requestUrl
     * @return
     *         boolean
     */
    @Override
    public int compareTo(Object obj) {
        PermissionCheckEntity otherEntity = (PermissionCheckEntity) obj;
        return this.timeNode.getTime().compareTo(otherEntity.getTimeNode().getTime());
    }
}
