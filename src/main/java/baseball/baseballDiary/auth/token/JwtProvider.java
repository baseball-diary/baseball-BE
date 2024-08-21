package baseball.baseballDiary.auth.token;

import baseball.baseballDiary.auth.domain.RefreshToken;
import baseball.baseballDiary.auth.repository.TokenRepository;
import baseball.baseballDiary.auth.dto.MemberTokenDto;
import baseball.baseballDiary.auth.service.CustomDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private String secret = "tokensecretkeytokensecretkeytokensecretkeytokensecretkeytokensecretkey";
    private SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    private final TokenRepository tokenRepository;
    private final CustomDetailService customDetailService;

    // 어세스 토큰 유효시간 | 20m
    private final long accessTokenValidTime = 20 * 60 * 1000L;
    // 리프레시 토큰 유효시간 | 1Month
    private final long refreshTokenValidTime = 30L * 24 * 60 * 60 * 1000;


    // Token 발급
    public MemberTokenDto generateLoginToken(String subject) {
        String refreshToken = generateToken(subject, refreshTokenValidTime);
        String accessToken = generateToken(subject, accessTokenValidTime);

        // Refresh 토큰 저장
        tokenRepository.save(new RefreshToken(refreshToken));

        return new MemberTokenDto(accessToken, refreshToken);
    }

    // 토큰 생성
    public String generateToken(String subject, Long validTime) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            // TODO: 예외처리
            return false;
        }
    }

    // 사용자 정보 추출
    public Authentication getAuthentication(String token) {

        // JWT 에서 Claim 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = customDetailService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public String resolveAccessToken(HttpServletRequest request) {
        if(request.getHeader("authorization") != null )
            return request.getHeader("authorization").substring(7);
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        if(request.getHeader("refreshToken") != null )
            return request.getHeader("refreshToken").substring(7);
        return null;
    }
}
