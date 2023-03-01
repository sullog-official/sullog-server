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

}
