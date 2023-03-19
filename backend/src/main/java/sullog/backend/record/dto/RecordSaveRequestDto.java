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

    private String title; // 제목

    private AlcoholPercentFeeling alcoholPercentFeeling; // 도수 느낌

    private List<FlavorDetail> flavorTagList; // 상세 플레이버 태그

    private Integer scentScore; //향점수(1~5)

    private Integer tasteScore; // 맛점수 (1~5)

    private Integer textureScore; // 감촉점수 (1~5)

    private String description; // 상세 내용

    private LocalDate experienceDate; // 경험 날짜

    public RecordSaveRequestDto() {
    }

    public RecordSaveRequestDto(
            Integer alcoholId,
            String title,
            AlcoholPercentFeeling alcoholPercentFeeling,
            List<FlavorDetail> flavorTagList,
            Integer scentScore,
            Integer tasteScore,
            Integer textureScore,
            String description,
            LocalDate experienceDate) {
        this.alcoholId = alcoholId;
        this.title = title;
        this.alcoholPercentFeeling = alcoholPercentFeeling;
        this.flavorTagList = flavorTagList;
        this.scentScore = scentScore;
        this.tasteScore = tasteScore;
        this.textureScore = textureScore;
        this.description = description;
        this.experienceDate = experienceDate;
    }

    public int getAlcoholId() {
        return alcoholId;
    }

    public String getTitle() {
        return title;
    }

    public AlcoholPercentFeeling getAlcoholPercentFeeling() {
        return alcoholPercentFeeling;
    }

    public List<FlavorDetail> getFlavorTagList() {
        return flavorTagList;
    }

    public Integer getScentScore() {
        return scentScore;
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

    public Record toEntity(int memberId, List<String> photoPathList) {
        return Record.builder()
                .memberId(memberId)
                .alcoholId(alcoholId)
                .title(title)
                .photoPathList(photoPathList)
                .alcoholPercentFeeling(alcoholPercentFeeling)
                .flavorTagList(flavorTagList)
                .scentScore(scentScore)
                .tasteScore(tasteScore)
                .textureScore(textureScore)
                .description(description)
                .experienceDate(experienceDate)
                .build();
    }
}
