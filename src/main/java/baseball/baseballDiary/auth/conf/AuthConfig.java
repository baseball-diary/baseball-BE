package baseball.baseballDiary.auth.conf;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthConfig {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String state;
}
