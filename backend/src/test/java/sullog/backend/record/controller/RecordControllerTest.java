package sullog.backend.record.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sullog.backend.member.config.jwt.JwtAuthFilter;
import sullog.backend.member.service.TokenService;
import sullog.backend.record.entity.FlavorDetail;
import sullog.backend.record.dto.RecordSaveRequestDto;
import sullog.backend.record.entity.AlcoholIntensity;
import sullog.backend.record.entity.Record;
import sullog.backend.record.service.ImageUploadService;
import sullog.backend.record.service.RecordService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .memberId(1L)
                .alcoholId(1L)
                .title("Test record")
                .alcoholIntensity(AlcoholIntensity.STRONG)
                .flavorTagList(flavorDetailList)
                .tasteScore(4)
                .textureScore(5)
                .description("This is a test record.")
                .experienceDate(LocalDate.now())
                .build();

        List<MockMultipartFile> mockMultipartFiles = Arrays.asList(
                new MockMultipartFile("photoList", "test1.jpg", "image/jpeg", "test1".getBytes()),
                new MockMultipartFile("photoList", "test2.jpg", "image/jpeg", "test2".getBytes()));


        // when, then
        mockMvc.perform(multipart("/records")
                        .file(mockMultipartFiles.get(0))
                        .file(mockMultipartFiles.get(1))
                        .file(new MockMultipartFile("recordInfo", "", "application/json",  objectMapper.writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isCreated())
                .andDo(document("save-record",
                        requestParts(List.of(
                                partWithName("photoList").description("사용자가 업로드한 이미지 리스트(0 ~ 3장)").optional(),
                                partWithName("recordInfo").description("사용자가 작성한 전통주 경험")
                        )),
                        requestPartFields("recordInfo", List.of(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 id"),
                                fieldWithPath("alcoholId").type(JsonFieldType.NUMBER).description("술 id"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("alcoholIntensity").type(JsonFieldType.STRING).description("도수 느낌"),
                                fieldWithPath("flavorTagList").type(JsonFieldType.ARRAY).description("상세 플레이버(optional)").optional(),
                                fieldWithPath("flavorTagList[0].majorTag").type(JsonFieldType.STRING).description("2분류").optional(),
                                fieldWithPath("flavorTagList[0].detailTag").type(JsonFieldType.STRING).description("3분류").optional(),
                                fieldWithPath("tasteScore").type(JsonFieldType.NUMBER).description("맛점수 (1~5)"),
                                fieldWithPath("textureScore").type(JsonFieldType.NUMBER).description("감촉점수 (1~5)"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("상세 내용"),
                                fieldWithPath("experienceDate").type(JsonFieldType.STRING).description("경험 날짜")
                        ))
                ));
    }
}