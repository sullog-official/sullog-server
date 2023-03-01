package sullog.backend.member.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sullog.backend.member.error.exception.MemberException;

@RestControllerAdvice
public class MemberControllerAdvice {

    @ExceptionHandler(MemberException.class)
    ResponseEntity<?> exceptionHandler(MemberException e) {
        return new ResponseEntity<>(e.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

}
