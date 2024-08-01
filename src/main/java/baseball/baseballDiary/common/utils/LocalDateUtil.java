package baseball.baseballDiary.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
public class LocalDateUtil {


    public final static ZoneId ZONE_UTC = ZoneId.of("UTC");
    public final static ZoneId ZONE_KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter formatter_ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String format_6 = "yyyy-MM-dd";
    private static final String format_10 = "yyyy-MM-dd HH:mm";
    private static final String format_12 = "yyyy-MM-dd HH:mm:ss";

    public static String formatKST(LocalDateTime datetime) {
        return formatter.format(ZonedDateTime.of(datetime, ZONE_UTC).withZoneSameInstant(ZONE_KST));
    }

    public static String formatKST(Date datetime) {
        LocalDateTime localDateTime = DateUtils.asLocalDateTime(datetime);
        return formatter.format(ZonedDateTime.of(localDateTime, ZONE_UTC).withZoneSameInstant(ZONE_KST));
    }

    public static String formatKST_ymd(LocalDateTime localDateTime) {
        return formatter_ymd.format(ZonedDateTime.of(localDateTime, ZONE_UTC).withZoneSameInstant(ZONE_KST));
    }

    public static String formatKST_format(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(ZonedDateTime.of(localDateTime, ZONE_UTC).withZoneSameInstant(ZONE_KST));
    }

    public static String formatUTC_format(LocalDateTime datetime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(datetime);
    }

    public static LocalDateTime getLocalDateByZoneKst() {
        return LocalDateTime.now(ZONE_KST);
    }

    public static LocalDateTime getLocalDateByZone() {
        ZoneId zoneId = ZoneId.of("UTC");
        LocalDateTime now = LocalDateTime.now(zoneId);
        return now;
    }

    public static LocalDateTime getLocalDateTimeByZone() {
        ZoneId zoneId = ZoneId.of("UTC");
        LocalDateTime now = LocalDateTime.now(zoneId);
        return now;
    }

    public static String parseStrTimeToZoneUTC(String timestamp) {

        if (StringUtils.isNotEmpty(timestamp)) {

            String format = null;
            int index = timestamp.indexOf(":");
            if (index < 0) {
                format = format_6;

            } else {
                int lastIndex = timestamp.lastIndexOf(":");
                if (index == lastIndex) {
                    format = format_10;
                } else {
                    format = format_12;
                }
            }

            Date parseDate = DateUtils.parse(timestamp, format);
            LocalDateTime localDateTime = DateUtils.asLocalDateTime(parseDate);
            String formatDate = formatter.format(ZonedDateTime.of(localDateTime, ZONE_KST).withZoneSameInstant(ZONE_UTC));
            return formatDate;
        } else {
            return null;
        }
    }
}

