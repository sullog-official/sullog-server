package sullog.backend.record.entity;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Builder
public class Record {

    private Long memberId;
    private Long alcoholId;

    private Long recordId; //auto increment
    private String title; // 제목
    private List<String> photoPathList; // 사진 경로(3장까지)
    private AlcoholIntensity alcoholIntensity; // 도수 느낌
    private List<FlavorDetail> flavorTagList; // 상세 플레이버 태그
    private Integer tasteScore; // 맛점수 (1~5)
    private Integer textureScore; // 감촉점수 (1~5)
    private String description; // 상세 내용
    private LocalDate experienceDate; // 경험 날짜

    //TODO baseEntity로 분리
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
