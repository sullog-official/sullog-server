package sullog.backend.member.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sullog.backend.auth.controller.AuthController;
import sullog.backend.auth.service.KakaoService;
import sullog.backend.member.config.jwt.JwtAuthFilter;
import sullog.backend.member.entity.Member;
import sullog.backend.member.entity.Token;
import sullog.backend.auth.service.TokenService;
import sullog.backend.member.service.MemberService;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ExtendWith(RestDocumentationExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private KakaoService kakaoService;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void testRefreshAuth() throws Exception {
        // given
        String token = "sample_token";
        int memberId = 0;
        Token newToken = new Token("sample_access_token", "sample_refresh_token");

        // when
        when(tokenService.getMemberId(token)).thenReturn(memberId);
        when(tokenService.generateToken(memberId, "USER")).thenReturn(newToken);

        // then
        this.mockMvc.perform(get("/token/refresh")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.AUTHORIZATION, newToken.getAccessToken()))
                .andExpect(MockMvcResultMatchers.header().string("Refresh", newToken.getRefreshToken()))
                .andDo(document("token-refresh",
                        responseHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("The new access token"), // response-headers.adoc 파일에 추가
                                headerWithName("Refresh").description("The new refresh token") // response-headers.adoc 파일에 추가
                        )
                ));
    }

    @Test
    void 인가코드를_전달받으면_jwt토큰을_반환한다() throws Exception {
        // given
        String code = "test_code";
        String accessToken = "test_accessToken";
        Member member = Member.builder().memberId(0).email("test@email.com").build();
        Token newToken = new Token("sample_access_token", "sample_refresh_token");

        when(kakaoService.getAccessToken(code)).thenReturn(accessToken);
        when(kakaoService.getKakaoUserInfo(accessToken)).thenReturn(member);
        when(memberService.findMemberByEmail(member.getEmail())).thenReturn(member);
        when(tokenService.generateToken(member.getMemberId(), "USER")).thenReturn(newToken);

        // when, then
        this.mockMvc.perform(get("/kakao")
                        .param("code", code))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.AUTHORIZATION, newToken.getAccessToken()))
                .andExpect(MockMvcResultMatchers.header().string("Refresh", newToken.getRefreshToken()))
                .andDo(document("oauth2/request-kakao-login",
                        requestParameters( // path 파라미터 정보 입력
                                parameterWithName("code").description("카카오 인증 서버에서 발급해준 인가코드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("The new access token"), // response-headers.adoc 파일에 추가
                                headerWithName("Refresh").description("The new refresh token") // response-headers.adoc 파일에 추가
                        )
                ));
    }
}