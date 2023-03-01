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
    ResponseEntity<?> exceptionHandler(RecordException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorCode());
    }

}
