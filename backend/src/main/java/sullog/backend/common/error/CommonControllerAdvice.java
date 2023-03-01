package sullog.backend.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sullog.backend.common.error.exception.CommonException;
import sullog.backend.member.error.exception.MemberException;

@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(Exception.class)
    ResponseEntity<?> exceptionHandler(Exception e) {

        return new ResponseEntity<>(ErrorCode.UNKNOWN_ERROR, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommonException.class)
    ResponseEntity<?> commonExceptionHandler(CommonException e) {

        return new ResponseEntity<>(e.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

}
