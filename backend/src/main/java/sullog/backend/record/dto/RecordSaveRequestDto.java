package sullog.backend.record.dto;

import lombok.Builder;
import sullog.backend.record.entity.AlcoholIntensity;
import sullog.backend.record.entity.FlavorDetail;
import sullog.backend.record.entity.Record;

import java.time.LocalDate;
import java.util.List;

@Builder
public class RecordSaveRequestDto {

    private Long memberId; // 작성자 id

    private Long alcoholId; // 술 id

    private String title; // 제목

    private AlcoholIntensity alcoholIntensity; // 도수 느낌
    private List<FlavorDetail> flavorTagList; // 상세 플레이버 태그
    private Integer tasteScore; // 맛점수 (1~5)
    private Integer textureScore; // 감촉점수 (1~5)
    private String description; // 상세 내용
    private LocalDate experienceDate; // 경험 날짜

    public RecordSaveRequestDto() {
    }

    public RecordSaveRequestDto(Long memberId, Long alcoholId, String title, AlcoholIntensity alcoholIntensity, List<FlavorDetail> flavorTagList, Integer tasteScore, Integer textureScore, String description, LocalDate experienceDate) {
        this.memberId = memberId;
        this.alcoholId = alcoholId;
        this.title = title;
        this.alcoholIntensity = alcoholIntensity;
        this.flavorTagList = flavorTagList;
        this.tasteScore = tasteScore;
        this.textureScore = textureScore;
        this.description = description;
        this.experienceDate = experienceDate;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getAlcoholId() {
        return alcoholId;
    }

    public String getTitle() {
        return title;
    }

    public AlcoholIntensity getAlcoholIntensity() {
        return alcoholIntensity;
    }

    public List<FlavorDetail> getFlavorTagList() {
        return flavorTagList;
    }

    public Integer getTasteScore() {
        return tasteScore;
    }

    public Integer getTextureScore() {
        return textureScore;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getExperienceDate() {
        return experienceDate;
    }

    public Record toEntity(List<String> photoPathList) {
        return Record.builder()
                .memberId(memberId)
                .alcoholId(alcoholId)
                .title(title)
                .photoPathList(photoPathList)
                .alcoholIntensity(alcoholIntensity)
                .flavorTagList(flavorTagList)
                .tasteScore(tasteScore)
                .textureScore(textureScore)
                .description(description)
                .experienceDate(experienceDate)
                .build();
    }
}
