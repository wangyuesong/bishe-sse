/**  
 * @Project: sse
 * @Title: TeacherModel.java
 * @Package sse.jsonmodel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年4月16日 下午4:50:36
 * @version V1.0  
 */
/**
 * 
 */
package sse.jsonmodel;

import sse.entity.User;

/**
 * @author yuesongwang
 *
 */
public class TeacherModel extends User {
    private String title;

    private int capacity;

    private String degree;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

}
