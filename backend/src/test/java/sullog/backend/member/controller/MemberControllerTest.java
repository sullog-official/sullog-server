package sullog.backend.member.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sullog.backend.member.config.jwt.JwtAuthFilter;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete; // MockMvcBuilders 사용하면 pathParameters 이용 시 에러발생
import sullog.backend.member.dto.response.RecentSearchHistoryDto;
import sullog.backend.member.service.MemberService;
import sullog.backend.member.service.TokenService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@ExtendWith(RestDocumentationExtension.class)
class MemberControllerTest {

    @Autowired
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
        int memberId = 0;
        String accessToken = "sample_token";

        // when
        doReturn(memberId).when(tokenService).getMemberId(accessToken);
        doNothing().when(memberService).deleteMember(memberId);

        // then
        this.mockMvc.perform(delete("/api/members/members/me")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("member/delete-member",
                        requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("사용자의 access token")
                )));
    }

    @Test
    public void 최근_검색어_조회() throws Exception {

        RecentSearchHistoryDto recentSearchHistoryDto = RecentSearchHistoryDto.builder()
                .recentSearchWordList(List.of("abc", "가나다"))
                .build();

        when(memberService.getRecentSearchHistory(anyInt())).thenReturn(recentSearchHistoryDto);

        mockMvc.perform(
                        get("/api/members/{memberId}/recent-search-history", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo( // rest docs 문서 작성 시작
                        document("member-get-recent-search",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("memberId").description("멤버 아이디")
                                ),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("recentSearchWordList").type(JsonFieldType.ARRAY).description("최근 검색어 리스트")
                                )
                        )
                )
        ;
    }
}