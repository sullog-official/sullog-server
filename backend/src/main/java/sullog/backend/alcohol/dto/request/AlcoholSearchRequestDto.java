package sullog.backend.alcohol.dto.request;

import lombok.Builder;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Builder
@ToString
public class AlcoholSearchRequestDto {

    private String keyword;

    private int cursor;

    private int limit;

    public String getKeyword() {
        return keyword;
    }

    public int getCursor() {
        return cursor;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlcoholSearchRequestDto that = (AlcoholSearchRequestDto) o;
        return cursor == that.cursor && limit == that.limit && keyword.equals(that.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword, cursor, limit);
    }
}
