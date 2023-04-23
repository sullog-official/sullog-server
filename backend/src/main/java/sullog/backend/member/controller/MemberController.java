package sullog.backend.member.controller;

import sullog.backend.member.service.MemberService;
import sullog.backend.auth.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sullog.backend.member.dto.response.RecentSearchHistoryDto;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;

    public MemberController(MemberService memberService, TokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    @GetMapping("/me/recent-search-history")
    public ResponseEntity<RecentSearchHistoryDto> getRecentSearchHistory(@RequestHeader String authorization) {
        int memberId = tokenService.getMemberId(authorization);
        return new ResponseEntity<>(memberService.getRecentSearchHistory(memberId), HttpStatus.OK);
    }

    @DeleteMapping("/me/recent-search-history/{keyword}")
    public ResponseEntity<Void> removeSpecificSearchKeyword(@RequestHeader String authorization,
                                                            @PathVariable String keyword) {
        int memberId = tokenService.getMemberId(authorization);
        memberService.removeSearchKeyword(memberId, keyword);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/recent-search-history")
    public ResponseEntity<Void> removeSpecificSearchKeyword(@RequestHeader String authorization) {
        int memberId = tokenService.getMemberId(authorization);
        memberService.clearRecentSearchKeyword(memberId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@RequestHeader String authorization) {
        int memberId = tokenService.getMemberId(authorization);
        memberService.deleteMember(memberId);
        return ResponseEntity.ok().build();
    }
}
