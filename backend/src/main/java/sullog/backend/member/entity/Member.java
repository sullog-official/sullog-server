package sullog.backend.member.entity;

import lombok.Builder;
import lombok.ToString;
import sullog.backend.common.entity.BaseEntity;

import java.time.Instant;

@ToString(callSuper = true)
public class Member extends BaseEntity {

    private final String memberId;

    private final String nickname;

    private final String searchWordList;

    @Builder
    public Member(
            String memberId,
            String nickname,
            String searchWordList,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.memberId = memberId;
        this.nickname = nickname;
        this.searchWordList = searchWordList;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSearchWordList() {
        return searchWordList;
    }

}
