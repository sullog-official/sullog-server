package sullog.backend.alcohol.dto.response;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class AlcoholInfoDto {

    private String alcoholName;

    private String brandName;

    private String alcoholType;

    private double alcoholPercent;

    private String productionLocation;

    private double productionLatitude;

    private double productionLongitude;

    private String alcoholTag;

    public String getAlcoholName() {
        return alcoholName;
    }

    public String getBrandName() {
        return brandName;
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


}
