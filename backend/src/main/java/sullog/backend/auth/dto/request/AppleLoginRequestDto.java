package sullog.backend.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import sullog.backend.member.entity.Member;

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
        return Member.ofRegisterMember(email, name);
    }

}
