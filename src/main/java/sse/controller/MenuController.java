package sse.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.entity.User;
import sse.pageModel.TreeModel;
import sse.service.impl.MenuServiceImpl;


@Controller
@RequestMapping(value = "/menu")
public class MenuController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MenuController.class);

   @Autowired 
   private MenuServiceImpl menuService;
   
   @ResponseBody
   @RequestMapping(value = "/getMenuList", method = {RequestMethod.GET,RequestMethod.POST})
   public List<TreeModel>  getMenuList(HttpServletRequest request)
   {
      return menuService.getMenu(null, (User) request.getSession().getAttribute("USER"));
   }
}