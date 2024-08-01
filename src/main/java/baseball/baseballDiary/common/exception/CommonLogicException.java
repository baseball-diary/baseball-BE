package baseball.baseballDiary.common.exception;

import baseball.baseballDiary.common.constants.ApiState;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class CommonLogicException extends ExceptionMessage {

	private Integer code;

	private String messageKey;

	private String customMessage;

	private Object[] args;

	private Object[] validateMessage;

    public CommonLogicException(ApiState apiState) {
        this.code = apiState.getCode();
        this.messageKey = apiState.getMessage();
    }

	public CommonLogicException(Integer code, String msgKey) {
		this.code = code;
		this.messageKey = msgKey;
	}

    public CommonLogicException(Integer code, String msgKey, String customMessage) {
        this.code = code;
        this.messageKey = msgKey;
        this.customMessage = customMessage;
    }

	public CommonLogicException(Integer code, String messageKey, Object... args) {
		this.code = code;
		this.messageKey = messageKey;
		this.args = args;
	}

	public CommonLogicException(Integer code, String messageKey, Object[] args, Object[] validateMessage) {
		this.code = code;
		this.messageKey = messageKey;
		this.args = args;
		this.validateMessage = validateMessage;
	}

}
