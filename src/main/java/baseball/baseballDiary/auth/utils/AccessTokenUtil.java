package baseball.baseballDiary.auth.utils;

import baseball.baseballDiary.auth.resolver.Account;
import baseball.baseballDiary.common.constants.ApiState;
import baseball.baseballDiary.common.exception.CommonLogicException;
import baseball.baseballDiary.common.property.BaseProperty;
import baseball.baseballDiary.common.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AccessTokenUtil {

    public static String getAccessToken(HttpServletRequest request) {
        var accessToken = request.getHeader("accessToken");
        log.info("accessToken : {}", accessToken);
        if (StringUtils.isNotEmpty(accessToken)) {
            return accessToken;
        }
        return null;
    }

    public static Boolean checkToken(HttpServletRequest request, BaseProperty property) {
        var accessToken = getAccessToken(request);
        log.info("accessToken : {}", accessToken);
        // accessToken 유무 판단
        if (StringUtils.isEmpty(accessToken)) {
            return accessError(accessToken);
        }

        //유효시간내의 토큰 해석
        checkAccessToken(accessToken, property, request);
        return true;
    }

    //토큰 체크
    public static Boolean checkAccessToken(String accessToken, BaseProperty property, HttpServletRequest request) {

        Map<String, Object> parseToken = JwtUtil.parseToken(accessToken, request, property);
        boolean isCertifiedToken = Boolean.parseBoolean(parseToken.get("isCertifiedToken").toString());
        if (!isCertifiedToken) {
            //accessToken is wrong
            return accessError(accessToken);
        }
        boolean isExpired = Boolean.parseBoolean(parseToken.get("isExpired").toString());
        if (isExpired) {
            // RefreshToken 요청
            return needCheckRefreshTokenError(accessToken);
        }

        // token 중에 저장된 유저정보 가져오기
        Object accountByToken = parseToken.get("accountByToken");
        ObjectMapper objectMapper = new ObjectMapper();

        Object account = objectMapper.convertValue(accountByToken, Account.class);

        if (account == null) {
            return accessError(accessToken);
        } else {
            request.setAttribute("accountByToken", account);
        }
        return true;
    }

    //Refresh 토큰 체크
    public static Account checkUserRefreshToken(String refreshToken, BaseProperty property, HttpServletRequest request) {
        Map<String, Object> parseToken = JwtUtil.parseToken(refreshToken, request, property);
        boolean isCertifiedToken = Boolean.parseBoolean(parseToken.get("isCertifiedToken").toString());
        if (!isCertifiedToken) {
            log.warn("refreshToken is wrong or expired : {}", refreshToken);
            throw new CommonLogicException(ApiState.ACCESS_REFRESH_ERROR);
        }
        boolean isExpired = Boolean.parseBoolean(parseToken.get("isExpired").toString());
        if (isExpired) {
            log.warn("refreshToken is wrong or expired : {}", refreshToken);
            throw new CommonLogicException(ApiState.ACCESS_REFRESH_ERROR);
        }
        // token 중에 저장된 유저정보 가져오기
        Object accountByToken = parseToken.get("accountByToken");
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.convertValue(accountByToken, Account.class);

        //Token 다시 생성
        makeAuthToken(account, property, request);

        return account;
    }

    public static void makeAuthToken(Account account, BaseProperty property, HttpServletRequest request) {

        //token에 로그인 계정 정보 저장
        Map<String, Object> claims = new HashMap<>();

        account.setRefreshToken(null);
        account.setAccessToken(null);
        claims.put("accountByToken", account);

        //Jwt 시간 2주
        int tokenTime = 2 * 7 * 24 * 60;
        String token = JwtUtil.generateToken(claims, Integer.parseInt(account.getExpireMinutes()), request, property);
        account.setAccessToken(token);
        String refreshToken = JwtUtil.generateToken(claims, tokenTime, request, property);
        account.setRefreshToken(refreshToken);

    }

    public static Boolean accessError(String accessToken) {
        log.warn("accessToken is wrong : {}", accessToken);
        throw new CommonLogicException(ApiState.ACCESS.getCode(), ApiState.ACCESS.getMessage());
    }

    public static Boolean accessExpiredError(String accessToken) {
        log.warn("accessToken is expired : {}", accessToken);
        throw new CommonLogicException(ApiState.ACCESS_EXPIRED.getCode(), ApiState.ACCESS_EXPIRED.getMessage());
    }

    public static Boolean needCheckRefreshTokenError(String accessToken) {
        log.warn("accessToken is expired, need check refreshToken : {}", accessToken);
        throw new CommonLogicException(ApiState.ACCESS_REFRESH.getCode(), ApiState.ACCESS_REFRESH.getMessage());
    }

}
