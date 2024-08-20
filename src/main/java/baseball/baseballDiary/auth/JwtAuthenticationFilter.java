package baseball.baseballDiary.auth;

import baseball.baseballDiary.auth.token.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 토큰 확인
        String accessToken = jwtProvider.resolveAccessToken(request);

        if (accessToken != null) {
            // 토큰 값이 유효하면 검증
            if (jwtProvider.validateToken(accessToken)) {
                // 토큰 검증 (인증객체 생성)
                Authentication authentication = jwtProvider.getAuthentication(accessToken);

                // SecurityContext 에 인증 객체 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                // accessToken 값 만료
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "AccessToken Expired");
            }
        }

        filterChain.doFilter(request, response);
    }
}
