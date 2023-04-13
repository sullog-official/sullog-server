package sullog.backend.alcohol.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sullog.backend.alcohol.error.exception.AlcoholException;
import sullog.backend.common.error.ErrorCode;

@RestControllerAdvice
public class AlcoholControllerAdvice {

    @ExceptionHandler(AlcoholException.class)
    ResponseEntity<String> exceptionHandler(AlcoholException e) {
        return new ResponseEntity<>(e.getErrorCode().toString(), HttpStatus.BAD_REQUEST);
    }

}
