package sullog.backend.record.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sullog.backend.alcohol.dto.response.AlcoholInfoDto;
import sullog.backend.alcohol.dto.response.PagingInfoDto;
import sullog.backend.alcohol.service.AlcoholService;
import sullog.backend.auth.service.TokenService;
import sullog.backend.record.dto.RecordSaveRequestDto;
import sullog.backend.record.dto.request.RecordSearchParamDto;
import sullog.backend.record.dto.response.*;
import sullog.backend.record.dto.table.AllRecordMetaWithAlcoholInfoDto;
import sullog.backend.record.dto.table.RecordMetaWithAlcoholInfoDto;
import sullog.backend.record.entity.Record;
import sullog.backend.record.service.ImageUploadService;
import sullog.backend.record.service.RecordService;
import sullog.backend.record.service.RecordStatisticService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;
    private final ImageUploadService imageUploadService;
    private final AlcoholService alcoholService;
    private final RecordStatisticService recordStatisticService;

    public RecordController(RecordService recordService, ImageUploadService imageUploadService, AlcoholService alcoholService, RecordStatisticService recordStatisticService) {
        this.recordService = recordService;
        this.imageUploadService = imageUploadService;
        this.alcoholService = alcoholService;
        this.recordStatisticService = recordStatisticService;
    }

    @PostMapping
    public ResponseEntity<Void> saveRecord(@RequestAttribute Integer memberId,
                        @RequestPart(required = false) List<MultipartFile> photoList,
                        @RequestPart("recordInfo") RecordSaveRequestDto requestDto) {
        // 이미지 저장
        List<String> photoPathList = imageUploadService.uploadImageList(photoList);

        // 경험기록 저장
        recordService.saveRecord(requestDto.toEntity(memberId, photoPathList));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<List<RecordMetaDto>> getRecords(@RequestAttribute Integer memberId) {
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

    @GetMapping("/me/search")
    public ResponseEntity<RecordMetaListWithPagingDto> searchRecords(@RequestAttribute Integer memberId,
                                                                     RecordSearchParamDto recordSearchParamDto) {
       List<RecordMetaWithAlcoholInfoDto> recordMetaWithAlcoholInfoList = recordService.getRecordMetasByCondition(memberId, recordSearchParamDto);

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

    @GetMapping
    public ResponseEntity<AllRecordMetaListWithPaging> getRecordFeed(
            @RequestParam(required = false) Integer cursor,
            @RequestParam Integer limit) {
        List<AllRecordMetaWithAlcoholInfoDto> allRecordMetaWithAlcoholInfoList = recordService.getRecordFeed(cursor, limit);
        int newCursor = 0;
        if (allRecordMetaWithAlcoholInfoList.size() > 0) {
            newCursor = allRecordMetaWithAlcoholInfoList.get(allRecordMetaWithAlcoholInfoList.size() - 1).getRecordId();
        }
        AllRecordMetaListWithPaging allRecordMetaListWithPaging = AllRecordMetaListWithPaging.builder()
                .allRecordMetaList(allRecordMetaWithAlcoholInfoList.stream()
                        .map(AllRecordMetaWithAlcoholInfoDto::toResponseDto)
                        .collect(Collectors.toList()))
                .pagingInfo(PagingInfoDto.builder()
                        .cursor(newCursor)
                        .limit(limit)
                        .build())
                .build();
        return new ResponseEntity<>(allRecordMetaListWithPaging, HttpStatus.OK);
    }

    @GetMapping("/me/statistics")
    public ResponseEntity<Map<String, Integer>> getMyRecordStatistics(@RequestAttribute Integer memberId) {
        Map<String, Integer> recordStatisticMap = recordStatisticService.getRecordStatistics(memberId);
        return new ResponseEntity<>(recordStatisticMap, HttpStatus.OK);
    }
}
