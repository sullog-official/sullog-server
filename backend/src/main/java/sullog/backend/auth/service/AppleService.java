package sullog.backend.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sullog.backend.common.error.ErrorCode;
import sullog.backend.common.error.exception.BusinessException;
import sullog.backend.member.entity.Member;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class AppleService {

    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final ObjectMapper objectMapper;
    private final String keyId;
    private final String teamId;
    private final String privateKeyPath;


    public AppleService(OAuth2ClientProperties oAuth2ClientProperties,
                        ObjectMapper objectMapper,
                        @Value("${apple.key-id}") String keyId,
                        @Value("${apple.team-id}") String teamId,
                        @Value("${apple.key-path}") String privateKeyPath) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.objectMapper = objectMapper;
        this.keyId = keyId;
        this.teamId = teamId;
        this.privateKeyPath = privateKeyPath;
    }

    //https://d0lim.com/blog/2022/06/login-with-apple-workaround/ 에서 code 검증 방식
    public Member verifyUserInfo(String code, Member needVerifyMember) throws JsonProcessingException {
        // 애플 oauth2 정보 조회
        OAuth2ClientProperties.Registration appleRegistration = oAuth2ClientProperties.getRegistration().get("apple");
        OAuth2ClientProperties.Provider appleProvider = oAuth2ClientProperties.getProvider().get("apple");

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", appleRegistration.getAuthorizationGrantType());
        body.add("client_id", appleRegistration.getClientId());
        body.add("redirect_uri", appleRegistration.getRedirectUri());
        body.add("client_secret", makeClientSecret(appleRegistration.getClientId()));
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> appleTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                appleProvider.getTokenUri(),
                HttpMethod.POST,
                appleTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        // 예시
        //{
        //    "access_token": "....",
        //    "token_type": "Bearer",
        //    "expires_in": 3600
        //    "id_token": "xxxxx"
        //}
        JsonNode rootNode = objectMapper.readTree(response.getBody());

        //id_token을 통해 이메일 검증
        SignedJWT signedJWT;
        ReadOnlyJWTClaimsSet getPayload;
        try {
            signedJWT = SignedJWT.parse(String.valueOf(rootNode.get("id_token").asText()));
            getPayload = signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JsonNode payload = objectMapper.readTree(getPayload.toJSONObject().toJSONString());
        String email  = payload.get("email").asText();

        // 애플 로그인 시 redirect uri로 전달받은 user객체 값 검증
        if (false == needVerifyMember.getEmail().equals(email)) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_VERIFIED);
        }

        return needVerifyMember;
    }

    private String makeClientSecret(String clientId) {
        Date expireDate = Date.from(ZonedDateTime.now().plusMonths(1).toInstant());
        return Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setHeaderParam("alg", "ES256")
                .setIssuer(teamId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expireDate)
                .setAudience("https://appleid.apple.com") // 고정
                .setSubject(clientId)
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private PrivateKey getPrivateKey() {
        PrivateKeyInfo object;
        PrivateKey privateKey;
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        try {
            ClassPathResource resource = new ClassPathResource(privateKeyPath);
            String privateKeyString = new String(Files.readAllBytes(Paths.get(resource.getURI())));
            Reader pemReader = new StringReader(privateKeyString);
            PEMParser pemParser = new PEMParser(pemReader);
            object = (PrivateKeyInfo) pemParser.readObject();
            privateKey = converter.getPrivateKey(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return privateKey;
    }

}
