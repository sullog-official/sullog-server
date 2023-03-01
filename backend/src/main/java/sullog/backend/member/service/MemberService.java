package sullog.backend.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import sullog.backend.member.dto.request.MemberRegisterDto;
import sullog.backend.member.dto.response.RecentSearchHistoryDto;
import org.springframework.stereotype.Service;
import sullog.backend.member.entity.Member;
import sullog.backend.member.mapper.MemberMapper;

@Service
public class MemberService {

    private MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public RecentSearchHistoryDto getRecentSearchHistory(int memberId) {
        String recentSearchHistoryStr = memberMapper.selectRecentSearchHistory(memberId);
        List<String> recentSearchList = Arrays.stream(recentSearchHistoryStr.split(",")).collect(Collectors.toList());
        return RecentSearchHistoryDto.builder()
                .recentSearchWordList(recentSearchList)
                .build();
    }

    public void registerMember(Member member) {
        if(isAlreadyRegisteredMember(member)) {
            return;
        }

        memberMapper.insertMember(member);
    }

    public Member findMemberByEmail(String email) {
        return memberMapper.selectMemberByEmail(email);
    }

    private boolean isAlreadyRegisteredMember(Member member) {
        return memberMapper.selectMemberByEmail(member.getEmail()) != null;
    }
}
