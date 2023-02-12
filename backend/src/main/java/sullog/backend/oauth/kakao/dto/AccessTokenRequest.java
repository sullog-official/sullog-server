package sullog.backend.oauth.kakao.dto;

//TODO
// client_id 주입 방식 yml 로 변경
// redirect_uri 환경에 따라 host 달라야 함. 프로파일로 처리하면 될듯 ?

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class AccessTokenRequest {
    private static final String grant_type = "authorization_code";
    private static final String client_id = "e1fdefd4d593e11ae9e4543da253b675"; //REST_API_KEY //TODO 의재님이 앱등록 다시해주시면, 수정 필요함
    private static final String redirect_uri = "http://localhost:3000/oauth/callback/kakao"; //REDIRECT_URI //TODO 의재님이 앱등록 다시해주시면, 수정 필요함

    public static MultiValueMap<String, String> withCode(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grant_type);
        params.add("client_id", client_id);
        params.add("redirect_uri", redirect_uri);
        params.add("code", code);
        return params;
    }
}
