package sullog.backend.member.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sullog.backend.common.error.response.ErrorResponse;
import sullog.backend.member.error.exception.MemberException;

@Slf4j
@RestControllerAdvice
public class MemberControllerAdvice {

    @ExceptionHandler(MemberException.class)
    ResponseEntity<ErrorResponse> exceptionHandler(MemberException e) {
        log.error("MemberException 발생", e);
        return ErrorResponse.toResponseEntity(e);
    }

}
