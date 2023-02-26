package sullog.backend.alcohol.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import sullog.backend.common.entity.BaseEntity;

import java.time.Instant;

@ToString(callSuper = true)
public class Alcohol extends BaseEntity {

    private int alcoholId;

    private String name;

    private double alcoholPercent;

    private String productionLocation;

    private double productionLatitude;

    private double productionLongitude;

    private String alcoholTag;

    private AlcoholBrand alcoholBrand;

    @Builder
    public Alcohol(
            int alcoholId,
            String name,
            double alcoholPercent,
            String productionLocation,
            double productionLatitude,
            double productionLongitude,
            String alcoholTag,
            AlcoholBrand alcoholBrand,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.alcoholId = alcoholId;
        this.name = name;
        this.alcoholPercent = alcoholPercent;
        this.productionLocation = productionLocation;
        this.productionLatitude = productionLatitude;
        this.productionLongitude = productionLongitude;
        this.alcoholTag = alcoholTag;
        this.alcoholBrand = alcoholBrand;
    }

}
