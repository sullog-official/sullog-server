package sullog.backend.record.error.exception;

import sullog.backend.common.error.ErrorCode;
import sullog.backend.common.error.exception.BusinessException;

public class RecordException extends BusinessException {

    private final ErrorCode errorCode;

    public RecordException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.errorCode = errorCode;
    }

    public RecordException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
