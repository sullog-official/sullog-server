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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sullog.backend.member.config.jwt.JwtAuthFilter;
import sullog.backend.member.service.MemberService;
import sullog.backend.member.service.TokenService;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete; // MockMvcBuilders 사용하면 pathParameters 이용 시 에러발생
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@ExtendWith(RestDocumentationExtension.class)
class MemberControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    void memberId값을_바탕으로_회원탈퇴를_진행한다() throws Exception {
        // given
        String email = "test@test.com";
        String accessToken = "sample_token";

        // when
        doReturn(email).when(tokenService).getEmail(accessToken);
        doNothing().when(memberService).deleteMember(email);

        // then
        this.mockMvc.perform(delete("/members/me")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("member/delete-member",
                        requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("사용자의 access token")
                )));
    }

}