package fun.ltool.core.exceptions;

/**
 * 反射处理异常类
 *
 * @author huangrongsong
 * @since 1.1
 */
public class ReflectException extends StatusException {
    private static final long serialVersionUID = 17149871679997L;

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException(int status, String message) {
        super(status, message);
    }

    public ReflectException(int status) {
        super(status);
    }

    public ReflectException(int status, String message, Throwable cause) {
        super(status, message, cause);
    }

    public ReflectException(int status, Throwable cause) {
        super(status, cause);
    }

    public ReflectException(int status, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(status, message, cause, enableSuppression, writableStackTrace);
    }
}
