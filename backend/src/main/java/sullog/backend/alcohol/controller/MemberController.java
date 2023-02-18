package sullog.backend.alcohol.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sullog.backend.alcohol.service.MemberService;

@RestController
@RequestMapping("/api/member")
@Validated
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> registerNewMember(
            @RequestParam String memberId,
            @RequestParam String nickname,
            @RequestParam String searchWordListJson) {

        memberService.registerNewMember(memberId, nickname, searchWordListJson);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
