package sullog.backend.alcohol.service;

import org.springframework.stereotype.Service;
import sullog.backend.alcohol.dto.request.AlcoholSearchRequestDto;
import sullog.backend.alcohol.dto.response.AlcoholInfoDto;
import sullog.backend.alcohol.dto.response.AlcoholInfoWithPagingDto;
import sullog.backend.alcohol.dto.response.PagingInfoDto;
import sullog.backend.alcohol.dto.table.AlcoholWithBrandDto;
import sullog.backend.alcohol.mapper.AlcoholMapper;
import sullog.backend.member.config.jwt.JwtAuthFilter;
import sullog.backend.member.dto.response.RecentSearchHistoryDto;
import sullog.backend.member.entity.Member;
import sullog.backend.member.mapper.MemberMapper;
import sullog.backend.member.service.MemberService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AlcoholService {

    private final AlcoholMapper alcoholMapper;

    public AlcoholService(AlcoholMapper alcoholMapper) {
        this.alcoholMapper = alcoholMapper;
    }

    public AlcoholInfoDto getAlcoholById(int alcoholId) {
        AlcoholWithBrandDto alcoholWithBrandDto = alcoholMapper.selectByAlcoholIdWithBrand(alcoholId);
        return alcoholWithBrandDto.toAlcoholInfoDto();
    }

    public AlcoholInfoWithPagingDto getAlcoholInfo(AlcoholSearchRequestDto alcoholSearchRequestDto) {
        List<AlcoholInfoDto> alcoholInfoDtoList = new ArrayList<>();
        List<AlcoholWithBrandDto> alcoholWithBrandDtoList = alcoholMapper.pagingSelectByKeyword(
                alcoholSearchRequestDto.getKeyword(),
                alcoholSearchRequestDto.getCursor(),
                alcoholSearchRequestDto.getLimit());

        alcoholWithBrandDtoList
                .forEach(dto -> alcoholInfoDtoList.add(dto.toAlcoholInfoDto()));

        int cursor = 0;
        if (alcoholInfoDtoList.size() > 0) {
            cursor = alcoholWithBrandDtoList.get(alcoholWithBrandDtoList.size() - 1).getAlcoholId();
        }
        PagingInfoDto pagingInfoDto = PagingInfoDto.builder()
                .cursor(cursor)
                .limit(alcoholSearchRequestDto.getLimit())
                .build();

        return AlcoholInfoWithPagingDto.builder()
                .alcoholInfoDtoList(alcoholInfoDtoList)
                .pagingInfoDto(pagingInfoDto)
                .build();
    }

}
