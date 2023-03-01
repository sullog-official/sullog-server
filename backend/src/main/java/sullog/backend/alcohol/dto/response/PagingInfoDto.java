package sullog.backend.alcohol.dto.response;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class PagingInfoDto {

    private int cursor;

    private int limit;

    public int getCursor() {
        return cursor;
    }

    public int getLimit() {
        return limit;
    }
}
