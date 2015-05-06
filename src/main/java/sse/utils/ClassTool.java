/**  
 * @Project: sse
 * @Title: ClassTools.java
 * @Package sse.utils
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年4月16日 下午4:54:07
 * @version V1.0  
 */
/**
 * 
 */
package sse.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import sse.entity.Teacher;
import sse.pageModel.TeacherListModel;

/**
 * @author yuesongwang
 *
 */
public class ClassTool<J, P> {

    Class<?> jpaEntityClass;
    Class<?> pojoEntityClass;

    public ClassTool(Class<J> j, Class<P> p)
    {
        jpaEntityClass = j;
        pojoEntityClass = p;
    }

    private void recursiveGetFields(List<Field> fields, Class<?> childClass)
    {
        for (Field f : childClass.getDeclaredFields())
            fields.add(f);
        if (childClass.getSuperclass() != null)
            recursiveGetFields(fields, childClass.getSuperclass());
    }

    public P convertJPAEntityToPOJO(J jpaEntity)
    {
        P pojo = null;
        try {
            pojo = (P) pojoEntityClass.newInstance();
            List<Field> fields = new LinkedList<Field>();
            recursiveGetFields(fields, pojoEntityClass);
            for (Field f : fields)
            {
                StringBuffer methodName = new StringBuffer(f.getName());
                methodName.setCharAt(0, Character.toUpperCase(methodName.charAt(0)));
                String setMethodString = "set" + methodName.toString();
                String getMethodString = "get" + methodName.toString();

                try {
                    Method setMethod = pojoEntityClass.getMethod(setMethodString, f.getType());
                    Method getMethod = jpaEntityClass.getMethod(getMethodString);
                    setMethod.invoke(pojo, getMethod.invoke(jpaEntity));
                } catch (Exception e)
                {
                    continue;
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return pojo;
    }

//    public static void main(String args[])
//    {
//        ClassTool<Teacher, TeacherListModel> classConverter = new ClassTool<Teacher, TeacherListModel>(Teacher.class, TeacherListModel.class);
//        Teacher t = new Teacher();
//        t.setName("Shabi");
//        t.setCapacity(10);
//
//        TeacherListModel tm = new TeacherListModel();
//        tm = classConverter.convertJPAEntityToPOJO(t);
//        System.out.println(tm.getName() + " " + tm.getCapacity());
//    }
}
