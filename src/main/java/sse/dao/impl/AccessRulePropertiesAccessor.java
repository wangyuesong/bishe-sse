package sse.dao.impl;

import java.util.HashMap;
import java.util.Map;

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
public class AccessRulePropertiesAccessor {

    public static Map<String, String> getStudentAccessRuleNameAndURLMapping() {
        return PropertiesUtil.readProperties("/student_url_mapping.properties");
    }

    public static void main(String args[])
    {
        Map<String, String> a = AccessRulePropertiesAccessor.getStudentAccessRuleNameAndURLMapping();
        System.out.println("hi");
    }
}
