package baseball.baseballDiary.auth.config;

import java.util.List;

public record SocialProvider(
        String clientId,
        String clientSecret,
        String redirectUri,
        String tokenUri,
        String resourceUri,
        String authorizationGrantType,
        List<String> scope
) {}
