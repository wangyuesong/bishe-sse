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
public class TianbaozhiyuanPermission implements Permission {

    @Autowired
    TimeNodeDaoImpl timeNodeDaoImpl;

    @Override
    public String doCheck(User u) {
        Date d = new Date();
        if (d.compareTo(timeNodeDaoImpl.getTimeNodeByName("填报志愿")) == -1
                || d.compareTo(timeNodeDaoImpl.getTimeNodeByName("申报课题")) == 1)
        {
            return "此时不在志愿填报阶段";
        }
        else {
            return null;
        }
    }
}
