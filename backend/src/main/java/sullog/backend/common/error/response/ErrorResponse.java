package sullog.backend.common.error.response;

import lombok.ToString;
import org.springframework.http.ResponseEntity;
import sullog.backend.common.error.ErrorCode;
import sullog.backend.common.error.exception.BusinessException;

import java.io.PrintWriter;
import java.io.StringWriter;

@ToString
public class ErrorResponse {

    private final String code; // 에러 코드 (ex. C001)

    private final String message; // 에러 메시지

    private final String systemErrorMessage; // e.printStackTrace

    /** 사용자 정의 예외 핸들링 */
    public static ResponseEntity<ErrorResponse> toResponseEntity(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), getSystemErrorMessage(e));

        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    /** 사용자 미정의 예외 핸들링 */
    public static ResponseEntity<ErrorResponse> toResponseEntity(Exception e) {
        ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), getSystemErrorMessage(e));

        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    private ErrorResponse(String code, String message, String systemErrorMessage) {
        this.code = code;
        this.message = message;
        this.systemErrorMessage = systemErrorMessage;
    }

    private static String getSystemErrorMessage(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
