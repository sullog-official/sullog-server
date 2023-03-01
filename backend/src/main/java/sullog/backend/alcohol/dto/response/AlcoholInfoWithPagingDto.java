package sullog.backend.alcohol.dto.response;

import lombok.Builder;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Builder
@ToString
public class AlcoholInfoWithPagingDto {

    private List<AlcoholInfoDto> alcoholInfoDtoList;

    private PagingInfoDto pagingInfoDto;

    public List<AlcoholInfoDto> getAlcoholInfoDtoList() {
        return alcoholInfoDtoList;
    }

    public PagingInfoDto getPagingInfoDto() {
        return pagingInfoDto;
    }
}
