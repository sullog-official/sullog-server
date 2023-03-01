package sullog.backend.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sullog.backend.member.entity.Member;

@Mapper
public interface MemberMapper {

    void insertMember(Member member) throws RuntimeException;

    Member selectMemberByEmail(@Param("email") String email);

    String selectRecentSearchHistory(@Param("memberId") int memberId) throws RuntimeException;
}
