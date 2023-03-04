package sullog.backend.record.dto.response;

import lombok.Builder;

@Builder
public class RecordMetaDto {

    private int recordId; // 경험기록 id
    private String description; // 경험기록 상세내용
    private String mainPhotoPath; // 사용자가 등록한 사진 경로(optional)
    private int alcoholId; // 전통주 id
    private double productionLatitude; // 전통주 생산지 위도
    private double productionLongitude; // 전통주 생산지 경도
    private String alcoholTag; // 전통주 태그

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

    public double getProductionLatitude() {
        return productionLatitude;
    }

    public double getProductionLongitude() {
        return productionLongitude;
    }

    public String getAlcoholTag() {
        return alcoholTag;
    }
}
