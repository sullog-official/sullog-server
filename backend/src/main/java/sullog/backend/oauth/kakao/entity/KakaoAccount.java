package sullog.backend.oauth.kakao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoAccount {
    @JsonProperty
    private Profile profile;
    @JsonProperty
    private String email; // 비즈니스 채널 연동하면 가능

    public KakaoAccount() {
    }

    public String getNickName() {
        return profile.nickname;
    }

    @Override
    public String toString() {
        return "KakaoAccount{" +
                "profile=" + profile +
                ", email='" + email + '\'' +
                '}';
    }

     private class Profile {
        @JsonProperty
        private String nickname;

        public Profile() {
        }

        @Override
        public String toString() {
            return "Profile{" +
                    "nickname='" + nickname + '\'' +
                    '}';
        }
    }
}
