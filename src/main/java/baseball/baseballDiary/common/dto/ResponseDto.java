package baseball.baseballDiary.common.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Data
@Builder
public class ResponseDto<T> implements Serializable {

    /**
     * 상태
     */
    @Schema(accessMode = READ_ONLY, example = "SUCCESS OR FAIL")
    @Parameter(description = "상태과 코드", required = true)
    @Builder.Default
    private String state = "SUCCESS";

    /**
     * 결과 코드
     */
    @Schema(accessMode = READ_ONLY, example = "200,401,404,500")
    @Parameter(description = "결과 코드", required = true)
    @Builder.Default
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

    private boolean confirm;

    /**
     * error 발생시 봔환정보 저장함수
     */
    public void setError(Integer stateCode, String msg) {
        this.stateCode = stateCode;
        this.msg = msg;
        this.state = "FAIL";
    }

    /**
     * error 발생시 봔환정보 저장함수
     */
    public void setErrorConfirm(Integer stateCode, String msg) {
        this.stateCode = stateCode;
        this.msg = msg;
        this.state = "FAIL";
        this.confirm = true;
    }

}
