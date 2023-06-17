package sullog.backend.record.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import sullog.backend.record.error.exception.RecordException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ImageUploadServiceAwsImplTest {

    @InjectMocks
    private ImageUploadServiceAwsImpl imageUploadService;

    private List<String> allowedExtensions = List.of("jpg", "JPG", "jpeg", "JPEG", "png", "PNG", "heic", "HEIC");

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(imageUploadService, "allowedExtensions", allowedExtensions);
    }

    @Test
    void 허용된_이미지포맷이_아니면_예외가_발생한다() {
        MultipartFile invalidFormatImage = createTestFile("test1", "invalid");
        assertThrows(RecordException.class, () -> {
            imageUploadService.uploadImageList(List.of(invalidFormatImage));
        });
    }

    MultipartFile createTestFile(String originalFileName, String contentType) {
        return new MockMultipartFile("test", originalFileName+"."+contentType, contentType, "test".getBytes());
    }

}