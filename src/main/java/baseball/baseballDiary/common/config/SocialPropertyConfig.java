package baseball.baseballDiary.common.config;

import baseball.baseballDiary.common.property.SocialProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SocialProperties.class)
public class SocialPropertyConfig {
}
