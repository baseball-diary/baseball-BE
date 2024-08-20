package baseball.baseballDiary.auth.domain;

import baseball.baseballDiary.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/*
* Authentication 안에 저장할 수 있는 OAuth2User 타입 구현
* */
public class PrincipalDetails implements OAuth2User {

    private Member member;
    private Map<String, Object> attributes;

    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: 멤버의 역할 설정 필요?
        return List.of();
    }

    @Override
    public String getName() {
        return member.getNickname();
    }
}
