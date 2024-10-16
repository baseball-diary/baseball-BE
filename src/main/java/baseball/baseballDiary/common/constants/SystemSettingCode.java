package baseball.baseballDiary.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum SystemSettingCode {

    //비밀번호
    PW_EXPIRED_TIME("PW_EXPIRED_TIME"), //비밀번호 변경유지 기간
    FIND_PASS_URL("FIND_PASS_URL"), //비밀번호 찾기 url


    // 세션 토큰 유지 시간(min)
    ACCESS_TOKEN_TIME("10"),
    ACCESS_TOKEN_TIME_LONG(Integer.toString(60 * 24)),

    //회원 로그인 블록 구분
    MEMBER_BLOCK_LOGIN("1"), //차단

    //sns구분(네이버:NAVER,카카오:KAKAO,구글:GOOGLE,애플:APPLE)
    CERT_TYPE_NAVER("NAVER"),
    CERT_TYPE_KAKAO("KAKAO"),
    CERT_TYPE_GOOGLE("GOOGLE"),
    CERT_TYPE_APPLE("APPLE"),

    CERT_GBN_JOIN("JOIN"),
    CERT_GBN_FIND("FIND"),


    // 증감구분(증감:+,차감:-)
    SYMBOL_PLUS("+"),
    SYMBOL_MINUS("-"),

    //파일 상태(임시저장:TEMP,저장확인:SAVE,삭제:DELETE)
    FILE_STATE_TEMP("TEMP"),
    FILE_STATE_SAVED("SAVED"),
    FILE_STATE_DELETE("DELETE"),

    // 파일 유형
    FILE_IMAGE("IMAGE"),
    FILE_VIDEO("VIDEO"),
    FILE_AUDIO("AUDIO"),
    FILE_ETC("ETC"),

    // 리사이즈 유형
    RESIZE_WIDTH("RESIZE_WIDTH"),
    RESIZE_HEIGHT("RESIZE_HEIGHT"),

    //sample
    SYSTEM_XXX_CODE("XX"); //


    private String code;

}
