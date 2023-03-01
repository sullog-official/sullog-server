package sullog.backend.alcohol.service;

import org.springframework.stereotype.Service;
import sullog.backend.alcohol.dto.request.AlcoholSearchRequestDto;
import sullog.backend.alcohol.dto.response.AlcoholInfoDto;
import sullog.backend.alcohol.dto.response.AlcoholInfoWithPagingDto;
import sullog.backend.alcohol.dto.response.PagingInfoDto;
import sullog.backend.alcohol.dto.table.AlcoholWithBrandDto;
import sullog.backend.alcohol.mapper.AlcoholMapper;

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
                .forEach(dto -> {
                    AlcoholInfoDto alcoholInfoDto = AlcoholInfoDto.builder()
                            .alcoholName(dto.getAlcoholName())
                            .alcoholPercent(dto.getAlcoholPercent())
                            .alcoholTag(dto.getAlcoholTag())
                            .brandName(dto.getBrandName())
                            .productionLatitude(dto.getProductionLatitude())
                            .productionLongitude(dto.getProductionLongitude())
                            .productionLocation(dto.getProductionLocation())
                            .alcoholPercent(dto.getAlcoholPercent())
                            .build();
                    alcoholInfoDtoList.add(alcoholInfoDto);
                });

        int cursor = alcoholWithBrandDtoList.get(alcoholWithBrandDtoList.size() - 1).getAlcoholId();
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
