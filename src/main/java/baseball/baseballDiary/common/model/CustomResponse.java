package baseball.baseballDiary.common.model;

import baseball.baseballDiary.common.constants.ApiState;
import baseball.baseballDiary.common.exception.BaseValidateException;
import baseball.baseballDiary.common.exception.CommonLogicException;
import baseball.baseballDiary.common.utils.LocalDateUtil;
import baseball.baseballDiary.common.exception.BaseAuthenticationException;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "공통 Response")
@Getter
public class CustomResponse<T> {

    /**
     * 상태
     */
    @Schema(accessMode = READ_ONLY, example = "SUCCESS OR FAIL")
    @Parameter(description = "상태과 코드", required = true)
    private StateCode state = StateCode.SUCCESS;

    /**
     * 결과 코드
     */
    @Schema(accessMode = READ_ONLY, example = "200,401,404,500")
    @Parameter(description = "결과 코드", required = true)
    private Integer stateCode = 200;

    /**
     * 결과 메시지
     */
    @Schema(accessMode = READ_ONLY, example = "성공")
    @Parameter(description = "결과 메시지")
    private String msg;

    /**
     * 결과 데이터
     */
    @Parameter(description = "결과 데이터")
    private T data;

    /**
     * 응답 시간
     */
    @Schema(accessMode = READ_ONLY, example = "20210701143000")
    @Parameter(description = "응답 시간")
    private String timestamp = LocalDateUtil.formatKST(LocalDateTime.now());

    /**
     * 게시물 갯수 : List 일때만 표출
     */
    @Schema(accessMode = READ_ONLY, example = "10")
    @Parameter(description = "Row 갯수")
    private Integer count = null;

    // ??
    private boolean confirm;

    public CustomResponse(T data) {
        this.data = data;
    }

    public CustomResponse(T data, Integer count) {
        this.data = data;
        this.count = count;
    }

    public CustomResponse(Integer stateCode, String msg) {
        this.state = StateCode.FAIL;
        this.stateCode = stateCode;
        this.msg = msg;
    }

    public CustomResponse(Integer stateCode, String msg, boolean confirm) {
        this.state = StateCode.FAIL;
        this.stateCode = stateCode;
        this.msg = msg;
        this.confirm = confirm;
    }

    public CustomResponse(Exception e) {
        this.stateCode = ApiState.SYSTEM.getCode();
        this.msg = "System error";

        if (e instanceof CommonLogicException) {
            CommonLogicException ex = (CommonLogicException) e;
            String customMessage = ((CommonLogicException) e).getCustomMessage();
            this.stateCode = ex.getCode();
            if (StringUtils.hasText(customMessage)) {
                this.msg = customMessage;
            } else {
                this.msg = ex.getExMessage(ex.getCode(), ex.getMessageKey(), ex.getArgs());
            }
            if (ArrayUtils.isNotEmpty(ex.getValidateMessage())) {
                this.data = (T) ex.getValidateMessage();
            }
        } else if (e instanceof IllegalArgumentException) {
            this.msg = e.getMessage();
        } else if (e instanceof BaseValidateException) {
            this.stateCode = ApiState.VALIDATE.getCode();
            this.msg = e.getMessage();
        } else if (e instanceof BaseAuthenticationException) {
            this.stateCode = ApiState.ACCESS_EXPIRED.getCode();
            this.msg = e.getMessage();
            this.confirm = true;
        }
        this.state = StateCode.FAIL;
    }


    /**
     * 데이터를 채운 성공 응답을 생성하는 정적 메서드
     */
    public static <T> CustomResponse<T> Ok(T data) {
        return new CustomResponse<>(data);
    }

    /**
     * 데이터와 카운트를 채운 성공 응답을 생성하는 정적 메서드
     */
    public static <T> CustomResponse<T> Ok(T data, int count) {
        return new CustomResponse<>(data, count);
    }

    public static CustomResponse Error(Exception e) {
        return new CustomResponse<>(e);
    }

//    /**
//     * error 발생시 봔환정보 저장함수
//     */
//    public void setError(Integer stateCode, String msg) {
//        this.stateCode = stateCode;
//        this.msg = msg;
//        this.state = "FAIL";
//    }
//
//    /**
//     * error 발생시 봔환정보 저장함수
//     */
//    public void setErrorConfirm(Integer stateCode, String msg) {
//        this.stateCode = stateCode;
//        this.msg = msg;
//        this.state = "FAIL";
//        this.confirm = true;
//    }

    enum StateCode {
        SUCCESS, FAIL
    }
}
