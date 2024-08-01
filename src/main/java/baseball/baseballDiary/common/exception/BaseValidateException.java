package baseball.baseballDiary.common.exception;

/**
 * 검증 예외처리 (클라이언트에 직접 메세지 내려주는 경우)
 */
public class BaseValidateException extends RuntimeException {

    public BaseValidateException(String message) {
        super(message);
    }

}
