package sullog.backend.record.entity;

import lombok.Builder;
import lombok.ToString;
import sullog.backend.common.entity.BaseEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@ToString(callSuper = true)
public class Record extends BaseEntity {

    private int memberId;

    private int alcoholId;

    private int recordId; //auto increment

    private List<String> photoPathList; // 사진 경로(3장까지)

    private AlcoholPercentFeeling alcoholPercentFeeling; // 도수 느낌

    private List<FlavorDetail> flavorTagList; // 상세 플레이버 태그

    private int starScore; // 별점(1~5)

    private int scentScore; // 향 점수(1~5)

    private int tasteScore; // 맛점수 (1~5)

    private int textureScore; // 감촉점수 (1~5)

    private String description; // 상세 내용

    private LocalDate experienceDate; // 경험 날짜

    public Record() {
        super();
    }

    @Builder
    public Record(
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            int memberId,
            int alcoholId,
            int recordId,
            List<String> photoPathList,
            AlcoholPercentFeeling alcoholPercentFeeling,
            List<FlavorDetail> flavorTagList,
            int starScore,
            int scentScore,
            int tasteScore,
            int textureScore,
            String description,
            LocalDate experienceDate) {
        super(createdAt, updatedAt, deletedAt);
        this.memberId = memberId;
        this.alcoholId = alcoholId;
        this.recordId = recordId;
        this.photoPathList = photoPathList;
        this.alcoholPercentFeeling = alcoholPercentFeeling;
        this.flavorTagList = flavorTagList;
        this.starScore = starScore;
        this.scentScore = scentScore;
        this.tasteScore = tasteScore;
        this.textureScore = textureScore;
        this.description = description;
        this.experienceDate = experienceDate;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getAlcoholId() {
        return alcoholId;
    }

    public int getRecordId() {
        return recordId;
    }

    public int getStarScore() {
        return starScore;
    }

    public List<String> getPhotoPathList() {
        return photoPathList;
    }

    public AlcoholPercentFeeling getAlcoholPercentFeeling() {
        return alcoholPercentFeeling;
    }

    public List<FlavorDetail> getFlavorTagList() {
        return flavorTagList;
    }

    public int getScentScore() {
        return scentScore;
    }

    public int getTasteScore() {
        return tasteScore;
    }

    public int getTextureScore() {
        return textureScore;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getExperienceDate() {
        return experienceDate;
    }
}
