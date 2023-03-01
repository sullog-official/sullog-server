package sullog.backend.record.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploadService {

    List<String> uploadImageList(List<MultipartFile> multipartFileList);
}
