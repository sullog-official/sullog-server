package sullog.backend.alcohol.controller;

// import문을 잘 확인해야 합니다.

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
import sullog.backend.alcohol.dto.request.AlcoholSearchRequestDto;
import sullog.backend.alcohol.dto.response.AlcoholInfoDto;
import sullog.backend.alcohol.dto.response.AlcoholInfoWithPagingDto;
import sullog.backend.alcohol.dto.response.PagingInfoDto;
import sullog.backend.alcohol.service.AlcoholService;
import sullog.backend.member.config.jwt.JwtAuthFilter;
import sullog.backend.auth.service.TokenService;
import sullog.backend.member.entity.Member;
import sullog.backend.member.service.MemberService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlcoholController.class)
@ExtendWith(RestDocumentationExtension.class)
class AlcoholControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AlcoholService alcoholService;

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
    public void 샘플_주류_조회() throws Exception {

        AlcoholInfoDto alcoholInfoDto = AlcoholInfoDto.builder()
                .alcoholId(1)
                .alcoholName("전통주 샘플")
                .alcoholType("기타")
                .alcoholTag("{}")
                .alcoholPercent(10.0)
                .productionLocation("서울시 광진구")
                .productionLongitude(0.0)
                .productionLatitude(0.0)
                .brandName("진로")
                .build();

        when(memberService.findMemberById(anyInt())).thenReturn(Member.builder().build());
        when(alcoholService.getAlcoholById(anyInt())).thenReturn(alcoholInfoDto);

        mockMvc.perform(
                        get("/alcohols")
                                .param("alcoholId", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo( // rest docs 문서 작성 시작
                        document("alcohol-get", // 문서 조각 디렉토리 명
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters( // path 파라미터 정보 입력
                                        parameterWithName("alcoholId").description("전통주 ID")
                                ),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("alcoholId").type(JsonFieldType.NUMBER).description("전통주 ID"),
                                        fieldWithPath("alcoholName").type(JsonFieldType.STRING).description("전통주 이름"),
                                        fieldWithPath("brandName").type(JsonFieldType.STRING).description("브랜드 이름"),
                                        fieldWithPath("alcoholType").type(JsonFieldType.STRING).description("주종"),
                                        fieldWithPath("alcoholPercent").type(JsonFieldType.NUMBER).description("도수"),
                                        fieldWithPath("productionLocation").type(JsonFieldType.STRING).description("생산지 주소"),
                                        fieldWithPath("productionLatitude").type(JsonFieldType.NUMBER).description("생산지 위도"),
                                        fieldWithPath("productionLongitude").type(JsonFieldType.NUMBER).description("생산지 경도"),
                                        fieldWithPath("alcoholTag").type(JsonFieldType.STRING).description("전통주 태그")
                                )
                        )
                )
        ;

    }

    @Test
    public void 키워드_커서_기반_전통주_조회() throws Exception {
        List<AlcoholInfoDto> alcoholInfoDtoList = new ArrayList<>();
        AlcoholInfoDto alcoholInfoDto = AlcoholInfoDto.builder()
                .alcoholId(1)
                .alcoholName("전통주 샘플")
                .alcoholTag("{}")
                .alcoholType("기타")
                .alcoholPercent(10.0)
                .productionLocation("서울시 광진구")
                .productionLongitude(0.0)
                .productionLatitude(0.0)
                .brandName("진로")
                .build();

        alcoholInfoDtoList.add(alcoholInfoDto);
        alcoholInfoDtoList.add(alcoholInfoDto);
        PagingInfoDto pagingInfoDto = PagingInfoDto.builder()
                .limit(10)
                .cursor(5)
                .build();
        AlcoholInfoWithPagingDto alcoholInfoWithPagingDto = AlcoholInfoWithPagingDto.builder()
                .alcoholInfoDtoList(alcoholInfoDtoList)
                .pagingInfoDto(pagingInfoDto)
                .build();

        AlcoholSearchRequestDto alcoholSearchRequestDto = AlcoholSearchRequestDto.builder()
                .keyword("keyword")
                .cursor(1)
                .limit(1)
                .build();

        int memberId = 1;
        when(tokenService.getMemberId(anyString())).thenReturn(memberId);
        when(memberService.findMemberById(anyInt())).thenReturn(Member.builder().build());
        when(alcoholService.getAlcoholInfo(alcoholSearchRequestDto)).thenReturn(alcoholInfoWithPagingDto);

        mockMvc.perform(
                        get("/alcohols/search")
                                .queryParam("keyword", alcoholSearchRequestDto.getKeyword())
                                .queryParam("cursor", String.valueOf(alcoholSearchRequestDto.getCursor()))
                                .queryParam("limit", String.valueOf(alcoholSearchRequestDto.getLimit()))
                                .with(request -> {
                                    request.addHeader("Authorization", "Bearer accessToken");
                                    return request;
                                })
                )
                .andExpect(status().isOk())
                .andDo( // rest docs 문서 작성 시작
                        document("alcohol-search", // 문서 조각 디렉토리 명
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters( // path 파라미터 정보 입력
                                        parameterWithName("keyword").description("키워드"),
                                        parameterWithName("cursor").description("커서"),
                                        parameterWithName("limit").description("조회 제한 개수")
                                ),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("alcoholInfoDtoList").type(JsonFieldType.ARRAY).description("전통주 리스트"),
                                        fieldWithPath("alcoholInfoDtoList[].alcoholId").type(JsonFieldType.NUMBER).description("전통주 ID"),
                                        fieldWithPath("alcoholInfoDtoList[].alcoholName").type(JsonFieldType.STRING).description("전통주 명"),
                                        fieldWithPath("alcoholInfoDtoList[].brandName").type(JsonFieldType.STRING).description("브랜드 명"),
                                        fieldWithPath("alcoholInfoDtoList[].alcoholType").type(JsonFieldType.STRING).description("주종"),
                                        fieldWithPath("alcoholInfoDtoList[].alcoholPercent").type(JsonFieldType.NUMBER).description("도수"),
                                        fieldWithPath("alcoholInfoDtoList[].productionLocation").type(JsonFieldType.STRING).description("생산지 주소"),
                                        fieldWithPath("alcoholInfoDtoList[].productionLatitude").type(JsonFieldType.NUMBER).description("생산지 위도"),
                                        fieldWithPath("alcoholInfoDtoList[].productionLongitude").type(JsonFieldType.NUMBER).description("생산지 경도"),
                                        fieldWithPath("alcoholInfoDtoList[].alcoholTag").type(JsonFieldType.STRING).description("전통주 태그"),
                                        fieldWithPath("pagingInfoDto").type(JsonFieldType.OBJECT).description("페이징 정보"),
                                        fieldWithPath("pagingInfoDto.cursor").type(JsonFieldType.NUMBER).description("커서 위치"),
                                        fieldWithPath("pagingInfoDto.limit").type(JsonFieldType.NUMBER).description("페이지 제한 개수")
                                )
                        )
                )
        ;

    }


}