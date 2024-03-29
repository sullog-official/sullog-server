package sullog.backend.alcohol.entity;

import lombok.Builder;
import lombok.ToString;
import sullog.backend.common.entity.BaseEntity;

import java.time.Instant;

@ToString(callSuper = true)
public class Alcohol extends BaseEntity {

    private int alcoholId;

    private int brandId;

    private String name;

    private String alcoholType;

    private double alcoholPercent;

    private String productionLocation;

    private double productionLatitude;

    private double productionLongitude;

    private String alcoholTag;

    public int getAlcoholId() {
        return alcoholId;
    }

    public String getName() {
        return name;
    }

    public String getAlcoholType() {
        return alcoholType;
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

    public int getBrandId() {
        return brandId;
    }

    @Builder
    public Alcohol(
            int alcoholId,
            int brandId,
            String name,
            String alcoholType,
            double alcoholPercent,
            String productionLocation,
            double productionLatitude,
            double productionLongitude,
            String alcoholTag,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.alcoholId = alcoholId;
        this.brandId = brandId;
        this.name = name;
        this.alcoholType = alcoholType;
        this.alcoholPercent = alcoholPercent;
        this.productionLocation = productionLocation;
        this.productionLatitude = productionLatitude;
        this.productionLongitude = productionLongitude;
        this.alcoholTag = alcoholTag;
    }

}
