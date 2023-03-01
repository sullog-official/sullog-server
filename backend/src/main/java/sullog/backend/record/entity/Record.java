package sullog.backend.record.entity;

import lombok.Builder;
import lombok.ToString;
import sullog.backend.common.entity.BaseEntity;

import java.time.Instant;
import java.util.List;

@ToString(callSuper = true)
public class Record extends BaseEntity {

    private int memberId;
    private int alcoholId;

    private int recordId; //auto increment

    private String title; // 제목

    private List<String> photoPathList; // 사진 경로(3장까지)

    private AlcoholIntensity alcoholPercentFeeling; // 도수 느낌

    private List<FlavorDetail> flavorTagList; // 상세 플레이버 태그

    private int scentScore; // 맛점수 (1~5)

    private int tasteScore; // 감촉점수 (1~5)

    private int textureScore; // 상세 내용

    private String description; // 경험 날짜

    @Builder
    public Record(
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            int recordId,
            String title,
            List<String> photoPathList,
            AlcoholIntensity alcoholPercentFeeling,
            List<FlavorDetail> flavorTagList,
            int scentScore,
            int tasteScore,
            int textureScore,
            String description,
            int memberId,
            int alcoholId) {
        super(createdAt, updatedAt, deletedAt);
        this.recordId = recordId;
        this.title = title;
        this.photoPathList = photoPathList;
        this.alcoholPercentFeeling = alcoholPercentFeeling;
        this.flavorTagList = flavorTagList;
        this.scentScore = scentScore;
        this.tasteScore = tasteScore;
        this.textureScore = textureScore;
        this.description = description;
        this.memberId = memberId;
        this.alcoholId = alcoholId;
    }

    public int getRecordId() {
        return recordId;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getPhotoPathList() {
        return photoPathList;
    }

    public AlcoholIntensity getAlcoholPercentFeeling() {
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

    public int getMemberId() {
        return memberId;
    }

    public int getAlcoholId() {
        return alcoholId;
    }
}
