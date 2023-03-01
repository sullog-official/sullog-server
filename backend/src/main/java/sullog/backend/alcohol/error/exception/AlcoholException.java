package sullog.backend.alcohol.error.exception;

import sullog.backend.common.error.ErrorCode;

public class AlcoholException extends RuntimeException {

    private final ErrorCode errorCode;

    public AlcoholException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AlcoholException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
