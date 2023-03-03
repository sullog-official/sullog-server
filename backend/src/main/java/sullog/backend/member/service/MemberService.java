package sullog.backend.member.service;

import org.springframework.stereotype.Service;
import sullog.backend.member.dto.response.RecentSearchHistoryDto;
import sullog.backend.member.entity.Member;
import sullog.backend.member.mapper.MemberMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public RecentSearchHistoryDto getRecentSearchHistory(int memberId) {
        String recentSearchList = memberMapper.selectRecentSearchHistory(memberId);
        List<String> searchWordList = Arrays.stream(recentSearchList
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .split(",")).toList();

        return RecentSearchHistoryDto.builder()
                .recentSearchWordList(searchWordList)
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

    public void deleteMember(String email) {
        memberMapper.deleteMemberByEmail(email);
    }
    
    private boolean isAlreadyRegisteredMember(Member member) {
        return memberMapper.selectMemberByEmail(member.getEmail()) != null;
    }
}
