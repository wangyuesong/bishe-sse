package sse.exception;

/**  
 * @Project: sse
 * @Title: SSEException.java
 * @Package 
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年4月20日 下午2:01:33
 * @version V1.0  
 */
/**
 * 
 */
/**
 * @author yuesongwang
 *
 */
public class SSEException extends RuntimeException {

    private static final long serialVersionUID = -1606546240450809089L;

    public SSEException(String s) {
        super(s);
    }

    public SSEException(String msg, Throwable t)
    {
        super(msg, t);
    }

    public SSEException(Throwable t) {
        super(t);
    }
}
