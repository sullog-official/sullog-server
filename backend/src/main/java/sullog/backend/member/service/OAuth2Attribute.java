package sullog.backend.member.service;

import lombok.Builder;
import sullog.backend.member.entity.Member;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Builder
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String name;

    static OAuth2Attribute of(String provider,
                              Map<String, Object> attributes) {
        switch (provider) {
            case "kakao":
                return ofKakao(attributes);
            case "apple":
                // TODO 개발 필요
            default:
                throw new RuntimeException();
        }
    }
    private static OAuth2Attribute ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .attributes(kakaoAccount)
                .attributeKey("email")
                .build();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("name", name);
        map.put("email", email);

        return map;
    }

    public Member toMember() {
        return Member.builder()
                .email(email)
                .nickName(name)
                .searchWordList(Collections.EMPTY_LIST)
                .build();
    }

    @Override
    public String toString() {
        return "OAuth2Attribute{" +
                "attributes=" + attributes +
                ", attributeKey='" + attributeKey + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}