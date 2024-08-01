package baseball.baseballDiary.common.utils;

import baseball.baseballDiary.common.constants.ApiState;
import baseball.baseballDiary.common.exception.CommonLogicException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * 검증 유틸
 */
public class ValidateUtils {

    /**
     * 휴대폰 정규식 체크
     * @param value
     * @return
     */
    public static boolean isPhone(String value) {
        return Pattern.matches("^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", value);
    }

    /**
     * 대한민국 지역 번호 및 인터넷 전화(VoIP) 번호 정규식 체크
     * @param value
     * @return
     */
    public static boolean isKoreaPhone(String value) {
        return Pattern.matches("^0(2|[3-6][0-9]|4[1-4]|5[0-5]|6[0-4]|70)[.-]?(\\d{3,4})[.-]?(\\d{4})$", value);
    }

    /**
     * 이메일 정규식 체크
     *
     * @param value
     */
    public static boolean isEmail(String value) {
        return Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", value);
    }

    public static boolean isUserId(String value) {
        return Pattern.matches("^[a-zA-Z0-9]{6,16}$", value);
    }

    public static boolean isUserPw(String value) {
        return Pattern.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-\\[\\]{};':\"\\\\|,.<>/?]).{8,16}$", value);
    }

    public static boolean isValidDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            throw new CommonLogicException(ApiState.PARAMETER.getCode(), ApiState.PARAMETER.getMessage());
        }
    }

}
