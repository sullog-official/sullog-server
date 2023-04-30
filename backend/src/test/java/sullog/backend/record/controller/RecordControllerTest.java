package sullog.backend.record.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sullog.backend.alcohol.dto.response.AlcoholInfoDto;
import sullog.backend.alcohol.service.AlcoholService;
import sullog.backend.member.config.jwt.JwtAuthFilter;
import sullog.backend.auth.service.TokenService;
import sullog.backend.record.dto.request.RecordSearchParamDto;
import sullog.backend.record.dto.table.AllRecordMetaWithAlcoholInfoDto;
import sullog.backend.record.dto.table.RecordMetaWithAlcoholInfoDto;
import sullog.backend.record.entity.FlavorDetail;
import sullog.backend.record.dto.RecordSaveRequestDto;
import sullog.backend.record.entity.AlcoholPercentFeeling;
import sullog.backend.record.entity.Record;
import sullog.backend.record.service.ImageUploadService;
import sullog.backend.record.service.RecordService;
import sullog.backend.record.service.RecordStatisticService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecordController.class)
@ExtendWith(RestDocumentationExtension.class)
class RecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecordService recordService;

    @MockBean
    private RecordStatisticService recordStatisticService;

    @MockBean
    private ImageUploadService imageUploadService;

    @MockBean
    private AlcoholService alcoholService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void saveRecordTest() throws Exception {
        // given
        int memberId = 1;
        List<FlavorDetail> flavorDetailList = Arrays.asList(FlavorDetail.of("FLOWER", "CHRYSANTHEMUM"), FlavorDetail.of("DAIRY", "BUTTER"));
        RecordSaveRequestDto requestDto = RecordSaveRequestDto.builder()
                .alcoholId(1)
                .alcoholPercentFeeling(AlcoholPercentFeeling.STRONG)
                .flavorTagList(flavorDetailList)
                .starScore(5)
                .scentScore(3)
                .tasteScore(4)
                .textureScore(5)
                .description("This is a test record.")
                .experienceDate(LocalDate.now())
                .build();

        List<MockMultipartFile> mockMultipartFiles = Arrays.asList(
                new MockMultipartFile("photoList", "test1.jpg", "image/jpeg", "test1".getBytes()),
                new MockMultipartFile("photoList", "test2.jpg", "image/jpeg", "test2".getBytes()));

        when(imageUploadService.uploadImageList(any())).thenReturn(List.of("test1.jpg", "test2.jpg"));
        when(tokenService.getMemberId(anyString())).thenReturn(memberId);

        // when, then
        mockMvc.perform(multipart("/records")
                        .file(mockMultipartFiles.get(0))
                        .file(mockMultipartFiles.get(1))
                        .file(new MockMultipartFile("recordInfo", "", "application/json", objectMapper.writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8)))
                        .with(request -> {
                            request.addHeader("Authorization", "Bearer accessToken");
                            return request;
                        })
                )
                .andExpect(status().isCreated())
                .andDo(document("record/save-record",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(List.of(
                                partWithName("photoList").description("사용자가 업로드한 이미지 리스트(0 ~ 3장)").optional(),
                                partWithName("recordInfo").description("사용자가 작성한 전통주 경험")
                        )),
                        requestPartFields("recordInfo", List.of(
                                fieldWithPath("alcoholId").type(JsonFieldType.NUMBER).description("술 id"),
                                fieldWithPath("alcoholPercentFeeling").type(JsonFieldType.STRING).description("도수 느낌. 값은 별첨 참고)"),
                                fieldWithPath("flavorTagList").type(JsonFieldType.ARRAY).description("상세 플레이버 태그(optional). 값은 별첨 참고").optional(),
                                fieldWithPath("flavorTagList[0].majorTag").type(JsonFieldType.STRING).description("1분류").optional(),
                                fieldWithPath("flavorTagList[0].detailTag").type(JsonFieldType.STRING).description("2분류").optional(),
                                fieldWithPath("starScore").type(JsonFieldType.NUMBER).description("별점 (1~5)"),
                                fieldWithPath("scentScore").type(JsonFieldType.NUMBER).description("향점수 (1~5)"),
                                fieldWithPath("tasteScore").type(JsonFieldType.NUMBER).description("맛점수 (1~5)"),
                                fieldWithPath("textureScore").type(JsonFieldType.NUMBER).description("감촉점수 (1~5)"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("상세 내용"),
                                fieldWithPath("experienceDate").type(JsonFieldType.STRING).description("경험 날짜")
                        ))
                ));
    }

    @Test
    void 멤버id기반_경험기록을_조회한다() throws Exception {
        // given
        int memberId = 1;

        // mocking data
        List<RecordMetaWithAlcoholInfoDto> recordMetaWithAlcoholInfoDtos = makeMockingDBResponse();
        doReturn(recordMetaWithAlcoholInfoDtos).when(recordService).getRecordMetasByMemberId(memberId);
        when(tokenService.getMemberId(anyString())).thenReturn(memberId);

        // when, then
        mockMvc.perform(get("/records/me")
                        .with(request -> {
                            request.addHeader("Authorization", "Bearer accessToken");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].recordId", is(recordMetaWithAlcoholInfoDtos.get(0).toResponseDto().getRecordId())))
                .andExpect(jsonPath("$[0].description", is(recordMetaWithAlcoholInfoDtos.get(0).toResponseDto().getDescription())))
                .andExpect(jsonPath("$[0].mainPhotoPath", is(recordMetaWithAlcoholInfoDtos.get(0).toResponseDto().getMainPhotoPath())))
                .andExpect(jsonPath("$[0].alcoholId", is(recordMetaWithAlcoholInfoDtos.get(0).toResponseDto().getAlcoholId())))
                .andExpect(jsonPath("$[0].alcoholName", is(recordMetaWithAlcoholInfoDtos.get(0).toResponseDto().getAlcoholName())))
                .andExpect(jsonPath("$[0].productionLocation", is(recordMetaWithAlcoholInfoDtos.get(0).toResponseDto().getProductionLocation())))
                .andExpect(jsonPath("$[0].productionLatitude", is(recordMetaWithAlcoholInfoDtos.get(0).toResponseDto().getProductionLatitude())))
                .andExpect(jsonPath("$[0].productionLongitude", is(recordMetaWithAlcoholInfoDtos.get(0).toResponseDto().getProductionLongitude())))
                .andExpect(jsonPath("$[0].alcoholTag", is(recordMetaWithAlcoholInfoDtos.get(0).toResponseDto().getAlcoholTag())))
                .andDo(document("record/get-records-by-memberId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].recordId").description("경험기록 ID"),
                                fieldWithPath("[].description").description("경험기록 상세내용"),
                                fieldWithPath("[].mainPhotoPath").description("사용자가 업로드한 사진 경로(없을 경우, 빈 문자열(\"\")"),
                                fieldWithPath("[].alcoholId").description("전통주 ID"),
                                fieldWithPath("[].alcoholName").description("전통주 이름"),
                                fieldWithPath("[].productionLocation").description("전통주 생산지 명"),
                                fieldWithPath("[].productionLatitude").description("전통주 생산지 위도"),
                                fieldWithPath("[].productionLongitude").description("전통주 생산지 경도"),
                                fieldWithPath("[].alcoholTag").description("전통주 태그"),
                                fieldWithPath("[].brandName").description("브랜드 이름")
                        ))
                );
    }

    private List<RecordMetaWithAlcoholInfoDto> makeMockingDBResponse() {
        RecordMetaWithAlcoholInfoDto recordMetaWithAlcoholInfoDto = RecordMetaWithAlcoholInfoDto.builder()
                .recordId(1)
                .description("This is a first sample record.")
                .photoPathList(Arrays.asList("path/to/photo1.jpg", "path/to/photo2.jpg"))
                .alcoholId(1)
                .alcoholName("test1")
                .productionLocation("서울시 광진구 능동로 120")
                .productionLatitude(37.123456)
                .productionLongitude(126.789012)
                .alcoholTag("SOJU")
                .brandName("진로")
                .build();

        RecordMetaWithAlcoholInfoDto recordMetaWithAlcoholInfoDto2 = RecordMetaWithAlcoholInfoDto.builder()
                .recordId(2)
                .description("This is a second sample record.")
                .photoPathList(Arrays.asList("path/to/photo3.jpg", "path/to/photo4.jpg"))
                .alcoholId(2)
                .alcoholName("test2")
                .productionLocation("서울시 광진구 능동로 120")
                .productionLatitude(36.987654)
                .productionLongitude(127.012345)
                .alcoholTag("FRUIT_WINE")
                .brandName("진로")
                .build();

        RecordMetaWithAlcoholInfoDto recordMetaWithAlcoholInfoDto3 = RecordMetaWithAlcoholInfoDto.builder()
                .recordId(3)
                .description("This is a third sample record.")
                .photoPathList(Arrays.asList("path/to/photo5.jpg", "path/to/photo6.jpg"))
                .alcoholId(3)
                .alcoholName("test3")
                .productionLocation("서울시 광진구 능동로 120")
                .productionLatitude(35.123456)
                .productionLongitude(128.789012)
                .alcoholTag("MAKGEOLLI")
                .brandName("진로")
                .build();

        return List.of(recordMetaWithAlcoholInfoDto, recordMetaWithAlcoholInfoDto2, recordMetaWithAlcoholInfoDto3);
    }

    @Test
    void 경험기록id로_단건의_경험을조회한다() throws Exception {
        // given
        int recordId = 1;

        // mocking data
        Record record = makeRecordData();
        doReturn(record).when(recordService).getRecordByRecordId(recordId);

        AlcoholInfoDto alcoholInfoDto = makeAlcoholInfoDto();
        doReturn(alcoholInfoDto).when(alcoholService).getAlcoholById(record.getAlcoholId());

        // when, then
        mockMvc.perform(get("/records/{recordId}", recordId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.record.recordId").value(record.getRecordId()))
                .andExpect(jsonPath("$.record.memberId").value(record.getMemberId()))
                .andExpect(jsonPath("$.record.alcoholId").value(record.getAlcoholId())) //나머지 검증은 생략
                .andDo(document("record/get-record-by-recordId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("recordId").description("경험 기록 id")),
                        responseFields(
                                fieldWithPath("record").type(JsonFieldType.OBJECT).optional().description("경험기록 정보"),
                                fieldWithPath("record.recordId").type(JsonFieldType.NUMBER).description("기록 id"),
                                fieldWithPath("record.memberId").type(JsonFieldType.NUMBER).description("작성자 id"),
                                fieldWithPath("record.alcoholId").type(JsonFieldType.NUMBER).description("전통주 id"),
                                fieldWithPath("record.photoPathList").type(JsonFieldType.ARRAY).description("사진 경로(3장까지)").optional(),
                                fieldWithPath("record.alcoholPercentFeeling").type(JsonFieldType.STRING).description("도수 느낌. 값은 별첨 참고"),
                                fieldWithPath("record.flavorTagList").type(JsonFieldType.ARRAY).description("상세 플레이버 태그(optional). 값은 별첨 참고").optional(),
                                fieldWithPath("record.flavorTagList[].majorTag").type(JsonFieldType.STRING).description("상세 플레이버 태그 - 1분류(optional)").optional(),
                                fieldWithPath("record.flavorTagList[].detailTag").type(JsonFieldType.STRING).description("상세 플레이버 태그 - 2분류(optional)").optional(),
                                fieldWithPath("record.starScore").type(JsonFieldType.NUMBER).description("별점(1~5)"),
                                fieldWithPath("record.scentScore").type(JsonFieldType.NUMBER).description("향 점수(1~5)"),
                                fieldWithPath("record.tasteScore").type(JsonFieldType.NUMBER).description("맛점수 (1~5)"),
                                fieldWithPath("record.textureScore").type(JsonFieldType.NUMBER).description("감촉점수 (1~5)"),
                                fieldWithPath("record.description").type(JsonFieldType.STRING).description("상세 내용"),
                                fieldWithPath("record.experienceDate").type(JsonFieldType.STRING).description("경험 날짜"),

                                fieldWithPath("alcoholInfo").type(JsonFieldType.OBJECT).optional().description("전통주 정보"),
                                fieldWithPath("alcoholInfo.brandName").description("전통주 브랜드 이름"),
                                fieldWithPath("alcoholInfo.alcoholName").description("전통주 이름"),
                                fieldWithPath("alcoholInfo.alcoholPercent").type(JsonFieldType.NUMBER).description("도수"),
                                fieldWithPath("alcoholInfo.productionLocation").type(JsonFieldType.STRING).description("생산지 주소"),
                                fieldWithPath("alcoholInfo.productionLatitude").type(JsonFieldType.NUMBER).description("생산지 위도"),
                                fieldWithPath("alcoholInfo.productionLongitude").type(JsonFieldType.NUMBER).description("생산지 경도"),
                                fieldWithPath("alcoholInfo.alcoholTag").type(JsonFieldType.STRING).description("전통주 태그")
                        ))
                );

    }

    private Record makeRecordData() {
        return Record.builder()
                .memberId(1)
                .alcoholId(1)
                .photoPathList(Arrays.asList("path1", "path2"))
                .alcoholPercentFeeling(AlcoholPercentFeeling.STRONG)
                .flavorTagList(Arrays.asList(FlavorDetail.of("FLOWER", "CHRYSANTHEMUM"), FlavorDetail.of("DAIRY", "BUTTER")))
                .starScore(5)
                .scentScore(4)
                .tasteScore(3)
                .textureScore(2)
                .description("This is a dummy data 1")
                .experienceDate(LocalDate.of(2022, 3, 4))
                .build();
    }

    private AlcoholInfoDto makeAlcoholInfoDto() {
        return AlcoholInfoDto.builder()
                .alcoholName("전통주 샘플")
                .alcoholTag("SOJU")
                .alcoholPercent(10.0)
                .productionLocation("서울시 광진구")
                .productionLongitude(37.54373496690244)
                .productionLatitude(127.077794504202)
                .brandName("진로")
                .build();
    }

    @Test
    void 특정사용자의_경험기록을_키워드기반으로_조회한다() throws Exception {
        // given
        int memberId = 1;
        RecordSearchParamDto recordSearchParamDto = RecordSearchParamDto.builder()
                .keyword("keyword")
                .cursor(1)
                .limit(2)
                .build();

        // mocking
        when(tokenService.getMemberId(anyString())).thenReturn(memberId);
        List<RecordMetaWithAlcoholInfoDto> recordMetaWithAlcoholInfoDtos = makeMockingDBResponse().stream().limit(recordSearchParamDto.getLimit()).collect(Collectors.toList());
        when(recordService.getRecordMetasByCondition(eq(memberId), any())).thenReturn(recordMetaWithAlcoholInfoDtos);

        // when, then
        mockMvc.perform(get("/records/me/search")
                        .param("cursor", String.valueOf(recordSearchParamDto.getCursor()))
                        .param("keyword", recordSearchParamDto.getKeyword())
                        .param("limit", String.valueOf(recordSearchParamDto.getLimit()))
                        .with(request -> {
                            request.addHeader("Authorization", "Bearer accessToken");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.recordMetaList", hasSize(recordSearchParamDto.getLimit())))
                .andDo(document("record/get-records-filter-by-condition",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters( // path 파라미터 정보 입력
                                parameterWithName("keyword").description("검색 키워드"),
                                parameterWithName("cursor").description("마지막으로 조회한 경험기록 id(최초 조회 시 null)"),
                                parameterWithName("limit").description("한번에 조회해올 데이터 갯수")
                        ),
                        responseFields(
                                fieldWithPath("recordMetaList").type(JsonFieldType.ARRAY).description("경험기록 리스트"),
                                fieldWithPath("recordMetaList[].recordId").description("경험기록 ID"),
                                fieldWithPath("recordMetaList[].description").description("경험기록 상세내용"),
                                fieldWithPath("recordMetaList[].mainPhotoPath").description("사용자가 업로드한 사진 경로(없을 경우, 빈 문자열(\"\")"),
                                fieldWithPath("recordMetaList[].alcoholId").description("전통주 ID"),
                                fieldWithPath("recordMetaList[].alcoholName").description("전통주 이름"),
                                fieldWithPath("recordMetaList[].productionLocation").description("전통주 생산지 명"),
                                fieldWithPath("recordMetaList[].productionLatitude").description("전통주 생산지 위도"),
                                fieldWithPath("recordMetaList[].productionLongitude").description("전통주 생산지 경도"),
                                fieldWithPath("recordMetaList[].alcoholTag").description("전통주 태그"),
                                fieldWithPath("recordMetaList[].brandName").description("브랜드 이름"),
                                fieldWithPath("pagingInfo").type(JsonFieldType.OBJECT).description("페이징 정보"),
                                fieldWithPath("pagingInfo.cursor").type(JsonFieldType.NUMBER).description("마지막으로 조회한 경험기록 id(다음요청 시 그대로 전달)"),
                                fieldWithPath("pagingInfo.limit").type(JsonFieldType.NUMBER).description("한번에 조회해올 데이터 갯수(다음요청 시 그대로 전달)")
                        ))
                );
    }

    @Test
    void 모든_사용자_경험기록_조회() throws Exception { // 피드
        // given
        int memberId = 1;
        RecordSearchParamDto recordSearchParamDto = RecordSearchParamDto.builder()
                .cursor(1)
                .limit(2)
                .build();

        // mocking
        when(tokenService.getMemberId(anyString())).thenReturn(memberId);
        List<AllRecordMetaWithAlcoholInfoDto> allRecordMetaWithAlcoholInfoDtoList = makeMockingDBResponse4All().stream().limit(recordSearchParamDto.getLimit()).collect(Collectors.toList());
        when(recordService.getRecordFeed(anyInt(), anyInt())).thenReturn(allRecordMetaWithAlcoholInfoDtoList);

        // when, then
        mockMvc.perform(get("/records")
                        .param("cursor", String.valueOf(recordSearchParamDto.getCursor()))
                        .param("limit", String.valueOf(recordSearchParamDto.getLimit()))
                        .with(request -> {
                            request.addHeader("Authorization", "Bearer accessToken");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.allRecordMetaList", hasSize(recordSearchParamDto.getLimit())))
                .andDo(document("record/get-record-feeds",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters( // path 파라미터 정보 입력
                                parameterWithName("cursor").description("마지막으로 조회한 경험기록 id(최초 조회 시 null)"),
                                parameterWithName("limit").description("한번에 조회해올 데이터 갯수")
                        ),
                        responseFields(
                                fieldWithPath("allRecordMetaList").type(JsonFieldType.ARRAY).description("경험기록 리스트"),
                                fieldWithPath("allRecordMetaList[].memberId").description("사용자 ID"),
                                fieldWithPath("allRecordMetaList[].recordId").description("경험기록 ID"),
                                fieldWithPath("allRecordMetaList[].description").description("경험기록 상세내용"),
                                fieldWithPath("allRecordMetaList[].mainPhotoPath").description("사용자가 업로드한 사진 경로(없을 경우, 빈 문자열(\"\")"),
                                fieldWithPath("allRecordMetaList[].alcoholId").description("전통주 ID"),
                                fieldWithPath("allRecordMetaList[].alcoholName").description("전통주 이름"),
                                fieldWithPath("allRecordMetaList[].productionLocation").description("전통주 생산지 명"),
                                fieldWithPath("allRecordMetaList[].productionLatitude").description("전통주 생산지 위도"),
                                fieldWithPath("allRecordMetaList[].productionLongitude").description("전통주 생산지 경도"),
                                fieldWithPath("allRecordMetaList[].alcoholTag").description("전통주 태그"),
                                fieldWithPath("allRecordMetaList[].brandName").description("브랜드 이름"),
                                fieldWithPath("pagingInfo").type(JsonFieldType.OBJECT).description("페이징 정보"),
                                fieldWithPath("pagingInfo.cursor").type(JsonFieldType.NUMBER).description("마지막으로 조회한 경험기록 id(다음요청 시 그대로 전달)"),
                                fieldWithPath("pagingInfo.limit").type(JsonFieldType.NUMBER).description("한번에 조회해올 데이터 갯수(다음요청 시 그대로 전달)")
                        ))
                );
    }

    @Test
    public void 사용자_경험_통계_조회() throws Exception {

        // given
        Integer memberId = 1;

        // mocking data
        HashMap<String, Integer> responseMap = new HashMap<>();
        responseMap.put("기타", 3);
        responseMap.put("막걸리", 2);
        responseMap.put("과실주", 5);
        List<RecordMetaWithAlcoholInfoDto> recordMetaWithAlcoholInfoDtos = makeMockingDBResponse();
        doReturn(responseMap).when(recordStatisticService).getRecordStatistics(memberId);
        when(tokenService.getMemberId(anyString())).thenReturn(memberId);

        // when, then
        mockMvc.perform(get("/records/me/statistics")
                        .with(request -> {
                            request.addHeader("Authorization", "Bearer accessToken");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andDo(document("record/get-statistics",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("*").description("주종 명")
                        ))
                );

    }

    private List<AllRecordMetaWithAlcoholInfoDto> makeMockingDBResponse4All() {
        AllRecordMetaWithAlcoholInfoDto allRecordMetaWithAlcoholInfoDto1 = AllRecordMetaWithAlcoholInfoDto.builder()
                .memberId(1)
                .recordId(1)
                .description("This is a first sample record.")
                .photoPathList(Arrays.asList("path/to/photo1.jpg", "path/to/photo2.jpg"))
                .alcoholId(1)
                .alcoholName("test1")
                .productionLocation("서울시 광진구 능동로 120")
                .productionLatitude(37.123456)
                .productionLongitude(126.789012)
                .alcoholTag("SOJU")
                .brandName("진로")
                .build();

        AllRecordMetaWithAlcoholInfoDto allRecordMetaWithAlcoholInfoDto2 = AllRecordMetaWithAlcoholInfoDto.builder()
                .memberId(2)
                .recordId(2)
                .description("This is a second sample record.")
                .photoPathList(Arrays.asList("path/to/photo3.jpg", "path/to/photo4.jpg"))
                .alcoholId(2)
                .alcoholName("test2")
                .productionLocation("서울시 광진구 능동로 120")
                .productionLatitude(36.987654)
                .productionLongitude(127.012345)
                .alcoholTag("FRUIT_WINE")
                .brandName("진로")
                .build();

        AllRecordMetaWithAlcoholInfoDto allRecordMetaWithAlcoholInfoDto3 = AllRecordMetaWithAlcoholInfoDto.builder()
                .memberId(3)
                .recordId(3)
                .description("This is a third sample record.")
                .photoPathList(Arrays.asList("path/to/photo5.jpg", "path/to/photo6.jpg"))
                .alcoholId(3)
                .alcoholName("test3")
                .productionLocation("서울시 광진구 능동로 120")
                .productionLatitude(35.123456)
                .productionLongitude(128.789012)
                .alcoholTag("MAKGEOLLI")
                .brandName("진로")
                .build();

        return List.of(allRecordMetaWithAlcoholInfoDto1, allRecordMetaWithAlcoholInfoDto2, allRecordMetaWithAlcoholInfoDto3);
    }
}