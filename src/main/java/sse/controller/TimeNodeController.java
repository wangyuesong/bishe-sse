/**  
 * @Project: sse
 * @Title: TimeNodeController.java
 * @Package sse.controller
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年4月15日 下午7:26:55
 * @version V1.0  
 */
/**
 * 
 */
package sse.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.dao.impl.TimeNodeDaoImpl;
import sse.dao.impl.TimeNodeDaoImpl.CalendarEvent;

/**
 * @author yuesongwang
 *
 */
@Controller
@RequestMapping(value = "/timenode")
public class TimeNodeController {

    @Autowired
    TimeNodeDaoImpl timeNodeDaoImpl;

    @ResponseBody
    @RequestMapping(value = "/getTimeNodes", method = { RequestMethod.GET })
    public List<CalendarEvent> login(HttpServletRequest request, HttpServletResponse response) {
        return timeNodeDaoImpl.getAllTimeNodes();
    }
}
