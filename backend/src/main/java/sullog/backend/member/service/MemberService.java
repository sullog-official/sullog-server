package sullog.backend.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import sullog.backend.member.dto.request.MemberRegisterDto;
import sullog.backend.member.dto.response.RecentSearchHistoryDto;
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
        String recentSearchHistoryStr = memberMapper.selectRecentSearchHistory(memberId);
        List<String> recentSearchList = Arrays.stream(recentSearchHistoryStr.split(",")).collect(Collectors.toList());
        return RecentSearchHistoryDto.builder()
                .recentSearchWordList(recentSearchList)
                .build();
    }
}
