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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import sse.dao.impl.TimeNodeDaoImpl;
import sse.entity.User;

/**
 * @author yuesongwang
 *
 */
public class BishejinxingPermission implements Permission {

    @Autowired
    TimeNodeDaoImpl timeNodeDaoImpl;

    @Override
    public String doCheck(User u) {
        Date d = new Date();
        if (d.compareTo(timeNodeDaoImpl.getTimeNodeByName("课题申报")) == -1
                || d.compareTo(timeNodeDaoImpl.getTimeNodeByName("答辩申请")) == 1)
        {
            return "此时不在毕设进行阶段";
        }
        else {
            return null;
        }
    }
}
