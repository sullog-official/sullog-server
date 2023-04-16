package sullog.backend.common.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sullog.backend.common.error.exception.CommonException;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> exceptionHandler(Exception e) {
        log.error("예외 발생", e);
        return new ResponseEntity<>(getSystemErrorMessage(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommonException.class)
    ResponseEntity<String> commonExceptionHandler(CommonException e) {
        log.error("예외 발생", e);
        return new ResponseEntity<>(e.getErrorCode().toString(), HttpStatus.BAD_REQUEST);
    }

    private String getSystemErrorMessage(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
