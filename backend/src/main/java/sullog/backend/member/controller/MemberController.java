package sullog.backend.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sullog.backend.alcohol.service.MemberService;
import sullog.backend.member.dto.request.MemberRegisterDto;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<?> registerNewMember(MemberRegisterDto memberRegisterDto) {

        memberService.registerNewMember(memberRegisterDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
