package sullog.backend.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sullog.backend.member.dto.response.RecentSearchHistoryDto;
import sullog.backend.member.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/{memberId}/recent-search-history")
    public ResponseEntity<RecentSearchHistoryDto> getRecentSearchHistory(
            @PathVariable int memberId) {

        return new ResponseEntity<>(memberService.getRecentSearchHistory(memberId), HttpStatus.OK);
    }
}
