package sullog.backend.alcohol.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import sullog.backend.member.dto.request.MemberRegisterDto;
import sullog.backend.member.mapper.MemberMapper;

@Service
public class MemberService {

    private final MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public void registerNewMember(MemberRegisterDto memberRegisterDto) {
        final String SEARCH_WORD_LIST_JSON_DEFAULT = "{}";
        memberMapper.insertMember(
                memberRegisterDto.getMemberId(),
                memberRegisterDto.getNickname(),
                SEARCH_WORD_LIST_JSON_DEFAULT);
    }
}
