package sse.utils;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import sse.entity.User;

/**
 * @Project: sse
 * @Title: SessionUtil.java
 * @Package sse.utils
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月9日 上午11:46:54
 * @version V1.0
 */
public class SessionUtil {

    public static User getUserFromSession(HttpServletRequest request)
    {
        return (User) request.getSession().getAttribute("USER");
    }
}
