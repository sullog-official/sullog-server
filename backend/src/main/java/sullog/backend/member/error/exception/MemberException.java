package sullog.backend.member.error.exception;

import sullog.backend.common.error.ErrorCode;
import sullog.backend.common.error.exception.BusinessException;

public class MemberException extends BusinessException {

    private final ErrorCode errorCode;

    public MemberException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.errorCode = errorCode;
    }

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
