package baseball.baseballDiary.common.utils;

import baseball.baseballDiary.common.property.BaseProperty;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


@Slf4j
public class JwtUtil {
    public static Map<String, Object> parseToken(String token, HttpServletRequest request, BaseProperty property) {
        Claims claims = Jwts.claims();
        try {

            claims = Jwts.parserBuilder()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(property.getSecretKey()))
//                    .requireAudience(getIp(request))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            claims.put("isCertifiedToken", Boolean.TRUE);
            claims.put("isExpired", Boolean.FALSE);
        } catch (ExpiredJwtException ex) {
            log.debug("The token is expired", ex);
            claims.put("isCertifiedToken", Boolean.TRUE);
            claims.put("isExpired", Boolean.TRUE);
        } catch (JwtException ex) {
            log.error("JwtException occurs when parseToken",ex);
            claims.put("isCertifiedToken", Boolean.FALSE);
        } catch (IllegalArgumentException ex) {
            log.error("IllegalArgumentException occurs when parseToken",ex);
            claims.put("isCertifiedToken", Boolean.FALSE);
        }
        return claims;
    }

    public static String generateToken(Map<String, Object> claims, int expireMinutes, HttpServletRequest request, BaseProperty property) {
        String token = null;
        try {
            JwtBuilder jwtBuilder = Jwts.builder();

            if (claims != null) {
                jwtBuilder.setClaims(claims);
            }

            jwtBuilder.setId(getJwtId());
//            jwtBuilder.setAudience(getIp(request));

            if (expireMinutes > 0) {
                Date expireDate = DateUtils.addMinutes(new Date(), expireMinutes);
                jwtBuilder.setExpiration(expireDate);
            }

            byte[] secreKeyBytes = DatatypeConverter.parseBase64Binary(property.getSecretKey());
            jwtBuilder.signWith(new SecretKeySpec(secreKeyBytes, SignatureAlgorithm.HS256.getJcaName()), SignatureAlgorithm.HS256);
            token = jwtBuilder.compact();
        } catch (JwtException ex) {
            log.error("JwtException occurs when generateToken", ex);
        } catch (IllegalArgumentException ex) {
            log.error("IllegalArgumentException occurs when generateToken", ex);
        }
        return token;
    }

    private static String getIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = IPUtil.getClientIp(request);

        if (StringUtils.isBlank(ip)) {
            ip = request.getHeader("Host");
        }
        if (StringUtils.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    private static String getJwtId() {
        // create unique jwtId not do
        return UUID.randomUUID().toString();
    }
}
