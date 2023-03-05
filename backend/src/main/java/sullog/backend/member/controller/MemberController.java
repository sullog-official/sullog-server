package sullog.backend.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sullog.backend.member.dto.response.RecentSearchHistoryDto;
import sullog.backend.member.service.MemberService;
import sullog.backend.member.service.TokenService;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;

    public MemberController(MemberService memberService, TokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
    }


    @GetMapping("/{memberId}/recent-search-history")
    public ResponseEntity<RecentSearchHistoryDto> getRecentSearchHistory(
            @PathVariable int memberId) {

        return new ResponseEntity<>(memberService.getRecentSearchHistory(memberId), HttpStatus.OK);
    }

    @DeleteMapping("/members/me")
    public void deleteMember(@RequestHeader String authorization) {
        String email = tokenService.getEmail(authorization);
        memberService.deleteMember(email);
    }

}
