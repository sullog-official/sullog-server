package sullog.backend.member.service;

import org.springframework.stereotype.Service;
import sullog.backend.member.dto.response.RecentSearchHistoryDto;
import sullog.backend.member.entity.Member;
import sullog.backend.member.mapper.MemberMapper;

import java.util.ArrayList;
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

    public void deleteMember(String email) {
        memberMapper.deleteMemberByEmail(email);
    }
    
    private boolean isAlreadyRegisteredMember(Member member) {
        return memberMapper.selectMemberByEmail(member.getEmail()) != null;
    }

    public void updateRecentSearchWordList(String email, String keyword) {
        Member member = findMemberByEmail(email);
        List<String> recentSearchWordList = member.getSearchWordList();
        if (recentSearchWordList.contains(keyword)) {
            return;
        }
        if (recentSearchWordList.size() >= 5) {
            recentSearchWordList.remove(0);
        }
        recentSearchWordList.add(keyword);
        memberMapper.updateSearchWordList(member.getMemberId(), recentSearchWordList);
    }
}
