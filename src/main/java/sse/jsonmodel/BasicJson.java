package sse.jsonmodel;

import java.io.Serializable;

public class BasicJson implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private boolean success = false;

    private String msg = "";

    private Object obj = null;

    /**
     * 返回格式的JSON
     * 
     * @param success
     *            是否成功
     * @param msg
     *            消息
     * @param obj
     *            对象
     */

    public BasicJson() {
        super();
    }

    /**
     * 返回JSON数据
     * 
     * @param msg
     */
    public BasicJson(String msg) {
        super();
        this.msg = msg;
    }

    public BasicJson(boolean success, String msg, Object obj) {
        super();
        this.success = success;
        this.msg = msg;
        this.obj = obj;
    }

    /**
     * 是否成功
     * 
     * @return
     */
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

}
