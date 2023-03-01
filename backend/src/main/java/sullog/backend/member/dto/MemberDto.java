package sullog.backend.member.dto;

import lombok.Builder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@Builder
public class MemberDto {
    private String email;
    private String nickName;

    public MemberDto(String email, String nickName) {
        this.email = email;
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public static MemberDto of(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return MemberDto.builder()
                .email((String)attributes.get("email"))
                .nickName((String)attributes.get("name"))
                .build();
    }
}
