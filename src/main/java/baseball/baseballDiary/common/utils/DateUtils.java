package baseball.baseballDiary.common.utils;

import baseball.baseballDiary.common.enums.CommonEnums;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static baseball.baseballDiary.common.utils.LocalDateUtil.ZONE_KST;
import static baseball.baseballDiary.common.utils.LocalDateUtil.ZONE_UTC;
import static baseball.baseballDiary.common.utils.ValidateUtils.isValidDate;

public class DateUtils {

    private static final String format_6 = "yyyy-MM-dd";
    private static final String format_10 = "yyyy-MM-dd HH:mm";
    private static final String format_12 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 두 날짜의 월차를 계산하면 1월 미만은 0월로 계산한다.
     * 날짜는 앞뒤 순서가 없고, 시간은 계산에 참여하지 않는다.
     *
     * @param time1
     * @param time2
     * @param format
     * @return
     */
    public static int getMonthSub(String time1, String time2, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar before = Calendar.getInstance();
        Calendar after = Calendar.getInstance();
        Date beforeDate = null;
        Date afterDate = null;
        try {
            beforeDate = sdf.parse(time1);
            afterDate = sdf.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (beforeDate.compareTo(afterDate) > 0) {
            before.setTime(afterDate);
            after.setTime(beforeDate);
        } else if (beforeDate.compareTo(afterDate) < 0) {
            before.setTime(beforeDate);
            after.setTime(afterDate);
        } else {
            return 0;
        }
        int surplus = after.get(Calendar.DATE) - before.get(Calendar.DATE);
        int result = after.get(Calendar.MONTH) - before.get(Calendar.MONTH);
        int month = (after.get(Calendar.YEAR) - before.get(Calendar.YEAR)) * 12;
        surplus = surplus < 0 ? -1 : 0;
        return Math.abs(month + result) + surplus;
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String asString(String pattern, LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
    }

    /**
     * date to string
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String dateToStr(Date date, String dateFormat) {
        if (date == null) {
            return "";
        }
        if (!StringUtils.isEmpty(dateFormat)) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.format(date);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
    }

    /**
     * 시간이 시간 구간에 있는지 아닌지를 판단하다
     *
     * @param startDate
     * @param endDate
     * @param currentDate
     * @return
     */
    public static boolean isInRange(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime currentDate) {

        if (ObjectUtils.isEmpty(endDate)) {
            return currentDate.isAfter(startDate);
        }

        return currentDate.isBefore(endDate) && currentDate.isAfter(startDate);
    }

    public static Date addYear(Date startDate, int years) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

    public static Date addDay(Date startDate, int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date addMinute(Date startDate, int Minute) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        calendar.add(Calendar.MINUTE, Minute);
        return calendar.getTime();
    }

    public static Date addSecond(Date startDate, int Second) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        calendar.add(Calendar.SECOND, Second);
        return calendar.getTime();
    }

    public static  Date parse(String strDate){

        String format = null;
        int index = strDate.indexOf(":");
        if (index < 0) {
            format = format_6;

        } else {
            int lastIndex = strDate.lastIndexOf(":");
            if (index == lastIndex) {
                format = format_10;
            } else {
                format = format_12;
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static  Date parse(String strDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 년령 계산
     */
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //금일 이후 계산불가
            throw new IllegalArgumentException(
                "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //금년도
        int monthNow = cal.get(Calendar.MONTH);  //금월
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //금일
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;   //나이 계산
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//해당일자가 생일전 이면, 삭감 1
            } else {
                age--;//해당월이 생일전 이면, 삭감 1
            }
        }
        return age;
    }



    //지난주 월요일
    public static LocalDateTime geLastWeekMonday(LocalDateTime date) {

        LocalDateTime withMONDAY = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime localDateTime = withMONDAY.minusDays(7);
        return localDateTime;
    }

    //다음주 월요일
    public static Date getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    //금주 월요일
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 금일이 일주일중 몇일째인지 확인
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 1주일의 첫날을 설정, 일주일이 습관인 첫째 날은 월요일
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 금일이 일주일중 몇일째인지 확인
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 달력의 규칙에 따라, 현재 날짜에 요일과 일주일의 첫째 날의 차이를 빼다
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    /**
     * 주 계산
     */
    public static int getWeekNum(LocalDateTime date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        return dayOfWeek;
    }

    /**
     * 월 계산
     */
    public static int getMonthNum(LocalDateTime date) throws ParseException {
        int month = date.getDayOfMonth();
        return month;
    }

    /**
     * 년 계산
     */
    public static int getYearNum(LocalDateTime date) throws ParseException {
        int dayOfYear = date.getDayOfYear();
        return dayOfYear;
    }

    /**
     * 18비트 무작위 타임스탬프 생성
     *
     * @return
     */
    public static String dateRandom18() {

        //현재 연도, 월, 일을 가져옵니다.
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        Date date_now = asDate(LocalDateUtil.getLocalDateByZone());
        String date = yyyyMMdd.format(date_now);

        //타임스탬프의 마지막 6자리를 가져옵니다.
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String fiveNumber = timeMillis.substring(timeMillis.length() - 6);

        //난수의 마지막 4자리를 구합니다.
        String tempRandom = String.valueOf(Math.random());
        String number = tempRandom.substring(tempRandom.length() - 4);

        //18자리 임의의 타임스탬프 번호 생성
        return date + fiveNumber + number;
    }

//    public static void main(String[] args) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date date = LocalDateUtil.getLocalDateByZoneKST();
//            System.out.println("금일" + sdf.format(date));
//            //System.out.println(" 금주 월요일: " + sdf.format(getThisWeekMonday(date)));
//            //System.out.println("다음주 월요일: " + sdf.format(getNextWeekMonday(date)));
//            System.out.println("지난주 월요일: " + sdf.format(geLastWeekMonday(date)));
//            System.out.println("지난주 주: " + getWeekNum(geLastWeekMonday(date)));
//            System.out.println("지난주 주-> 몇월: " + getMonthNum(geLastWeekMonday(date)));
//            System.out.println("지난주 주-> 년도:  " + getYearNum(geLastWeekMonday(date)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static int getDayWeek(LocalDate date) {
        // 금일이 일주일중 몇일째인지 확인
        int dayWeek = date.getDayOfWeek().getValue();
        return dayWeek;
    }

    /**
     * 만나이 계산
     *
     * @return
     */
    public static int getAmericanAge(String birthDate) {
        LocalDate now = LocalDate.now();
        LocalDate parsedBirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

        int americanAge = now.minusYears(parsedBirthDate.getYear()).getYear(); // (1)

        // (2)
        // 생일이 지났는지 여부를 판단하기 위해 (1)을 입력받은 생년월일의 연도에 더한다.
        // 연도가 같아짐으로 생년월일만 판단할 수 있다!
        if (parsedBirthDate.plusYears(americanAge).isAfter(now)) {
            americanAge = americanAge -1;
        }

        return americanAge;
    }

    public static LocalDateTime parseLocalDateTimeToUTC(String date, String parse) {
        if (StringUtils.isAnyEmpty(date, parse)) {
            return null;
        }
        Date parseDate = parse(date);
        LocalDateTime localDateTime = DateUtils.asLocalDateTime(parseDate);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZONE_KST).withZoneSameInstant(ZONE_UTC);
        LocalDateTime localDateTime1 = zonedDateTime.toLocalDateTime();
        return localDateTime1;
    }

    public static String addHhMmSs(String date, boolean isStart) {
        if (!org.springframework.util.StringUtils.hasText(date)) {
            return null;
        }
        // 날짜 포맷 검증
        isValidDate(date, CommonEnums.Date.DATE.getPattern());
        // StringBuilder를 사용한 문자열 결합
        return date + (isStart ? " 00:00:00" : " 23:59:59");
    }

    /**
     * 현재랑 비교
     */
    public static boolean isValidateNow(String comparedDate) {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime dateTimeToCompare = LocalDateTime.parse(comparedDate, formatter);

        // 현재 날짜와 시간과 비교
        return !now.isAfter(dateTimeToCompare);
    }

}
