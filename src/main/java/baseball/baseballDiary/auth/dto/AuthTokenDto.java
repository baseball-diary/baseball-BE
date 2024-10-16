package baseball.baseballDiary.auth.dto;

public record AuthTokenDto(String accessToken, String refreshToken) {

    public AuthTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
