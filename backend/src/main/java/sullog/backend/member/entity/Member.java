package sullog.backend.member.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sullog.backend.common.entity.BaseEntity;

import java.util.Collection;
import java.util.List;

public class Member extends BaseEntity implements UserDetails {

    private final String email;
    private String nickName;
    private List<String> searchWordList;

    public Member(String email, String nickName) {
        this.email = email;
        this.nickName = nickName;
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
}
