package sullog.backend.record.dto.response;

import lombok.Builder;

@Builder
public class RecordSaveDto {

    private Integer recordId; // 경험기록 id

    public Integer getRecordId() {
        return recordId;
    }
}
