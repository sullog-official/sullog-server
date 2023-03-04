package sullog.backend.record.dto.table;

import lombok.Builder;
import sullog.backend.record.dto.response.RecordMetaDto;

import java.util.List;

@Builder
public class RecordMetaWithAlcoholInfoDto {

    private int recordId;
    private String description;
    private List<String> photoPathList;
    private int alcoholId;
    private double productionLatitude;
    private double productionLongitude;
    private String alcoholTag;

    public int getRecordId() {
        return recordId;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPhotoPathList() {
        return photoPathList;
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

    public RecordMetaDto toResponseDto() {
        return RecordMetaDto.builder()
                .recordId(recordId)
                .description(description)
                .mainPhotoPath(getMainPhotoPath())
                .alcoholId(alcoholId)
                .productionLatitude(productionLatitude)
                .productionLongitude(productionLongitude)
                .alcoholTag(alcoholTag)
                .build();
    }

    private String getMainPhotoPath() {
        if(photoPathList.isEmpty()) {
            return "";
        }

        return photoPathList.get(0);
    }
}
