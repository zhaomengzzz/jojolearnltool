package fun.ltool.core.exceptions;

/**
 * 验证异常类
 *
 * @author huangrongsong
 * @since 1.0
 */
public class ValidatorException extends StatusException {
    private static final long serialVersionUID = 1714987167999L;

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(int status, String message) {
        super(status, message);
    }

    public ValidatorException(int status) {
        super(status);
    }

    public ValidatorException(int status, String message, Throwable cause) {
        super(status, message, cause);
    }

    public ValidatorException(int status, Throwable cause) {
        super(status, cause);
    }

    public ValidatorException(int status, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(status, message, cause, enableSuppression, writableStackTrace);
    }
}
