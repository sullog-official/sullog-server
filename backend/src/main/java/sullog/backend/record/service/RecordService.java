package sullog.backend.record.service;

import org.springframework.stereotype.Service;
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

    public void saveRecord(Record record) {
        recordMapper.insertRecord(record);
    }

    public List<RecordMetaWithAlcoholInfoDto> getRecordMetasByMemberId(int memberId) {
        List<RecordMetaWithAlcoholInfoDto> recordMetaWithAlcoholInfoDtos = recordMapper.selectRecordMetaByMemberId(memberId);
        return recordMetaWithAlcoholInfoDtos;
    }
}
