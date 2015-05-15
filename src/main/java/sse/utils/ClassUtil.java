package sse.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Project: sse
 * @Title: ClassUtil.java
 * @Package sse.utils
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月15日 下午3:08:14
 * @version V1.0
 */
public class ClassUtil<J> {

    public String callGetMethod(J instance, String name)
    {
        try {
            StringBuffer methodName = new StringBuffer(name);
            methodName.setCharAt(0, Character.toUpperCase(name.charAt(0)));
            Method m = instance.getClass().getMethod(
                    "get" + methodName);
            try {

                Object object = m.invoke(instance);
                if (object instanceof Integer)
                    return (Integer) object + "";
                else if (object instanceof String)
                    return (String) object;
                else if (object instanceof Boolean)
                    return (Boolean) object + "";
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
