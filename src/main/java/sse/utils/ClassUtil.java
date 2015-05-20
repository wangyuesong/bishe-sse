package sse.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sse.exception.SSEException;

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

    /**
     * Description: 参数paramterValue可能为int或String，setMethod也可能为int或String，需要进行判断
     * 
     * @param instance
     * @param name
     * @param parameterValue
     *            void
     */
    public void callSetMethod(J instance, String name, Object parameterValue)
    {
        StringBuffer methodName = new StringBuffer(name);
        methodName.setCharAt(0, Character.toUpperCase(name.charAt(0)));
        // Method m = instance.getClass().getDeclaredMethod(
        // "set" + methodName, parameter.getClass());
        Class<?> targetMethodType = null;
        Method method = getDeclaredMethod(instance, "set" + methodName, String.class);
        targetMethodType = String.class;
        if (method == null) {
            method = getDeclaredMethod(instance, "set" + methodName, int.class);
            targetMethodType = int.class;
        }
        try {
            if (targetMethodType == String.class)
            {
                if (parameterValue instanceof String)
                    method.invoke(instance, (String) parameterValue);
                else
                    method.invoke(instance, (int) parameterValue + "");
            }
            else if (targetMethodType == int.class)
            {
                if (!(parameterValue instanceof String))
                    method.invoke(instance, (int) parameterValue + "");
                else
                    method.invoke(instance, Integer.parseInt((String) parameterValue));
            }
        } catch (Exception e)
        {
            throw new SSEException("注入失败", e);
        }

    }

    public String callGetMethod(J instance, String name)
    {
        StringBuffer methodName = new StringBuffer(name);
        methodName.setCharAt(0, Character.toUpperCase(name.charAt(0)));
        Method m = getDeclaredMethod(instance, "get" + methodName);

        Object object = null;
        try {
            object = m.invoke(instance);
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
        if (object instanceof Integer)
            return (Integer) object + "";
        else if (object instanceof String)
            return (String) object;
        else if (object instanceof Boolean)
            return (Boolean) object + "";
        return null;
    }

    // 获取方法，如果本类没有则向父类寻找
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
            }
        }
        return null;
    }

}
