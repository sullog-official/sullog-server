package sullog.backend.member.dto.request;

import lombok.Builder;

@Builder
public class SearchKeywordDto {

    private String keyword;

    public SearchKeywordDto() {
    }

    public SearchKeywordDto(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
