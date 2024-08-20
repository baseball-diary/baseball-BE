package baseball.baseballDiary.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberTokenDto {
    private final String accessToken;
    private final String refreshToken;
}
