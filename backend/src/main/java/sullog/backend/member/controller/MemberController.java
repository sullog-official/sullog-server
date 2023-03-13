package sullog.backend.member.controller;

import sullog.backend.member.service.MemberService;
import sullog.backend.member.service.TokenService;
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


    @GetMapping("/{memberId}/recent-search-history")
    public ResponseEntity<RecentSearchHistoryDto> getRecentSearchHistory(
            @PathVariable int memberId) {

        return new ResponseEntity<>(memberService.getRecentSearchHistory(memberId), HttpStatus.OK);
    }

    @DeleteMapping("/me")
    public void deleteMember(@RequestHeader String authorization) {
        String email = tokenService.getEmail(authorization);
        memberService.deleteMember(email);
    }
}
