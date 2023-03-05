package sullog.backend.member.config.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sullog.backend.member.entity.Member;
import sullog.backend.member.entity.Token;
import sullog.backend.member.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    public OAuth2SuccessHandler(TokenService tokenService, ObjectMapper objectMapper) {
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    // oauth 서버에서 accessToken, refreshToekn 생성
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        Member member = Member.of(oAuth2User);

        Token token = tokenService.generateToken(member.getMemberId(), "USER");

        makeTokenResponse(response, token);
    }

    private void makeTokenResponse(HttpServletResponse response, Token token) throws IOException {
        response.addHeader(HttpHeaders.AUTHORIZATION, token.getAccessToken());
        response.addHeader("Refresh", token.getRefreshToken());

        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}
