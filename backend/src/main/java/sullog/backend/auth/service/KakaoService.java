package sullog.backend.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sullog.backend.member.entity.Member;

@Service
public class KakaoService {

    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final ObjectMapper objectMapper;

    public KakaoService(OAuth2ClientProperties oAuth2ClientProperties, ObjectMapper objectMapper) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.objectMapper = objectMapper;
    }

    public String getAccessToken(String code) throws JsonProcessingException {
        // 카카오 oauth2 정보 조회
        OAuth2ClientProperties.Registration kakaoRegistration = oAuth2ClientProperties.getRegistration().get("kakao");
        OAuth2ClientProperties.Provider kakaoProvider = oAuth2ClientProperties.getProvider().get("kakao");

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", kakaoRegistration.getAuthorizationGrantType());
        body.add("client_id", kakaoRegistration.getClientId());
        body.add("redirect_uri", kakaoRegistration.getRedirectUri());
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                kakaoProvider.getTokenUri(),
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        return rootNode.get("access_token").asText();
    }

    public Member getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 카카오 oauth2 정보 조회
        OAuth2ClientProperties.Provider kakaoProvider = oAuth2ClientProperties.getProvider().get("kakao");

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                kakaoProvider.getUserInfoUri(),
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickName = jsonNode.get("properties")
                .get("nickname").asText();

        return Member.builder()
                .email(email)
                .nickName(nickName)
                .build();
    }
}
