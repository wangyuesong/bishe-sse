package sse.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.commandmodel.LoginForm;
import sse.dao.impl.UserDaoImpl;
import sse.entity.User;
import sse.jsonmodel.BasicJson;

@Service
public class UserServiceImpl {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDaoImpl userDaoImpl;

    /*
     * (non-Javadoc)
     * 
     * @see sse.service.impl.IUserService#doLogin(sse.commandmodel.LoginForm)
     */
    public BasicJson doLogin(LoginForm command, HttpServletRequest request)
    {
        BasicJson json = new BasicJson();
        User user = userDaoImpl.findUserByAccount(command.getAccount());
        if (user == null || !user.getPassword().equals(command.getPassword()))
        {
            json.setSuccess(false);
            json.setMsg("用户名不存在或密码不正确");
            return json;
        }
        else {
            request.getSession().setAttribute("USER", user);
            logger.info("Login Succeed: " + user.getAccount());
            if (user.getRole().equals("Student"))
                json.setObj(new RoleObject("Student"));
            if (user.getRole().equals("Teacher"))
                json.setObj(new RoleObject("Teacher"));
            json.setSuccess(true);

        }
        return json;
    }

    public static class RoleObject {
        private String role;

        public RoleObject(String role)
        {
            this.role = role;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

    }

}
