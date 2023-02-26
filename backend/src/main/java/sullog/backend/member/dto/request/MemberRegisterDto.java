package sullog.backend.member.dto.request;


import lombok.ToString;

@ToString
public class MemberRegisterDto {

    private String memberId;

    private String nickname;

    public MemberRegisterDto(
            String memberId,
            String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getNickname() {
        return nickname;
    }

}
