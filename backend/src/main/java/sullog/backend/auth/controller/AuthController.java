package sullog.backend.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sullog.backend.auth.dto.request.AppleLoginRequestDto;
import sullog.backend.auth.service.AppleService;
import sullog.backend.auth.service.KakaoService;
import sullog.backend.member.entity.Member;
import sullog.backend.member.entity.Token;
import sullog.backend.auth.service.TokenService;
import sullog.backend.member.service.MemberService;

@RestController
public class AuthController {

    private final TokenService tokenService;
    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final MemberService memberService;

    public AuthController(TokenService tokenService, KakaoService kakaoService, AppleService appleService, MemberService memberService) {
        this.tokenService = tokenService;
        this.kakaoService = kakaoService;
        this.appleService = appleService;
        this.memberService = memberService;
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<Void> refreshAuth(@RequestAttribute Integer memberId) {
        Token newToken = tokenService.generateToken(memberId, "USER");

        // response 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, newToken.getAccessToken());
        headers.set("Refresh", newToken.getRefreshToken());

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

    @GetMapping("/kakao")
    public ResponseEntity<Void> kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        // 인가코드로 accessToken 조회
        String accessToken = kakaoService.getAccessToken(code);

        // accessToken 로 카카오 서버에 사용자 데이터 조회
        Member oAuth2Member = kakaoService.getKakaoUserInfo(accessToken);

        // 회원가입 절차 진행
        memberService.registerMember(oAuth2Member);

        // 회원정보 조회
        Member findMember = memberService.findMemberByEmail(oAuth2Member.getEmail());

        // 토큰 발급
        Token token = tokenService.generateToken(findMember.getMemberId(), "USER");

        // response 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token.getAccessToken());
        headers.set("Refresh", token.getRefreshToken());

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

    @PostMapping("/apple")
    public ResponseEntity<Void> appleLogin(@RequestBody AppleLoginRequestDto appleLoginRequestDto) throws JsonProcessingException {
        // 인가코드로 user정보 검증
        Member needVerifyMember = appleLoginRequestDto.authorizedMember();
        Member verifiedMember = appleService.verifyUserInfo(appleLoginRequestDto.getCode(), needVerifyMember);

        // 회원가입 절차 진행
        memberService.registerMember(verifiedMember);

        // 회원정보 조회
        Member findMember = memberService.findMemberByEmail(verifiedMember.getEmail());

        // 토큰 발급
        Token token = tokenService.generateToken(findMember.getMemberId(), "USER");

        // response 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token.getAccessToken());
        headers.set("Refresh", token.getRefreshToken());

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

}
