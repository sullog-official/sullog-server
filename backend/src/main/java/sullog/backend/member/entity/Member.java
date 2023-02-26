package sullog.backend.member.entity;

import lombok.Builder;
import lombok.ToString;
import sullog.backend.common.entity.BaseEntity;

import java.time.Instant;

@ToString(callSuper = true)
public class Member extends BaseEntity {

    private final int memberId;

    private final String email;

    private final String nickname;

    private final String searchWordList;

    @Builder
    public Member(
            int memberId,
            String email,
            String nickname,
            String searchWordList,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.searchWordList = searchWordList;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSearchWordList() {
        return searchWordList;
    }

}
