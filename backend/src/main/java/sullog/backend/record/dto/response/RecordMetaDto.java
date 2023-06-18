package sullog.backend.record.dto.response;

import lombok.Builder;

@Builder
public class RecordMetaDto {

    private int recordId; // 경험기록 id
    private String description; // 경험기록 상세내용
    private String mainPhotoPath; // 사용자가 등록한 사진 경로(optional)
    private int alcoholId; // 전통주 id
    private String alcoholName; // 전통주 이름
    private String productionLocation; // 생산지 명
    private double productionLatitude; // 전통주 생산지 위도
    private double productionLongitude; // 전통주 생산지 경도
    private String alcoholType; // 주종
    private String brandName; // 브랜드 이름

    public int getRecordId() {
        return recordId;
    }

    public String getDescription() {
        return description;
    }

    public String getMainPhotoPath() {
        return mainPhotoPath;
    }

    public int getAlcoholId() {
        return alcoholId;
    }

    public String getAlcoholName() {
        return alcoholName;
    }

    public String getProductionLocation() {
        return productionLocation;
    }

    public double getProductionLatitude() {
        return productionLatitude;
    }

    public double getProductionLongitude() {
        return productionLongitude;
    }

    public String getAlcoholType() {
        return alcoholType;
    }

    public String getBrandName() {
        return brandName;
    }
}
