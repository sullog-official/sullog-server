package sullog.backend.oauth.kakao.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sullog.backend.oauth.kakao.dto.AccessTokenRequest;
import sullog.backend.oauth.kakao.dto.AccessTokenResponse;
import sullog.backend.oauth.kakao.dto.UserInfoResponse;

public class KakaoOAuth {

    //https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token
    public AccessTokenResponse getAccessToken(String code) {
        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = AccessTokenRequest.withCode(code);

        //HttpHeader 와 HttpBody 를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(params, headers);

        //카카오 auth 서버로 요청
        AccessTokenResponse accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                accessTokenRequest,
                AccessTokenResponse.class
        ).getBody();

        return accessTokenResponse;
    }

    //https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
    public UserInfoResponse getUserInfo(String token) {
        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 와 HttpBody 를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);

        //카카오 auth 서버로 요청
        UserInfoResponse userInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                userInfoRequest,
                UserInfoResponse.class
        ).getBody();

        return userInfoResponse;
    }

}
