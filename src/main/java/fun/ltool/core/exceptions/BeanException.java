package fun.ltool.core.exceptions;

/**
 * JavaBean处理异常类
 *
 * @author huangrongsong
 * @since 1.1
 */
public class BeanException extends StatusException {
    private static final long serialVersionUID = 17149871679997L;

    public BeanException(String message) {
        super(message);
    }

    public BeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanException(int status, String message) {
        super(status, message);
    }

    public BeanException(int status) {
        super(status);
    }

    public BeanException(int status, String message, Throwable cause) {
        super(status, message, cause);
    }

    public BeanException(int status, Throwable cause) {
        super(status, cause);
    }

    public BeanException(int status, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(status, message, cause, enableSuppression, writableStackTrace);
    }
}
