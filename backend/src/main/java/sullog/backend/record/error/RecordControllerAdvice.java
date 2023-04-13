package sullog.backend.record.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sullog.backend.member.error.exception.MemberException;
import sullog.backend.record.error.exception.RecordException;

@RestControllerAdvice
public class RecordControllerAdvice {

    @ExceptionHandler(RecordException.class)
    ResponseEntity<String> exceptionHandler(RecordException e) {
        return new ResponseEntity<>(e.getErrorCode().toString(), HttpStatus.BAD_REQUEST);
    }

}
