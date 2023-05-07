package sullog.backend.record.service;

import org.springframework.stereotype.Service;
import sullog.backend.record.dto.request.RecordSearchParamDto;
import sullog.backend.record.dto.table.AllRecordMetaWithAlcoholInfoDto;
import sullog.backend.record.dto.table.RecordMetaWithAlcoholInfoDto;
import sullog.backend.record.entity.Record;
import sullog.backend.record.mapper.RecordMapper;

import java.util.List;

@Service
public class RecordService {

    private final RecordMapper recordMapper;

    public RecordService(RecordMapper recordMapper) {
        this.recordMapper = recordMapper;
    }

    public Integer saveRecord(Record record) {
        recordMapper.insertRecord(record);
        return record.getRecordId();
    }

    public List<RecordMetaWithAlcoholInfoDto> getRecordMetasByMemberId(int memberId) {
        List<RecordMetaWithAlcoholInfoDto> recordMetaWithAlcoholInfoDtos = recordMapper.selectRecordMetaByMemberId(memberId);
        return recordMetaWithAlcoholInfoDtos;
    }

    public Record getRecordByRecordId(int recordId) {
        return recordMapper.selectRecordById(recordId);
    }

    public List<RecordMetaWithAlcoholInfoDto> getRecordMetasByCondition(int memberId, RecordSearchParamDto recordSearchParamDto) {
        List<RecordMetaWithAlcoholInfoDto> recordMetaWithAlcoholInfoDtos = recordMapper.selectRecordMetaByCondition(memberId, recordSearchParamDto);
        return recordMetaWithAlcoholInfoDtos;
    }

    public List<AllRecordMetaWithAlcoholInfoDto> getRecordFeed(Integer cursor, Integer limit) {
        RecordSearchParamDto recordSearchParamDto = RecordSearchParamDto.builder()
                .cursor(cursor)
                .keyword("")
                .limit(limit)
                .build();
        return recordMapper.selectAllRecordMetaByPaging(recordSearchParamDto);
    }
}
