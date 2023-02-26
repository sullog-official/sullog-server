package sullog.backend.alcohol.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sullog.backend.alcohol.error.exception.AlcoholException;

@RestControllerAdvice
public class AlcoholControllerAdvice {

    @ExceptionHandler(AlcoholException.class)
    ResponseEntity<?> exceptionHandler(AlcoholException e) {
        return new ResponseEntity<>(e.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

}
