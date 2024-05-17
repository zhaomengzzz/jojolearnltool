package fun.ltool.core.exceptions;

import lombok.Getter;

/**
 * 有状态码的异常类
 *
 * @author huangrongsong
 * @since 1.0
 */
@Getter
public class StatusException extends RuntimeException {
    private static final long serialVersionUID = 1714987167998L;

    /**
     * 异常状态码
     */
    private int status;

    public StatusException(String message) {
        super(message);
    }

    public StatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusException(int status, String message) {
        super(message);
        this.status = status;
    }

    public StatusException(int status) {
        this.status = status;
    }


    public StatusException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public StatusException(int status, Throwable cause) {
        super(cause);
        this.status = status;
    }

    public StatusException(int status, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
    }
}
