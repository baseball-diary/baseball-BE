package baseball.baseballDiary.common.constants;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum ApiState {

    PARAMETER(10000,"error.10000"),
    LOGIC(20000,"error.20000"),
    VALIDATE(30000,"error.30000"),
    PERMISSION(90000,"error.90000"),
    SYSTEM(900,"error.900"),
    CLIENT(901,"error.901"),
    ACCESS(902,"error.902"),
    ACCESS_EXPIRED(903,"error.903"),
    IP_ERROR(904,"error.904"),

    AUTH_SNS(907,"error.907"),
    ACCESS_REFRESH(908,"error.908"),
    ACCESS_REFRESH_ERROR(909,"error.909"),

    OTHER(999,"error.999");

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
