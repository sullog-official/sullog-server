package sullog.backend.record.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sullog.backend.record.dto.RecordSaveRequestDto;
import sullog.backend.record.service.ImageUploadService;
import sullog.backend.record.service.RecordService;

import java.util.List;

@RestController
public class RecordController {

    private final RecordService recordService;
    private final ImageUploadService imageUploadService;

    public RecordController(RecordService recordService, ImageUploadService imageUploadService) {
        this.recordService = recordService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/records")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRecord(
                        @RequestPart(required = false) List<MultipartFile> photoList,
                        @RequestPart("recordInfo") RecordSaveRequestDto requestDto) {
        // 이미지 저장
        List<String> photoPathList = imageUploadService.uploadImageList(photoList);

        // 경험기록 저장
        recordService.saveRecord(requestDto.toEntity(photoPathList));
    }
}
