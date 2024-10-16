package baseball.baseballDiary.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.client")
public record SocialProperties(SocialProperty naver, SocialProperty kakao) {

}
