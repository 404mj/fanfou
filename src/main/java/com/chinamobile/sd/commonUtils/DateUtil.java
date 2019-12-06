package com.chinamobile.sd.commonUtils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;

import org.springframework.util.StringUtils;

/**
 * <p>
 * Title: DateUtil.java
 * </p>
 * <p>
 * Description: 对日期的处理类，提供一些常用的对日期进行处理的方法
 * </p>
 *
 * @version 1.0
 * @Author: fengchen.zsx
 * @Date: 2019/9/25 9:15
 */
public class DateUtil {

    /**
     * 预定义的日期格式:yyyy-MM-dd HH:mm:ss
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 预定义的日期格式:yyyyMMdd_HHmmss
     */
    public static final String YYYYMMDD_HHMMSS = "yyyyMMdd_HHmmss";

    /**
     * 预定义的日期格式:yyyy-MM-dd HH:mm
     */
    public static final String YYYYMMDD_HHMM = "yyyyMMdd HHmm";

    /**
     * 预定义的日期格式:yyyy-MM-dd
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 预定义的日期格式:yyyy-MM
     */
    public static final String YYYY_MM = "yyyy-MM";

    /**
     * 预定义的日期格式:yyyyMMdd
     */
    public static final String YYYYMMDD = "yyyyMMdd";

    /**
     * 预定义的日期格式:yyyyMM
     */
    public static final String YYYYMM = "yyyyMM";

    /**
     * 预定义的日期格式:yyyy
     */
    public static final String YYYY = "yyyy";

    /**
     * 预定义的日期格式:MM-dd
     */
    public static final String MM_dd = "MM-dd";

    /**
     * 预定义的日期格式:MMdd
     */
    public static final String MMdd = "MMdd";

    /**
     * 预定义的日期格式:MM
     */
    public static final String MM = "MM";

    /**
     * 预定义的日期格式:HH:mm
     */
    public static final String HH_mm = "HH:mm";

    /**
     * 预定义的日期格式:HHmm
     */
    public static final String HHmm = "HHmm";

    /**
     * 几个固定时间点
     */
    public static final LocalTime breakEnd = LocalTime.parse("08:30");
    public static final LocalTime lunchTime = LocalTime.parse("11:30");
    public static final LocalTime lunchEnd = LocalTime.parse("13:00");
    public static final LocalTime dinnerTime = LocalTime.parse("17:30");
    public static final LocalTime dinnerEnd = LocalTime.parse("18:00");


    /**
     * 按指定的格式sFormat将日期dDate转化为字符串，sFormat的取值在类中定义了常量，也可以自己设置字符串，默认为yyyy-MM-dd
     *
     * @param dDate   待转化的日期
     * @param sFormat 格式化指定的格式
     * @return String 格式为sFormat的日期字符串
     */
    public static String date2String(Date dDate, String sFormat) {
        if (dDate == null) {
            return "";
        } else {
            if (StringUtils.isEmpty(sFormat)) {
                sFormat = YYYY_MM_DD;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
            return sdf.format(dDate);
        }
    }

    /**
     * 将字符串按指定格式转换为java.util.Date类型，format的取值在类中定义了常量，默认格式为yyyy-MM-dd HH:mm:ss
     *
     * @param str    待转化的字符串
     * @param format 指定格式
     * @return Date 返回指定格式为format的日期
     */
    public static Date string2Date(String str, String format) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        Date result = null;
        if (StringUtils.isEmpty(format)) {
            return string2Date(str);
        }
        try {
            DateFormat mFormat = new SimpleDateFormat(format);
            result = mFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 字符串转换为java.util.Date类型,按字符串的长度来自动设置格式
     *
     * @param s 待转化的字符串
     * @return Date 按字符串长度设置格式，然后转化为java.util.Date类型
     */
    public static Date string2Date(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        Date result = null;
        try {
            DateFormat format = null;
            if (s.length() > 15) {
                format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            } else if (s.length() > 8) {
                format = new SimpleDateFormat(YYYY_MM_DD);
            } else if (s.length() > 4) {
                format = new SimpleDateFormat(YYYY_MM);
            } else {
                format = new SimpleDateFormat(YYYY);
            }
            result = format.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 取得当前日期的月份，以MM格式返回.
     *
     * @return 当前月份 MM
     */
    public static String getCurrentMonth() {
        return getFormatCurrentTime("MM");
    }

    /**
     * 取得当前日期的年份，以yyyy格式返回.
     *
     * @return 当年 yyyy
     */
    public static String getCurrentYear() {
        return getFormatCurrentTime("yyyy");
    }

    /**
     * 取得当前时间的小时
     *
     * @return MM
     */
    public static String getCurrentHour() {
        return getFormatCurrentTime("HH");
    }

    /**
     * 取得当前时间的分钟
     *
     * @return mm
     */
    public static String getCurrentMinute() {
        return getFormatCurrentTime("mm");
    }


    /**
     * 根据给定的格式，返回时间字符串
     * <p>
     * 参照DateFormator类，是调用了DateFormator类的date2String方法。
     *
     * @param format 日期格式字符串
     * @return String 指定格式的日期字符串.
     */
    public static String getFormatCurrentTime(String format) {
        return date2String(new Date(), format);
    }

    /**
     * 省略掉时间的毫秒，设置millisecond为0
     *
     * @param dDate
     * @return Date
     */
    public static Date trimMillis(Date dDate) {
        if (dDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dDate);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * trimMillis 字符串返回
     *
     * @return
     */
    public static String getCurrentSeconds() {
        return String.valueOf((System.currentTimeMillis() / 1000));
    }

    /**
     * 返回当天的日期
     *
     * @return "YYYYMMDD"
     */
    public static String getToday() {
        return DateUtil.date2String(new Date(), DateUtil.YYYYMMDD);
    }

    /**
     * 返回当天的日期
     *
     * @return "YYYY-MM-DD"
     */
    public static String getTodayWithSlash() {
        return DateUtil.date2String(new Date(), DateUtil.YYYY_MM_DD);
    }

    /**
     * 返回当天的日期
     *
     * @return "YYYYMMDD"
     */
    public static String getTodayDate() {
        return DateUtil.date2String(new Date(), DateUtil.YYYYMMDD);
    }


    /**
     * @return
     */
    public static String[] getCurrentWeekFirstLastDay() {
        String[] days = new String[2];
        final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.CHINA).getFirstDayOfWeek();
        final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1).plus(1);
        days[0] = LocalDate.now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek.plus(1))).toString();
        days[1] = LocalDate.now().with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).toString();
        System.out.println(days[0]);
        return days;
    }

    /**
     * @param time
     * @return
     */
    public static boolean isWorkDate(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        Integer weekTh = dayOfWeek.getValue();
        if (weekTh >= 1 && weekTh <= 5) {
            return true;
        }
        return false;
    }

    /**
     * @param secondts
     * @return
     */
    public static String parseTimestamp2String(long secondts) {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochSecond(secondts), TimeZone.getDefault().toZoneId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        return time.format(formatter);
    }

}
