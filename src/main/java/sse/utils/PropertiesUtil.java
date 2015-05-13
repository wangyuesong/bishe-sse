package sse.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import sse.exception.SSEException;

/**
 * @Project: sse
 * @Title: PropertiesUtil.java
 * @Package sse.utils
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月14日 上午12:32:46
 * @version V1.0
 */
public class PropertiesUtil {
    public static Map<String, String> readProperties(String contextUrl)
    {
        Map<String, String> propMap = new HashMap<String, String>();
        Properties prop = new Properties();
        InputStream in = new PropertiesUtil().getClass().getResourceAsStream(contextUrl);
        try {
            prop.load(in);
        } catch (IOException e) {
            throw new SSEException("属性文件无法获取", e);
        }
        Set<Object> keyValues = prop.keySet();
        for (Iterator it = keyValues.iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            propMap.put(key, prop.getProperty(key));
        }
        return propMap;
    }
}
