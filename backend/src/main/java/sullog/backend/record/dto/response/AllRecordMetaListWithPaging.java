package sullog.backend.record.dto.response;

import lombok.Builder;
import sullog.backend.alcohol.dto.response.PagingInfoDto;

import java.util.List;

@Builder
public class AllRecordMetaListWithPaging {
    private List<AllRecordMeta> allRecordMetaList;
    private PagingInfoDto pagingInfo;

    public List<AllRecordMeta> getAllRecordMetaList() {
        return allRecordMetaList;
    }

    public PagingInfoDto getPagingInfo() {
        return pagingInfo;
    }
}
