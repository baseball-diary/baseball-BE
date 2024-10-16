package baseball.baseballDiary.auth.domain;

import baseball.baseballDiary.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/*
* Authentication 안에 저장할 수 있는 OAuth2User 타입 구현
* */
public class PrincipalDetails implements UserDetails {

    private Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    // 해당 User 의 권한을 리턴(role)
    // SecurityFilterChain 에서 권한 체크할때 사용
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: 멤버의 역할 설정 필요?
        return List.of();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
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
}
