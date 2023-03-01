package sullog.backend.alcohol.entity;

import lombok.Builder;
import lombok.ToString;
import sullog.backend.common.entity.BaseEntity;

import java.time.Instant;

@ToString(callSuper = true)
public class AlcoholBrand extends BaseEntity {

    private int brandId;

    private String name;

    @Builder
    public AlcoholBrand(Instant createdAt,
                        Instant updatedAt,
                        Instant deletedAt,
                        int brandId,
                        String name) {
        super(createdAt, updatedAt, deletedAt);
        this.brandId = brandId;
        this.name = name;
    }

    public int getBrandId() {
        return brandId;
    }

    public String getName() {
        return name;
    }

}
