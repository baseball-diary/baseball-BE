package baseball.baseballDiary.auth.handler;

import baseball.baseballDiary.auth.domain.PrincipalDetails;
import baseball.baseballDiary.auth.dto.MemberTokenDto;
import baseball.baseballDiary.auth.token.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        // OAuth2User 로 캐스팅하여 인증된 사용자 정보 가져옴
        PrincipalDetails oAuth2User = (PrincipalDetails) authentication.getPrincipal();

        // 토큰 생성
        String name = oAuth2User.getName();
        MemberTokenDto tokenDto = jwtProvider.generateLoginToken(name);

        if(response.isCommitted()){
            // HTTP 응답이 이미 전송되었는지
            return;
        }

        /*
        * 인증 시도 중에 세션에 저장된 인증 관련 속성들을 제거하여
        * 다음 요청에서 해당 속성들이 불필요하게 남아있지 않도록 보장
        * */
        clearAuthenticationAttributes(request);

        response.setHeader("Authorization", tokenDto.getAccessToken());

//        // 생성한 target URL 로 리다이렉트
//        String targetUrl = "/";
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
