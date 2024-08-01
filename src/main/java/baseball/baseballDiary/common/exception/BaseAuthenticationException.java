package baseball.baseballDiary.common.exception;

/**
 * 로그인 인증 예외처리
 */
public class BaseAuthenticationException extends RuntimeException {

    public BaseAuthenticationException(String message) {
        super(message);
    }

}
