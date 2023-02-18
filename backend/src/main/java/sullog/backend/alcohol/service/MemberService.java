package sullog.backend.alcohol.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import sullog.backend.member.mapper.MemberMapper;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

    public void registerNewMember(
            String memberId,
            String nickname,
            String searchWordListJson) {
        memberMapper.insertMember(memberId, nickname, searchWordListJson);
    }
}
