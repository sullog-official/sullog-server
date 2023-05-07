package sullog.backend.common.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sullog.backend.common.error.exception.BusinessException;
import sullog.backend.common.error.response.ErrorResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        log.error("미처리 예외 발생", e);
        return ErrorResponse.toResponseEntity(e);
    }

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ErrorResponse> businessExceptionHandler(BusinessException e) {
        log.error("비즈니스 예외 발생", e);
        return ErrorResponse.toResponseEntity(e);
    }

    private String getSystemErrorMessage(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
