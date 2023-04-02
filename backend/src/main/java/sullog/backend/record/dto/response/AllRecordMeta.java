package sullog.backend.record.dto.response;

import lombok.Builder;

@Builder
public class AllRecordMeta {
    
    private Integer memberId; // 유저 id
    private Integer recordId; // 경험기록 id
    private String description; // 경험기록 상세내용
    private String mainPhotoPath; // 사용자가 등록한 사진 경로(optional)
    private Integer alcoholId; // 전통주 id
    private String alcoholName; // 전통주 이름
    private String productionLocation; // 생산지 명
    private Double productionLatitude; // 전통주 생산지 위도
    private Double productionLongitude; // 전통주 생산지 경도
    private String alcoholTag; // 전통주 태그
    private String brandName; // 브랜드 이름

    public Integer getMemberId() {
        return memberId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public String getDescription() {
        return description;
    }

    public String getMainPhotoPath() {
        return mainPhotoPath;
    }

    public Integer getAlcoholId() {
        return alcoholId;
    }

    public String getAlcoholName() {
        return alcoholName;
    }

    public String getProductionLocation() {
        return productionLocation;
    }

    public Double getProductionLatitude() {
        return productionLatitude;
    }

    public Double getProductionLongitude() {
        return productionLongitude;
    }

    public String getAlcoholTag() {
        return alcoholTag;
    }

    public String getBrandName() {
        return brandName;
    }
}
