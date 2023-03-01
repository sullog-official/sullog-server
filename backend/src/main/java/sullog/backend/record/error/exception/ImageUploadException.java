package sullog.backend.record.error.exception;

public class ImageUploadException extends RuntimeException {

    //    TODO private ErrorCode errorCode;
    public ImageUploadException(String message) {
        super(message); // TODO: 나중에 code로 옮기기
    }

}
