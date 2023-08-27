package sullog.backend.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.util.StringUtils;
import sullog.backend.member.entity.Member;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
public class AppleLoginRequestDto {

    private String code;
    private String name;
    private String email;

    public AppleLoginRequestDto() {
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Member authorizedMember(){
        if (false == StringUtils.hasText(email)) {
            return null;
        }

        if (false == StringUtils.hasText(name)) {
            return null;
        }

        return Member.ofRegisterMember(email, name);
    }

}
