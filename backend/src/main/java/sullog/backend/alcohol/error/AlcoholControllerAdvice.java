package sullog.backend.alcohol.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sullog.backend.alcohol.error.exception.AlcoholException;
import sullog.backend.common.error.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class AlcoholControllerAdvice {

    @ExceptionHandler(AlcoholException.class)
    ResponseEntity<ErrorResponse> exceptionHandler(AlcoholException e) {
        log.error("AlcoholException 예외 발생", e);
        return ErrorResponse.toResponseEntity(e);
    }

}
