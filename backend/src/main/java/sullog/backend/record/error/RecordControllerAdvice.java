package sullog.backend.record.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sullog.backend.common.error.response.ErrorResponse;
import sullog.backend.record.error.exception.ImageUploadException;
import sullog.backend.record.error.exception.RecordException;

@Slf4j
@RestControllerAdvice
public class RecordControllerAdvice {

    @ExceptionHandler(RecordException.class)
    ResponseEntity<ErrorResponse> recordExceptionHandler(RecordException e) {
        log.error("RecordException 예외 발생", e);
        return ErrorResponse.toResponseEntity(e);
    }

    @ExceptionHandler(ImageUploadException.class)
    ResponseEntity<ErrorResponse> imageUploadExceptionHandler(ImageUploadException e) {
        log.error("ImageUploadException 예외 발생", e);
        return ErrorResponse.toResponseEntity(e);
    }

}
