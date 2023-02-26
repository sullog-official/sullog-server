package sullog.backend.record.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import sullog.backend.alcohol.entity.Alcohol;
import sullog.backend.common.entity.BaseEntity;
import sullog.backend.member.entity.Member;

import java.time.Instant;

@ToString(callSuper = true)
public class Record extends BaseEntity {

    private double recordId;

    private String title;

    private String photoPath;

    private String alcoholPercentFeeling;

    private String flavorTagList;

    private int tasteScore;

    private int textureScore;

    private String description;

    private Member member;

    private Alcohol alcohol;

    private String imagePath1;

    private String imagePath2;

    private String imagePath3;

    @Builder
    public Record(
            double recordId,
            String title,
            String photoPath,
            String alcoholPercentFeeling,
            String flavorTagList,
            int tasteScore,
            int textureScore,
            String description,
            Member member,
            Alcohol alcohol,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            String imagePath1,
            String imagePath2,
            String imagePath3) {
        super(createdAt, updatedAt, deletedAt);
        this.recordId = recordId;
        this.title = title;
        this.photoPath = photoPath;
        this.alcoholPercentFeeling = alcoholPercentFeeling;
        this.flavorTagList = flavorTagList;
        this.tasteScore = tasteScore;
        this.textureScore = textureScore;
        this.description = description;
        this.member = member;
        this.alcohol = alcohol;
        this.imagePath1 = imagePath1;
        this.imagePath2 = imagePath2;
        this.imagePath3 = imagePath3;
    }

    public double getRecordId() {
        return recordId;
    }

    public String getTitle() {
        return title;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getAlcoholPercentFeeling() {
        return alcoholPercentFeeling;
    }

    public String getFlavorTagList() {
        return flavorTagList;
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

    public Member getMember() {
        return member;
    }

    public Alcohol getAlcohol() {
        return alcohol;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public String getImagePath3() {
        return imagePath3;
    }
}
