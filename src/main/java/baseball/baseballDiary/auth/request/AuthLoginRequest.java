package baseball.baseballDiary.auth.request;

import baseball.baseballDiary.auth.conf.AuthCallback;
import baseball.baseballDiary.auth.conf.AuthConfig;
import baseball.baseballDiary.auth.conf.AuthSource;
import baseball.baseballDiary.auth.utils.HttpClientUtil;
import baseball.baseballDiary.auth.utils.UrlBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthLoginRequest {

    private AuthConfig authConfig;
    private AuthSource authSource;

    public AuthLoginRequest(AuthConfig authConfig, AuthSource authSource) {
        this.authConfig = authConfig;
        this.authSource = authSource;
    }

    public String authorize() {
        return UrlBuilder.fromBaseUrl(authSource.authorize())
                .queryParam("response_type", "code")
                .queryParam("client_id", authConfig.getClientId())
                .queryParam("client_secret", authConfig.getClientSecret())
                .queryParam("redirect_uri", authConfig.getRedirectUri())
                .queryParam("prompt", "login")
                .build();
    }

    public Map<String, Object> getRequestToken(AuthCallback authCallback) {
        String url = UrlBuilder.fromBaseUrl(authSource.accessToken()).build();

        Map<String, String> param = new HashMap<>();
        param.put("grant_type", "authorization_code");
        param.put("client_id", authConfig.getClientId());
        param.put("client_secret", authConfig.getClientSecret());
        param.put("redirect_uri", authConfig.getRedirectUri());
        param.put("code", authCallback.getCode());

        String response = null;

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> dataMap = null;
        response = HttpClientUtil.doPost(url , param);
        try {
            dataMap = mapper.readValue(response, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> userMap = null;
        try {
            String error = MapUtils.getString(dataMap, "error");
            String access_token = MapUtils.getString(dataMap, "access_token");
            if(StringUtils.isEmpty(error)  && StringUtils.isNotBlank(access_token)){
                userMap = getUserInfo(AuthCallback.builder().oauth_token(access_token).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataMap.putAll(userMap);

        return dataMap;
    }

    public Map<String, Object>  getUserInfo(AuthCallback authCallback) {
        String url = getUserInfoUrl();
        log.warn("url:*******" + url + "*******");
        String response = null;

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> userMap = null;

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + authCallback.getOauth_token());
        Map<String, String> param = new HashMap<>();
        param.put("perty_keys","[\"properties.nickname\", \"kakao_account.email\", \"kakao_account.phone_number\"]");
        response = HttpClientUtil.doGet(url, null ,headers);
        try {
            userMap = mapper.readValue(response, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.warn("response:*******" + response + "*******");
        log.warn("userMap: " + userMap);

        return userMap;
    }

    protected String getUserInfoUrl() {
        return UrlBuilder.fromBaseUrl(authSource.profileUrl()).build();
    }


}
