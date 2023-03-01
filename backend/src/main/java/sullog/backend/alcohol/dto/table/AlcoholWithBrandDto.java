package sullog.backend.alcohol.dto.table;

import lombok.Builder;
import lombok.ToString;
import sullog.backend.alcohol.dto.response.AlcoholInfoDto;

import java.time.Instant;

@Builder
@ToString
public class AlcoholWithBrandDto {

    private int alcoholId;

    private String alcoholName;

    private double alcoholPercent;

    private String productionLocation;

    private double productionLatitude;

    private double productionLongitude;

    private String alcoholTag;

    private Instant alcoholCreatedAt;

    private Instant alcoholUpdatedAt;

    private Instant alcoholDeletedAt;

    private int brandId;

    private String brandName;

    private Instant brandCreatedAt;

    private Instant brandUpdatedAt;

    private Instant brandDeletedAt;

    public int getAlcoholId() {
        return alcoholId;
    }

    public String getAlcoholName() {
        return alcoholName;
    }

    public double getAlcoholPercent() {
        return alcoholPercent;
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

    public String getAlcoholTag() {
        return alcoholTag;
    }

    public Instant getAlcoholCreatedAt() {
        return alcoholCreatedAt;
    }

    public Instant getAlcoholUpdatedAt() {
        return alcoholUpdatedAt;
    }

    public Instant getAlcoholDeletedAt() {
        return alcoholDeletedAt;
    }

    public int getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public Instant getBrandCreatedAt() {
        return brandCreatedAt;
    }

    public Instant getBrandUpdatedAt() {
        return brandUpdatedAt;
    }

    public Instant getBrandDeletedAt() {
        return brandDeletedAt;
    }

    public AlcoholInfoDto toAlcoholInfoDto() {
        return AlcoholInfoDto.builder()
                .alcoholName(this.getAlcoholName())
                .alcoholPercent(this.getAlcoholPercent())
                .alcoholTag(this.getAlcoholTag())
                .brandName(this.getBrandName())
                .productionLatitude(this.getProductionLatitude())
                .productionLongitude(this.getProductionLongitude())
                .productionLocation(this.getProductionLocation())
                .alcoholPercent(this.getAlcoholPercent())
                .build();
    }
}
