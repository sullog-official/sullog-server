package sullog.backend.record.dto.response;

import lombok.Builder;
import sullog.backend.alcohol.dto.response.PagingInfoDto;

import java.util.List;

@Builder
public class RecordMetaListWithPagingDto {
    private List<RecordMetaDto> recordMetaList;
    private PagingInfoDto pagingInfo;

    public List<RecordMetaDto> getRecordMetaList() {
        return recordMetaList;
    }

    public PagingInfoDto getPagingInfo() {
        return pagingInfo;
    }
}
