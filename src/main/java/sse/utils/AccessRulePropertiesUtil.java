package sse.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import sse.utils.PropertiesUtil;

/**
 * @Project: sse
 * @Title: AccessRulePropertiesDaoImpl.java
 * @Package sse.dao.impl
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月14日 上午12:39:04
 * @version V1.0
 */
public class AccessRulePropertiesUtil {

    public static Map<String, String> getStudentAccessRuleNameAndURLMapping() {
        return PropertiesUtil.readProperties("/student_url_mapping.properties");
    }

    public static Map<String, String> getTeacherAccessRuleNameAndURLMapping() {
        return PropertiesUtil.readProperties("/teacher_url_mapping.properties");
    }

    public static String readProperty(String key, String role)
    {
        if (StringUtils.equals("Student", role))
            return PropertiesUtil.readProperty("/student_url_mapping.properties", key);
        else
            return PropertiesUtil.readProperty("/teacher_url_mapping.properties", key);
    }
    // public static void main(String args[])
    // {
    // Map<String, String> a = AccessRulePropertiesAccessor.getStudentAccessRuleNameAndURLMapping();
    // System.out.println("hi");
    // }
}
