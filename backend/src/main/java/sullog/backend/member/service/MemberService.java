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
        String recentSearchList = memberMapper.selectRecentSearchHistory(memberId);
        System.out.println("recentSearchListSTR = " + recentSearchList);
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

    public void updateRecentSearchWordList(String email, String keyword) {
        Member member = findMemberByEmail(email);
        RecentSearchHistoryDto recentSearchHistoryDto = getRecentSearchHistory(member.getMemberId());
        List<String> recentSearchWordList = new ArrayList<>(recentSearchHistoryDto.getRecentSearchWordList());
        if (recentSearchWordList.contains(keyword)) {
            return;
        }
        if (recentSearchWordList.size() >= 5) {
            recentSearchWordList.remove(0);
        }
        recentSearchWordList.add(keyword);
        memberMapper.updateSearchWordList(member.getMemberId(), parseListToListStr(recentSearchWordList));
    }

    private String parseListToListStr(List<String> recentSearchWordList) {
        StringBuilder builder = new StringBuilder("[");
        for (String word : recentSearchWordList) {
            builder.append(word).append(",");
        }
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }
        System.out.println("builder.toString() = " + builder.toString());
        return builder.append("]").toString();
    }
}
