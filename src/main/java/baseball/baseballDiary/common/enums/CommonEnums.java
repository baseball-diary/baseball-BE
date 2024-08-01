package baseball.baseballDiary.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO : 데이터 더 채우기
 * 공통 Enums
 * 특별 하지 않으면 여기에 모아두기
 * CommonEnums.Regex.DATA_TRIM으로 호출
 */
public enum CommonEnums {
    ;

    @Getter
    @AllArgsConstructor
    public enum Regex {
        DATE_TIME_TRIM("^\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}$"),
        DATE_TRIM("^\\d{8}$"),
        DATE_ISO8601("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$");

        private String pattern;
    }

    @Getter
    @AllArgsConstructor
    public enum Date {
        DATE_TRIM("yyyyMMdd"),
        DATE_TIME_TRIM("yyyyMMddHHmmss"),
        DATE("yyyy-MM-dd"),
        DATE_TIME("yyyy-MM-dd HH:mm:ss");

        private String pattern;
    }
}