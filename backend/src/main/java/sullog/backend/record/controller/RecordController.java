package sullog.backend.record.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sullog.backend.alcohol.dto.response.AlcoholInfoDto;
import sullog.backend.alcohol.dto.response.PagingInfoDto;
import sullog.backend.alcohol.service.AlcoholService;
import sullog.backend.record.dto.RecordSaveRequestDto;
import sullog.backend.record.dto.request.RecordSearchParamDto;
import sullog.backend.record.dto.response.RecordMetaDto;
import sullog.backend.record.dto.response.RecordDetailDto;
import sullog.backend.record.dto.response.RecordMetaListWithPagingDto;
import sullog.backend.record.dto.table.RecordMetaWithAlcoholInfoDto;
import sullog.backend.record.entity.Record;
import sullog.backend.record.service.ImageUploadService;
import sullog.backend.record.service.RecordService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;
    private final ImageUploadService imageUploadService;
    private final AlcoholService alcoholService;

    public RecordController(RecordService recordService, ImageUploadService imageUploadService, AlcoholService alcoholService) {
        this.recordService = recordService;
        this.imageUploadService = imageUploadService;
        this.alcoholService = alcoholService;
    }

    @PostMapping
    public ResponseEntity<Void> saveRecord(
                        @RequestPart(required = false) List<MultipartFile> photoList,
                        @RequestPart("recordInfo") RecordSaveRequestDto requestDto) {
        // 이미지 저장
        List<String> photoPathList = imageUploadService.uploadImageList(photoList);

        // 경험기록 저장
        recordService.saveRecord(requestDto.toEntity(photoPathList));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecordMetaDto>> getRecords(@RequestParam int memberId) {
        List<RecordMetaDto> recordMetaDtoList = recordService.getRecordMetasByMemberId(memberId).stream()
                .map(RecordMetaWithAlcoholInfoDto::toResponseDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(recordMetaDtoList, HttpStatus.OK);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<RecordDetailDto> getRecord(@PathVariable int recordId) {
        // 경험기록 조회
        Record record = recordService.getRecordByRecordId(recordId);

        // 주류 정보 조회
        AlcoholInfoDto alcoholWithBrand = alcoholService.getAlcoholById(record.getAlcoholId());

        RecordDetailDto recordDetailDto = RecordDetailDto.builder()
                .record(record)
                .alcoholInfo(alcoholWithBrand)
                .build();

        return new ResponseEntity<>(recordDetailDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<RecordMetaListWithPagingDto> searchRecords(RecordSearchParamDto recordSearchParamDto) {
        List<RecordMetaWithAlcoholInfoDto> recordMetaWithAlcoholInfoList = recordService.getRecordMetasByCondition(recordSearchParamDto);

        RecordMetaListWithPagingDto recordMetaListWithPagingDto = RecordMetaListWithPagingDto.builder()
                .recordMetaList(recordMetaWithAlcoholInfoList.stream()
                        .map(RecordMetaWithAlcoholInfoDto::toResponseDto)
                        .collect(Collectors.toList())
                )
                .pagingInfo(PagingInfoDto.builder()
                        .cursor(recordMetaWithAlcoholInfoList.get(recordMetaWithAlcoholInfoList.size() - 1).getRecordId())
                        .limit(recordSearchParamDto.getLimit())
                        .build()
                )
                .build();

        return new ResponseEntity<>(recordMetaListWithPagingDto, HttpStatus.OK);
    }
}
