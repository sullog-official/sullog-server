package sullog.backend.record.mapper;

import org.apache.ibatis.annotations.Mapper;
import sullog.backend.record.entity.Record;

@Mapper
public interface RecordMapper {

    void insertRecord(Record record);
}
