package baseball.baseballDiary.auth.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "auth.client.registration")
@Getter
public class SocialLoginProperties {

    private Google google;

    // Getter and Setter for google
    public Google getGoogle() {
        return google;
    }

    public void setGoogle(Google google) {
        this.google = google;
    }

    // Google record
    public static record Google(
            String clientId,
            String clientSecret,
            String redirectUri,
            String tokenUri,
            String resourceUri,
            String authorizationGrantType,
            List<String> scope
    ) {}
}
