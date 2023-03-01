package sullog.backend.record.mapper.typeHandler;

import org.apache.ibatis.type.EnumTypeHandler;
import sullog.backend.record.entity.AlcoholIntensity;


public class AlcoholIntensityTypeHandler extends EnumTypeHandler<AlcoholIntensity> {

    public AlcoholIntensityTypeHandler(Class type) {
        super(type);
    }
}
