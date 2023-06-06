package sullog.backend.alcohol.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sullog.backend.alcohol.dto.table.AlcoholWithBrandDto;
import sullog.backend.alcohol.entity.Alcohol;

import java.util.List;

@Mapper
public interface AlcoholMapper {
    Alcohol selectByAlcoholId(@Param("alcoholId") int alcoholId) throws RuntimeException;

    AlcoholWithBrandDto selectByAlcoholIdWithBrand(int alcoholId) throws RuntimeException;

    List<AlcoholWithBrandDto> pagingSelectByKeyword(
            @Param("keyword") String keyword,
            @Param("cursor") int cursor,
            @Param("limit") int limit) throws RuntimeException;

    void updateAlcohol(Alcohol alcohol) throws RuntimeException;

    List<Alcohol> selectAllAlcoholList() throws RuntimeException;

    List<Alcohol> selectFromNameAndLocationInfo(@Param("name") String name,
                                                @Param("lat") double lat,
                                                @Param("lng") double lng) throws RuntimeException;
}
