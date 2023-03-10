package sullog.backend.record.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import sullog.backend.member.service.TokenService;
import sullog.backend.record.dto.request.RecordSearchParamDto;
import sullog.backend.record.dto.table.RecordMetaWithAlcoholInfoDto;
import sullog.backend.record.entity.FlavorDetail;
import sullog.backend.record.dto.RecordSaveRequestDto;
import sullog.backend.record.entity.AlcoholPercentFeeling;
import sullog.backend.record.entity.Record;
import sullog.backend.record.service.ImageUploadService;
import sullog.backend.record.service.RecordService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
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
        List<FlavorDetail> flavorDetailList = Arrays.asList(FlavorDetail.of("FLOWER", "CHRYSANTHEMUM"), FlavorDetail.of("DAIRY", "BUTTER"));
        RecordSaveRequestDto requestDto = RecordSaveRequestDto.builder()
                .memberId(1)
                .alcoholId(1)
                .title("Test record")
                .alcoholPercentFeeling(AlcoholPercentFeeling.STRONG)
                .flavorTagList(flavorDetailList)
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


        // when, then
        mockMvc.perform(multipart("/records")
                        .file(mockMultipartFiles.get(0))
                        .file(mockMultipartFiles.get(1))
                        .file(new MockMultipartFile("recordInfo", "", "application/json",  objectMapper.writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isCreated())
                .andDo(document("record/save-record",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(List.of(
                                partWithName("photoList").description("???????????? ???????????? ????????? ?????????(0 ~ 3???)").optional(),
                                partWithName("recordInfo").description("???????????? ????????? ????????? ??????")
                        )),
                        requestPartFields("recordInfo", List.of(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("????????? id"),
                                fieldWithPath("alcoholId").type(JsonFieldType.NUMBER).description("??? id"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("alcoholPercentFeeling").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("flavorTagList").type(JsonFieldType.ARRAY).description("?????? ????????????(optional)").optional(),
                                fieldWithPath("flavorTagList[0].majorTag").type(JsonFieldType.STRING).description("2??????").optional(),
                                fieldWithPath("flavorTagList[0].detailTag").type(JsonFieldType.STRING).description("3??????").optional(),
                                fieldWithPath("scentScore").type(JsonFieldType.NUMBER).description("????????? (1~5)"),
                                fieldWithPath("tasteScore").type(JsonFieldType.NUMBER).description("????????? (1~5)"),
                                fieldWithPath("textureScore").type(JsonFieldType.NUMBER).description("???????????? (1~5)"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("experienceDate").type(JsonFieldType.STRING).description("?????? ??????")
                        ))
                ));
    }

    @Test
    void ??????id??????_???????????????_????????????() throws Exception {
        // given
        int memberId = 1;

        // mocking data
        List<RecordMetaWithAlcoholInfoDto> recordMetaWithAlcoholInfoDtos = makeMockingDBResponse();
        doReturn(recordMetaWithAlcoholInfoDtos).when(recordService).getRecordMetasByMemberId(memberId);

        // when, then
        mockMvc.perform(get("/records")
                        .param("memberId", String.valueOf(memberId)))
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
                        requestParameters(
                                parameterWithName("memberId").description("????????? ????????? ID")
                        ),
                        responseFields(
                                fieldWithPath("[].recordId").description("???????????? ID"),
                                fieldWithPath("[].description").description("???????????? ????????????"),
                                fieldWithPath("[].mainPhotoPath").description("???????????? ???????????? ?????? ??????(?????? ??????, ??? ?????????(\"\")"),
                                fieldWithPath("[].alcoholId").description("????????? ID"),
                                fieldWithPath("[].alcoholName").description("????????? ??????"),
                                fieldWithPath("[].productionLocation").description("????????? ????????? ???"),
                                fieldWithPath("[].productionLatitude").description("????????? ????????? ??????"),
                                fieldWithPath("[].productionLongitude").description("????????? ????????? ??????"),
                                fieldWithPath("[].alcoholTag").description("????????? ??????"),
                                fieldWithPath("[].brandName").description("????????? ??????")
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
                .productionLocation("????????? ????????? ????????? 120")
                .productionLatitude(37.123456)
                .productionLongitude(126.789012)
                .alcoholTag("SOJU")
                .brandName("??????")
                .build();

        RecordMetaWithAlcoholInfoDto recordMetaWithAlcoholInfoDto2 = RecordMetaWithAlcoholInfoDto.builder()
                .recordId(2)
                .description("This is a second sample record.")
                .photoPathList(Arrays.asList("path/to/photo3.jpg", "path/to/photo4.jpg"))
                .alcoholId(2)
                .alcoholName("test2")
                .productionLocation("????????? ????????? ????????? 120")
                .productionLatitude(36.987654)
                .productionLongitude(127.012345)
                .alcoholTag("FRUIT_WINE")
                .brandName("??????")
                .build();

        RecordMetaWithAlcoholInfoDto recordMetaWithAlcoholInfoDto3 = RecordMetaWithAlcoholInfoDto.builder()
                .recordId(3)
                .description("This is a third sample record.")
                .photoPathList(Arrays.asList("path/to/photo5.jpg", "path/to/photo6.jpg"))
                .alcoholId(3)
                .alcoholName("test3")
                .productionLocation("????????? ????????? ????????? 120")
                .productionLatitude(35.123456)
                .productionLongitude(128.789012)
                .alcoholTag("MAKGEOLLI")
                .brandName("??????")
                .build();

        return List.of(recordMetaWithAlcoholInfoDto, recordMetaWithAlcoholInfoDto2, recordMetaWithAlcoholInfoDto3);
    }

    @Test
    void ????????????id???_?????????_?????????????????????() throws Exception {
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
                .andExpect(jsonPath("$.record.alcoholId").value(record.getAlcoholId())) //????????? ????????? ??????
                .andDo(document("record/get-record-by-recordId",
                    pathParameters(parameterWithName("recordId").description("?????? ?????? id")),
                    responseFields(
                            fieldWithPath("record").type(JsonFieldType.OBJECT).optional().description("???????????? ??????"),
                            fieldWithPath("record.recordId").type(JsonFieldType.NUMBER).description("?????? id"),
                            fieldWithPath("record.memberId").type(JsonFieldType.NUMBER).description("????????? id"),
                            fieldWithPath("record.alcoholId").type(JsonFieldType.NUMBER).description("????????? id"),
                            fieldWithPath("record.title").type(JsonFieldType.STRING).description("??????"),
                            fieldWithPath("record.photoPathList").type(JsonFieldType.ARRAY).description("?????? ??????(3?????????)").optional(),
                            fieldWithPath("record.alcoholPercentFeeling").type(JsonFieldType.STRING).description("?????? ??????"),
                            fieldWithPath("record.flavorTagList").type(JsonFieldType.ARRAY).description("?????? ???????????? ??????(optional)").optional(),
                            fieldWithPath("record.flavorTagList[].majorTag").type(JsonFieldType.STRING).description("?????? ???????????? ?????? - 2??????(optional)").optional(),
                            fieldWithPath("record.flavorTagList[].detailTag").type(JsonFieldType.STRING).description("?????? ???????????? ?????? - 3??????(optional)").optional(),
                            fieldWithPath("record.scentScore").type(JsonFieldType.NUMBER).description("??? ??????(1~5)"),
                            fieldWithPath("record.tasteScore").type(JsonFieldType.NUMBER).description("????????? (1~5)"),
                            fieldWithPath("record.textureScore").type(JsonFieldType.NUMBER).description("???????????? (1~5)"),
                            fieldWithPath("record.description").type(JsonFieldType.STRING).description("?????? ??????"),
                            fieldWithPath("record.experienceDate").type(JsonFieldType.STRING).description("?????? ??????"),

                            fieldWithPath("alcoholInfo").type(JsonFieldType.OBJECT).optional().description("????????? ??????"),
                            fieldWithPath("alcoholInfo.brandName").description("????????? ????????? ??????"),
                            fieldWithPath("alcoholInfo.alcoholName").description("????????? ??????"),
                            fieldWithPath("alcoholInfo.alcoholPercent").type(JsonFieldType.NUMBER).description("??????"),
                            fieldWithPath("alcoholInfo.productionLocation").type(JsonFieldType.STRING).description("????????? ??????"),
                            fieldWithPath("alcoholInfo.productionLatitude").type(JsonFieldType.NUMBER).description("????????? ??????"),
                            fieldWithPath("alcoholInfo.productionLongitude").type(JsonFieldType.NUMBER).description("????????? ??????"),
                            fieldWithPath("alcoholInfo.alcoholTag").type(JsonFieldType.STRING).description("????????? ??????")
                    ))
                );

    }

    private Record makeRecordData() {
        return Record.builder()
                .memberId(1)
                .alcoholId(1)
                .title("Dummy Data 1")
                .photoPathList(Arrays.asList("path1", "path2"))
                .alcoholPercentFeeling(AlcoholPercentFeeling.STRONG)
                .flavorTagList(Arrays.asList(FlavorDetail.of("FLOWER", "CHRYSANTHEMUM"), FlavorDetail.of("DAIRY", "BUTTER")))
                .scentScore(4)
                .tasteScore(3)
                .textureScore(2)
                .description("This is a dummy data 1")
                .experienceDate(LocalDate.of(2022, 3, 4))
                .build();
    }

    private AlcoholInfoDto makeAlcoholInfoDto() {
        return AlcoholInfoDto.builder()
                .alcoholName("????????? ??????")
                .alcoholTag("SOJU")
                .alcoholPercent(10.0)
                .productionLocation("????????? ?????????")
                .productionLongitude(37.54373496690244)
                .productionLatitude(127.077794504202)
                .brandName("??????")
                .build();
    }

    @Test
    void ??????????????????_???????????????_?????????????????????_????????????() throws Exception {
        // given
        RecordSearchParamDto recordSearchParamDto = RecordSearchParamDto.builder()
                .memberId(1)
                .keyword("keyword")
                .cursor(1)
                .limit(2)
                .build();

        // when, then
        List<RecordMetaWithAlcoholInfoDto> recordMetaWithAlcoholInfoDtos = makeMockingDBResponse().stream().limit(recordSearchParamDto.getLimit()).collect(Collectors.toList());
        doReturn(recordMetaWithAlcoholInfoDtos).when(recordService).getRecordMetasByCondition(any());

        // when, then
        mockMvc.perform(get("/records/search")
                        .param("memberId", String.valueOf(recordSearchParamDto.getMemberId()))
                        .param("cursor", String.valueOf(recordSearchParamDto.getCursor()))
                        .param("keyword", recordSearchParamDto.getKeyword())
                        .param("limit", String.valueOf(recordSearchParamDto.getLimit())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.recordMetaList", hasSize(recordSearchParamDto.getLimit())))
                .andDo(document("record/get-records-filter-by-condition",
                        requestParameters( // path ???????????? ?????? ??????
                                parameterWithName("memberId").description("?????? ????????? ??? ????????? id"),
                                parameterWithName("keyword").description("?????? ?????????"),
                                parameterWithName("cursor").description("??????????????? ????????? ???????????? id(?????? ?????? ??? null)"),
                                parameterWithName("limit").description("????????? ???????????? ????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("recordMetaList").type(JsonFieldType.ARRAY).description("???????????? ?????????"),
                                    fieldWithPath("recordMetaList[].recordId").description("???????????? ID"),
                                    fieldWithPath("recordMetaList[].description").description("???????????? ????????????"),
                                    fieldWithPath("recordMetaList[].mainPhotoPath").description("???????????? ???????????? ?????? ??????(?????? ??????, ??? ?????????(\"\")"),
                                    fieldWithPath("recordMetaList[].alcoholId").description("????????? ID"),
                                    fieldWithPath("recordMetaList[].alcoholName").description("????????? ??????"),
                                    fieldWithPath("recordMetaList[].productionLocation").description("????????? ????????? ???"),
                                    fieldWithPath("recordMetaList[].productionLatitude").description("????????? ????????? ??????"),
                                    fieldWithPath("recordMetaList[].productionLongitude").description("????????? ????????? ??????"),
                                    fieldWithPath("recordMetaList[].alcoholTag").description("????????? ??????"),
                                    fieldWithPath("recordMetaList[].brandName").description("????????? ??????"),
                                fieldWithPath("pagingInfo").type(JsonFieldType.OBJECT).description("????????? ??????"),
                                    fieldWithPath("pagingInfo.cursor").type(JsonFieldType.NUMBER).description("??????????????? ????????? ???????????? id(???????????? ??? ????????? ??????)"),
                                    fieldWithPath("pagingInfo.limit").type(JsonFieldType.NUMBER).description("????????? ???????????? ????????? ??????(???????????? ??? ????????? ??????)")
                        ))
                );
    }
}