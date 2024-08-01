package baseball.baseballDiary.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TEMP_ERROR(HttpStatus.BAD_REQUEST, "M001", "임시 오류");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
