package sullog.backend.member.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import sullog.backend.member.dto.response.RecentSearchHistoryDto;
import sullog.backend.member.service.MemberService;
import sullog.backend.member.service.TokenService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
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
                        document("member-get-recent-search", // 문서 조각 디렉토리 명
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