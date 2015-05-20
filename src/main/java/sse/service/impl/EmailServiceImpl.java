//package sse.service.impl;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.struts2.ServletActionContext;
//
//import com.feng.common.security.SecurityEncoder;
//import com.feng.common.security.ThreeDES;
//import com.feng.common.util.WebIpUtil;
//import com.feng.dto.TpUsers;
//import com.feng.email.Mailer;
//
///**
// * @Project: sse
// * @Title: EmailServiceImpl.java
// * @Package sse.service.impl
// * @Description: TODO
// * @author YuesongWang
// * @date 2015年5月19日 上午1:43:40
// * @version V1.0
// */
//public class EmailServiceImpl {
//    private Mailer mailer;
//    
//    public void sendValidateEmail(HttpServletRequest request) throws Exception {
//        try {
//            String path = request.getContextPath();
//            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
//                    + path + "/";
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("users", users);
//            ThreeDES des = new ThreeDES();
//            des.getKey(users.getSusercode());
//            String enc = des.getEncString(users.getSusercode() + "_" + users.getSemail() + users.getSusercode()
//                    + users.getSregip() + "_" + System.currentTimeMillis());// 将注册时的用户代码 注册IP 发送时间
//            map.put("url",
//                    basePath + "validateEmailAction.html?email=" + SecurityEncoder.encryptBase64(users.getSemail())
//                            + "&validate=" + enc);
//            mailer.templateSend(map, new String[] { users.getSemail() }, "欢迎使用【志峰创业室】云系统-校验您的邮箱", "validate-email.vm");
//        } catch (Exception e) {
//            log.error("发送", e);
//        }
//    }
//}
