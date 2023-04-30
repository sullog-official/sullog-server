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

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me/recent-search-history")
    public ResponseEntity<RecentSearchHistoryDto> getRecentSearchHistory(@RequestAttribute Integer memberId) {
        return new ResponseEntity<>(memberService.getRecentSearchHistory(memberId), HttpStatus.OK);
    }

    @DeleteMapping("/me/recent-search-history/{keyword}")
    public ResponseEntity<Void> removeSpecificSearchKeyword(@RequestAttribute Integer memberId,
                                                            @PathVariable String keyword) {
        memberService.removeSearchKeyword(memberId, keyword);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/recent-search-history")
    public ResponseEntity<Void> removeSpecificSearchKeyword(@RequestAttribute Integer memberId) {
        memberService.clearRecentSearchKeyword(memberId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@RequestAttribute Integer memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok().build();
    }
}
