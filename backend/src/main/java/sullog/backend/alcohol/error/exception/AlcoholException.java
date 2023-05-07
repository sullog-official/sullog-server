package sullog.backend.alcohol.error.exception;

import sullog.backend.common.error.ErrorCode;
import sullog.backend.common.error.exception.BusinessException;

public class AlcoholException extends BusinessException {

    private final ErrorCode errorCode;

    public AlcoholException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.errorCode = errorCode;
    }

    public AlcoholException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
