package com.chinamobile.sd.commonUtils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

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
     * 年月日定义
     */
    private static final String REF_HOUR = "hour";
    private static final String REF_DAY = "day";
    private static final String REF_MONTH = "month";
    private static final String REF_YEAR = "year";
    private static final String REF_WEEK = "week";

    /**
     * 转换MM-dd和yyyy-MM定义长度
     */
    private static final Integer MMDD_LEN = 5;
    private static final Integer YYYYMM_LEN = 7;

    /**
     * 获取以前的某个日期
     *
     * @param currentDate
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getDate(Date currentDate, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        return getDate(year, month, day, cal);
    }

    private static Date getDate(int year, int month, int day, Calendar cal) {
        cal.set(cal.YEAR, cal.get(Calendar.YEAR) + year);
        cal.set(cal.MONTH, cal.get(Calendar.MONTH) + month);
        cal.set(cal.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + day);
        return cal.getTime();
    }


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
     * 返回当天所在的年月
     *
     * @return String "yyyyMM"
     */
    public static String getCurrentYearMonth() {
        String res = "";
        Calendar caldTmp = Calendar.getInstance();
        caldTmp.setTime(new Date());
        if (caldTmp.get(Calendar.MONTH) + 1 < 10)
            res = caldTmp.get(Calendar.YEAR) + "0"
                    + (caldTmp.get(Calendar.MONTH) + 1);
        else
            res = caldTmp.get(Calendar.YEAR) + ""
                    + (caldTmp.get(Calendar.MONTH) + 1);
        return res;
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
     * 对当前时间，取向前（为负值时向后）多少秒
     *
     * @param dInput         输入时间
     * @param numberOfSecond 偏移的秒数
     * @return Date 结果时间
     */
    public static Date addSecond(Date dInput, int numberOfSecond) {
        if (dInput == null) {
            return null;
        }
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(dInput);
        c.add(java.util.Calendar.SECOND, numberOfSecond);
        return c.getTime();
    }

    /**
     * 取得前后day天数的日期,day为负数表示以前的日期
     *
     * @param date
     * @param day
     * @return
     */
    public static Date nextDate(Date date, int day) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);
        return calendar.getTime();
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
     * 返回当天的日期
     *
     * @return "YYYYMMDD"
     */
    public static String getToday() {
        return DateUtil.date2String(new Date(), DateUtil.YYYYMMDD);
    }

    public static String getTodayDate() {
        return DateUtil.date2String(new Date(), DateUtil.YYYYMMDD);
    }

    /**
     * 根据所给的起始时间,间隔天数来计算终止时间
     *
     * @param date
     * @param step
     * @return 终止时间
     */
    public static java.sql.Date getStepDay(java.sql.Date date, int step) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, step);
        return new java.sql.Date(calendar.getTime().getTime());
    }

    /**
     * 得到将date增加指定月数后的date
     *
     * @param date
     * @param intBetween
     * @return date加上intBetween月数后的日期
     */
    public static java.sql.Date getStepMonth(Date date, int intBetween) {
        Calendar calo = Calendar.getInstance();
        calo.setTime(date);
        calo.add(Calendar.MONTH, intBetween);
        return new java.sql.Date(calo.getTime().getTime());
    }

    public static Date stringTimeToDate(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return new Date(Long.parseLong(value));
    }

    public static String getCurrentSeconds() {
        return String.valueOf((System.currentTimeMillis() / 1000));
    }

}
