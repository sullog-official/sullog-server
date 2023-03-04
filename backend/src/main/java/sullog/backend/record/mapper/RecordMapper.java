package sullog.backend.record.mapper;

import org.apache.ibatis.annotations.Mapper;
import sullog.backend.record.dto.request.RecordSearchParamDto;
import sullog.backend.record.dto.table.RecordMetaWithAlcoholInfoDto;
import sullog.backend.record.entity.Record;

import java.util.List;

@Mapper
public interface RecordMapper {

    void insertRecord(Record record);

    List<RecordMetaWithAlcoholInfoDto> selectRecordMetaByMemberId(int memberId);

    Record selectRecordById(int recordId);

    List<RecordMetaWithAlcoholInfoDto> selectRecordMetaByCondition(RecordSearchParamDto recordSearchParamDto);
}
