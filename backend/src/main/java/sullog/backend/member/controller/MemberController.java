package sullog.backend.member.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import sullog.backend.member.service.MemberService;
import sullog.backend.member.service.TokenService;

@RestController
public class MemberController {

    private final TokenService tokenService;
    private final MemberService memberService;

    public MemberController(TokenService tokenService, MemberService memberService) {
        this.tokenService = tokenService;
        this.memberService = memberService;
    }

    @DeleteMapping("/members/me")
    public void deleteMember(@RequestHeader String authorization){
        String email = tokenService.getEmail(authorization);
        memberService.deleteMember(email);
    }
}
