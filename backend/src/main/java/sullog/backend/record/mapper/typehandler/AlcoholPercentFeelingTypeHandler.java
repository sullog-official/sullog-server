package sullog.backend.record.mapper.typehandler;

import org.apache.ibatis.type.EnumTypeHandler;
import sullog.backend.record.entity.AlcoholPercentFeeling;


public class AlcoholPercentFeelingTypeHandler extends EnumTypeHandler<AlcoholPercentFeeling> {

    public AlcoholPercentFeelingTypeHandler(Class type) {
        super(type);
    }
}
