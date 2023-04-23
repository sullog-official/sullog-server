package sullog.backend.member.entity;

import lombok.Builder;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import sullog.backend.common.entity.BaseEntity;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@ToString(callSuper = true)
public class Member extends BaseEntity implements UserDetails {

    private final int memberId;
    private final String email;
    private final String nickName;
    private final List<String> searchWordList;

    @Builder
    public Member(
            int memberId,
            String email,
            String nickName,
            List<String> searchWordList,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.memberId = memberId;
        this.email = email;
        this.nickName = nickName;
        this.searchWordList = searchWordList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return nickName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public List<String> getSearchWordList() {
        return searchWordList;
    }

    public int getMemberId() {
        return memberId;
    }

    public static Member of(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return Member.builder()
                .memberId((int)attributes.get("memberId"))
                .email((String)attributes.get("email"))
                .nickName((String)attributes.get("name"))
                .build();
    }

    public static Member ofRegisterMember(String email, String nickName) {
        return Member.builder()
                .email(email)
                .nickName(nickName)
                .searchWordList(List.of())
                .build();
    }
}
