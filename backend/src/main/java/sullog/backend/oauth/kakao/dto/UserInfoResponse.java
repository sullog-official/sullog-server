package sullog.backend.oauth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import sullog.backend.oauth.kakao.entity.KakaoAccount;

public class UserInfoResponse {

    private Long id;
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public UserInfoResponse() {
    }

    public Long getId() {
        return id;
    }

    public KakaoAccount getKakaoAccount() {
        return kakaoAccount;
    }

    @Override
    public String toString() {
        return "UserInfoResponse{" +
                "id=" + id +
                ", kakaoAccount=" + kakaoAccount +
                '}';
    }
}
