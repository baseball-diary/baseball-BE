package baseball.baseballDiary.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberTokenDto {
    private final String accessToken;
    private final String refreshToken;
}
