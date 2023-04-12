package sullog.backend.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sullog.backend.common.error.exception.CommonException;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> exceptionHandler(Exception e) {

        e.printStackTrace();

        return new ResponseEntity<>(getSystemErrorMessage(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommonException.class)
    ResponseEntity<String> commonExceptionHandler(CommonException e) {

        e.printStackTrace();

        return new ResponseEntity<>(e.getErrorCode().toString(), HttpStatus.BAD_REQUEST);
    }

    private String getSystemErrorMessage(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
