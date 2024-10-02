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
    private Map<String, SocialProvider> providers;
}

