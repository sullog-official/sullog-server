package sullog.backend.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sullog.backend.oauth.kakao.dto.AccessTokenResponse;
import sullog.backend.oauth.kakao.dto.UserInfoResponse;
import sullog.backend.oauth.kakao.service.KakaoOAuth;

@RestController
public class MemberController {

    private KakaoOAuth kakaoOAuth;

    public MemberController(KakaoOAuth kakaoOAuth) {
        this.kakaoOAuth = kakaoOAuth;
    }

    @GetMapping("/oauth/callback/kakao")
    public void login(@RequestParam String code) {
        AccessTokenResponse accessToken = kakaoOAuth.getAccessToken(code);
        UserInfoResponse userInfo = kakaoOAuth.getUserInfo(accessToken.getAccessToken());
        // member service
            // 필요시 회원가입
            // sullog JWT 토큰 처리
        // sullog JWT 토큰 실어서 반한
    }
}
