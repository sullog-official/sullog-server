package sullog.backend.record.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sullog.backend.record.dto.request.RecordSearchParamDto;
import sullog.backend.record.dto.table.AllRecordMetaWithAlcoholInfoDto;
import sullog.backend.record.dto.table.RecordMetaWithAlcoholInfoDto;
import sullog.backend.record.entity.Record;

import java.util.List;

@Mapper
public interface RecordMapper {

    void insertRecord(Record record);

    List<RecordMetaWithAlcoholInfoDto> selectRecordMetaByMemberId(int memberId);

    Record selectRecordById(int recordId);

    List<RecordMetaWithAlcoholInfoDto> selectRecordMetaByCondition(@Param("memberId") int memberId,
                                                                   @Param("recordSearchParamDto") RecordSearchParamDto recordSearchParamDto);

    List<AllRecordMetaWithAlcoholInfoDto> selectAllRecordMetaByPaging(@Param("recordSearchParamDto") RecordSearchParamDto recordSearchParamDto);

    List<String> selectAlcoholTypeList(@Param("member_id") Integer memberId);
}
