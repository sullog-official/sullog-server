package sullog.backend.record.error.exception;

import sullog.backend.common.error.ErrorCode;

public class RecordException extends RuntimeException {

    private final ErrorCode errorCode;

    public RecordException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public RecordException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
