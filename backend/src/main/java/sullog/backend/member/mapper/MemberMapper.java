package sullog.backend.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sullog.backend.member.entity.Member;

import java.util.List;

@Mapper
public interface MemberMapper {

    void insertMember(Member member) throws RuntimeException;

    Member selectMemberByEmail(@Param("email") String email);

    String selectRecentSearchHistory(@Param("memberId") int memberId) throws RuntimeException;

    int updateSearchWordList(
            @Param("memberId") int memberId,
            @Param("searchWordList") String searchWordList) throws RuntimeException;
}
