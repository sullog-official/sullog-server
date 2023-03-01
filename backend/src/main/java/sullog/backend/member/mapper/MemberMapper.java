package sullog.backend.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    void insertMember(
            @Param("memberId") String memberId,
            @Param("nickname") String nickname,
            @Param("searchWordListJson") String searchWordListJson
    ) throws RuntimeException;
}
