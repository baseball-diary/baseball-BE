package baseball.baseballDiary.auth.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "auth.client.registration")
public class SocialLoginProperties {
    private Map<String, String> google;
    private Map<String, String> naver;
    private Map<String, String> kakao;

    public Map<String, String> getGoogle() {
        return google;
    }

    public Map<String, String> getNaver() {
        return naver;
    }

    public Map<String, String> getKakao() {
        return kakao;
    }
}
