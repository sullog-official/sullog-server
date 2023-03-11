package sullog.backend.record.dto.response;

import lombok.Builder;
import sullog.backend.alcohol.dto.response.AlcoholInfoDto;
import sullog.backend.record.entity.Record;

@Builder
public class RecordDetailDto {

    private Record record;
    private AlcoholInfoDto alcoholInfo;

    public Record getRecord() {
        return record;
    }

    public AlcoholInfoDto getAlcoholInfo() {
        return alcoholInfo;
    }

}
