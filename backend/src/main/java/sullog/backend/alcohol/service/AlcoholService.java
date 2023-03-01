package sullog.backend.alcohol.service;

import org.springframework.stereotype.Service;
import sullog.backend.alcohol.dto.response.AlcoholInfoDto;
import sullog.backend.alcohol.dto.response.AlcoholInfoWithPagingDto;
import sullog.backend.alcohol.dto.response.PagingInfoDto;
import sullog.backend.alcohol.dto.table.AlcoholWithBrandWithPagingDto;
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
        AlcoholWithBrandWithPagingDto alcoholWithBrandWithPagingDto = alcoholMapper.selectByAlcoholIdWithBrand(alcoholId);
        return AlcoholInfoDto.builder()
                .alcoholName(alcoholWithBrandWithPagingDto.getAlcoholName())
                .alcoholPercent(alcoholWithBrandWithPagingDto.getAlcoholPercent())
                .alcoholTag(alcoholWithBrandWithPagingDto.getAlcoholTag())
                .brandName(alcoholWithBrandWithPagingDto.getBrandName())
                .productionLatitude(alcoholWithBrandWithPagingDto.getProductionLatitude())
                .productionLongitude(alcoholWithBrandWithPagingDto.getProductionLongitude())
                .productionLocation(alcoholWithBrandWithPagingDto.getProductionLocation())
                .alcoholPercent(alcoholWithBrandWithPagingDto.getAlcoholPercent())
                .build();
    }

    public AlcoholInfoWithPagingDto getAlcoholInfo(String keyword, int cursor, int limit) {
        List<AlcoholInfoDto> alcoholInfoDtoList = new ArrayList<>();
        AtomicInteger currentCursor = new AtomicInteger();
        alcoholMapper
                .pagingSelectByKeyword(keyword, cursor, limit)
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
                    currentCursor.set(dto.getAlcoholId());
                });

        PagingInfoDto pagingInfoDto = PagingInfoDto.builder()
                .cursor(currentCursor.get())
                .limit(limit)
                .build();

        return AlcoholInfoWithPagingDto.builder()
                .alcoholInfoDtoList(alcoholInfoDtoList)
                .pagingInfoDto(pagingInfoDto)
                .build();
    }
}
