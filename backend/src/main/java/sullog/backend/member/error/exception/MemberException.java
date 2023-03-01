package sullog.backend.member.error.exception;

import sullog.backend.common.error.ErrorCode;

public class MemberException extends RuntimeException {

    private final ErrorCode errorCode;

    public MemberException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public MemberException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
