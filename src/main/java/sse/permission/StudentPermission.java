/**  
 * @Project: sse
 * @Title: StudentPermission.java
 * @Package sse.permission
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月6日 下午11:35:22
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
public class StudentPermission implements Permission {

    @Override
    public String doCheck(User u) {
        if (u.getClass().getName() != "Student")
            return "只有学生才能进入该页面";
        else {
            return null;
        }
    }
}
