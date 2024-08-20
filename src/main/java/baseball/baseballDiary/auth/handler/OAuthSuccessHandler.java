package baseball.baseballDiary.auth.handler;

import baseball.baseballDiary.auth.domain.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        // OAuth2User 로 캐스팅하여 인증된 사용자 정보 가져옴
        PrincipalDetails oAuth2User = (PrincipalDetails) authentication.getPrincipal();

        String name = oAuth2User.getName();
        UserTokens token = jwtProvider.generateLoginToken(name);

        response.setHeader("Authorization", token.getAccessToken());

        // 쿠키를 저장하는 걸로 설정
        Cookie cookie = new Cookie("JWT", token.getAccessToken());
        cookie.setMaxAge(60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        String targetUrl = "/";
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
