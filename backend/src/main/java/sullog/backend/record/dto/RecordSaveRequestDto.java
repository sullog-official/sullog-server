package sullog.backend.record.dto;

import lombok.Builder;
import sullog.backend.record.entity.AlcoholPercentFeeling;
import sullog.backend.record.entity.FlavorDetail;
import sullog.backend.record.entity.Record;

import java.time.LocalDate;
import java.util.List;

@Builder
public class RecordSaveRequestDto {
    private Integer alcoholId; // 술 id

    private AlcoholPercentFeeling alcoholPercentFeeling; // 도수 느낌

    private List<FlavorDetail> flavorTagList; // 상세 플레이버 태그

    private Double starScore; // 별점(1~5)

    private Double scentScore; // 향점수(1~5)

    private Double tasteScore; // 맛점수 (1~5)

    private Double textureScore; // 감촉점수 (1~5)

    private String description; // 상세 내용

    private LocalDate experienceDate; // 경험 날짜

    public RecordSaveRequestDto() {
    }

    public RecordSaveRequestDto(
            Integer alcoholId,
            AlcoholPercentFeeling alcoholPercentFeeling,
            List<FlavorDetail> flavorTagList,
            Double starScore,
            Double scentScore,
            Double tasteScore,
            Double textureScore,
            String description,
            LocalDate experienceDate) {
        this.alcoholId = alcoholId;
        this.alcoholPercentFeeling = alcoholPercentFeeling;
        this.flavorTagList = flavorTagList;
        this.starScore = starScore;
        this.scentScore = scentScore;
        this.tasteScore = tasteScore;
        this.textureScore = textureScore;
        this.description = description;
        this.experienceDate = experienceDate;
    }

    public int getAlcoholId() {
        return alcoholId;
    }

    public AlcoholPercentFeeling getAlcoholPercentFeeling() {
        return alcoholPercentFeeling;
    }

    public List<FlavorDetail> getFlavorTagList() {
        return flavorTagList;
    }

    public Double getStarScore() {
        return starScore;
    }

    public Double getScentScore() {
        return scentScore;
    }

    public Double getTasteScore() {
        return tasteScore;
    }

    public Double getTextureScore() {
        return textureScore;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getExperienceDate() {
        return experienceDate;
    }

    public Record toEntity(int memberId,
                           List<String> photoPathList) {
        return Record.builder()
                .memberId(memberId)
                .alcoholId(alcoholId)
                .photoPathList(photoPathList)
                .alcoholPercentFeeling(alcoholPercentFeeling)
                .flavorTagList(flavorTagList)
                .starScore(starScore)
                .scentScore(scentScore)
                .tasteScore(tasteScore)
                .textureScore(textureScore)
                .description(description)
                .experienceDate(experienceDate)
                .build();
    }
}
