package sse.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AjaxRenderViewController {

    @RequestMapping(value = "/dispatch/{url}/{url2}", method = {RequestMethod.GET})
    public ModelAndView doRedirect2(
            @PathVariable(value = "url") String redirectUrl,
            @PathVariable(value = "url2") String redirectUrl2)
    {
        return new ModelAndView(redirectUrl + "/" + redirectUrl2);
    }

    @RequestMapping(value = "/dispatch/{url}", method = {RequestMethod.GET})
    public ModelAndView doRedirect(@PathVariable(value = "url") String redirectUrl)
    {
        return new ModelAndView(redirectUrl);
    }
}
