package sse.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.commandmodel.LoginForm;
import sse.service.impl.AdminServiceImpl;

/**
 * @author yuesongwang
 *
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    public AdminServiceImpl adminServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/doMatch", method = { RequestMethod.GET })
    public void login(HttpServletRequest request, HttpServletResponse response) {
        adminServiceImpl.doMatch();
    }

}
