package sullog.backend.member.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.specification.RequestSpecification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "classpath:/application.yml")
class SecurityConfigTest {

    private RequestSpecification documentSpec;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        this.documentSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }


    @Test
    void kakao로그인_시도하면_OAuth로그인창이_등장한다() {
        given(this.documentSpec)
                .filter(document("oauth2/request-kakao-login"))
                .when()
                    .redirects().follow(false)
                    .get("/oauth2/authorization/kakao")
                .then()
                    .statusCode(302)
                    .header("Location", containsString("login/oauth2/code/kakao"));
    }
}