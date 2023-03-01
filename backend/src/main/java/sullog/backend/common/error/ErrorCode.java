package sullog.backend.common.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // Common
    UNKNOWN_ERROR(HttpStatus.BAD_REQUEST, "C001", "???"),


    // Member
    MEMBER_UNKNOWN_ERROR(HttpStatus.BAD_REQUEST, "M001", "Member Unknown Error"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M002", "Member Not Found Error"),
    ;

    private final HttpStatus status;

    private final String errorCode;

    private final String message;

    ErrorCode(HttpStatus status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorCode{" +
                "status=" + status +
                ", errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
