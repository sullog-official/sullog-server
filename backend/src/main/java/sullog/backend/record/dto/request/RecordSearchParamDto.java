package sullog.backend.record.dto.request;

import lombok.Builder;

@Builder
public class RecordSearchParamDto {

    private String keyword;
    private Integer cursor;
    private Integer limit;

    public String getKeyword() {
        return keyword;
    }

    public Integer getCursor() {
        return cursor;
    }

    public Integer getLimit() {
        return limit;
    }
}
