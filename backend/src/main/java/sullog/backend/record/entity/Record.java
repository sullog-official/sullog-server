package sullog.backend.record.entity;

import lombok.Builder;
import lombok.ToString;
import sullog.backend.common.entity.BaseEntity;

import java.time.Instant;

@ToString(callSuper = true)
public class Record extends BaseEntity {

    private int recordId;

    private String title;

    private String photoPathList;

    private String alcoholPercentFeeling;

    private String flavorTagList;

    private int scentScore;

    private int tasteScore;

    private int textureScore;

    private String description;

    private int memberId;

    private int alcoholId;

    @Builder
    public Record(
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            int recordId,
            String title,
            String photoPathList,
            String alcoholPercentFeeling,
            String flavorTagList,
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

    public String getPhotoPathList() {
        return photoPathList;
    }

    public String getAlcoholPercentFeeling() {
        return alcoholPercentFeeling;
    }

    public String getFlavorTagList() {
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
