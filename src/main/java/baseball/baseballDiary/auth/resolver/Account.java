package baseball.baseballDiary.auth.resolver;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Account {
    private UUID memberCd;
    private String ip;
    private String accessToken;
    private String refreshToken;
    private String expireMinutes;
}
