package sullog.backend.common.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // Common
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C001", "Unknown Error"),


    // Member
    MEMBER_UNKNOWN_ERROR(HttpStatus.BAD_REQUEST, "M001", "Member Unknown Error"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M002", "Member Not Found Error"),
    ;

    private final HttpStatus status;

    private final String code;

    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorCode{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
