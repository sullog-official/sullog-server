package sullog.backend.member.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sullog.backend.member.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @DeleteMapping("/members/{memberId}")
    public void deleteMember(@PathVariable int memberId){
        memberService.deleteMember(memberId);
    }
}
