package sullog.backend.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    String selectRecentSearchHistory(@Param("memberId") int memberId) throws RuntimeException;
}
