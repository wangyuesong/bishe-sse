/**  
 * @Project: sse
 * @Title: Permission.java
 * @Package sse.permission
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月6日 下午11:37:03
 * @version V1.0  
 */
/**
 * 
 */
package sse.permission;

import sse.entity.User;

/**
 * @author yuesongwang
 *
 */
public interface Permission {

    public String doCheck(User u);
}
