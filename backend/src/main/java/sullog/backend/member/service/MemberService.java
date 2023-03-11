package sullog.backend.member.service;

import org.springframework.stereotype.Service;
import sullog.backend.member.dto.response.RecentSearchHistoryDto;
import sullog.backend.member.entity.Member;
import sullog.backend.member.mapper.MemberMapper;

import java.util.List;

@Service
public class MemberService {

    private final MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public RecentSearchHistoryDto getRecentSearchHistory(int memberId) {
        List<String> recentSearchList = memberMapper.selectRecentSearchHistory(memberId);
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

    public Member findMemberById(int memberId) {
        return memberMapper.selectMemberById(memberId);
    }

    public void deleteMember(int memberId) {
        memberMapper.deleteMemberById(memberId);
    }
    
    private boolean isAlreadyRegisteredMember(Member member) {
        return memberMapper.selectMemberByEmail(member.getEmail()) != null;
    }

    public void updateRecentSearchWordList(int memberId, String keyword) {
        Member member = findMemberById(memberId);
        List<String> recentSearchWordList = member.getSearchWordList();
        if (recentSearchWordList.contains(keyword)) {
            return;
        }
        if (recentSearchWordList.size() >= 5) {
            recentSearchWordList.remove(0);
        }
        recentSearchWordList.add(keyword);
        memberMapper.updateSearchWordList(memberId, recentSearchWordList);
    }
}
