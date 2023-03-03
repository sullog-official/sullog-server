package sullog.backend.member.dto.response;

import lombok.Builder;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Builder
@ToString
public class RecentSearchHistoryDto {

    private List<String> recentSearchWordList;

    public List<String> getRecentSearchWordList() {
        return recentSearchWordList;
    }
}
