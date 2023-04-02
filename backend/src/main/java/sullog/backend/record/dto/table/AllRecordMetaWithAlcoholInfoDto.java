package sullog.backend.record.dto.table;

import lombok.Builder;
import lombok.NoArgsConstructor;
import sullog.backend.record.dto.response.AllRecordMeta;

import java.util.List;

@Builder
@NoArgsConstructor
public class AllRecordMetaWithAlcoholInfoDto {

    private Integer memberId;
    private Integer recordId;
    private String description;
    private List<String> photoPathList;
    private Integer alcoholId;
    private String alcoholName;
    private String productionLocation;
    private Double productionLatitude;
    private Double productionLongitude;
    private String alcoholTag;
    private String brandName;

    public Integer getMemberId() {
        return memberId;
    }

    public String getBrandName() {
        return brandName;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPhotoPathList() {
        return photoPathList;
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

    public AllRecordMetaWithAlcoholInfoDto(
            Integer memberId,
            Integer recordId,
            String description,
            List<String> photoPathList,
            Integer alcoholId,
            String alcoholName,
            String productionLocation,
            Double productionLatitude,
            Double productionLongitude,
            String alcoholTag,
            String brandName) {
        this.memberId = memberId;
        this.recordId = recordId;
        this.description = description;
        this.photoPathList = photoPathList;
        this.alcoholId = alcoholId;
        this.alcoholName = alcoholName;
        this.productionLocation = productionLocation;
        this.productionLatitude = productionLatitude;
        this.productionLongitude = productionLongitude;
        this.alcoholTag = alcoholTag;
        this.brandName = brandName;
    }

    public AllRecordMeta toResponseDto() {
        return AllRecordMeta.builder()
                .memberId(memberId)
                .recordId(recordId)
                .description(description)
                .mainPhotoPath(getMainPhotoPath())
                .alcoholId(alcoholId)
                .alcoholName(alcoholName)
                .productionLocation(productionLocation)
                .productionLatitude(productionLatitude)
                .productionLongitude(productionLongitude)
                .alcoholTag(alcoholTag)
                .brandName(brandName)
                .build();
    }

    private String getMainPhotoPath() {
        if(photoPathList.isEmpty()) {
            return "";
        }

        return photoPathList.get(0);
    }
}
