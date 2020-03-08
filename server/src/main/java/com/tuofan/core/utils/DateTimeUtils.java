package com.tuofan.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理帮助类
 */
@Slf4j
public class DateTimeUtils extends DateUtils {
    private static final long MINUTE = 60 * 1000L; // 1分钟

    private static final long HOUR = 60 * MINUTE; // 1小时

    private static final long DAY = 24 * HOUR; // 1天

    private static final long MONTH = 31 * DAY; // 月

    private static final long YEAR = 12 * MONTH; // 年

    /**
     * 默认的时间格式
     */
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_YEAR = "yyyy";

    public static final String DATE_FORMAT_NOJOINER_MONTH = "yyyyMM";

    public static final String DATE_FORMAT_MONTH = "yyyy-MM";

    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";

    public static final String TIME_FORMAT_DEFAULT = "HH:mm:ss";

    public static final String TIME_FORMAT_HOURMINTER = "HH:mm:ss";

    public static String formatTime(Date date) {
        return DateFormatUtils.format(date, TIME_FORMAT_DEFAULT);
    }

    public static String formatTimeHourminter(Date date) {
        return DateFormatUtils.format(date, TIME_FORMAT_HOURMINTER);
    }


    /**
     * 获取当前时间字符串
     *
     * @param date 时间点
     * @return String
     */
    public static String formatDateTime(Date date) {
        return DateFormatUtils.format(date, DATE_FORMAT_DEFAULT);
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDateTime(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime(Date date) {
        return formatDateTime(date, "HH:mm:ss");
    }

    public static String getDate() {
        return formatDateTime(new Date(), DATE_FORMAT_DAY);
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDateTime(new Date());
    }

    /**
     * 获取当天0点的时间字符串
     */
    public static Date getDateFirstTime(Date date) {
        if (null == date) {
            return null;
        }
        FastDateFormat dateFormat = FastDateFormat.getInstance(DATE_FORMAT_DAY);
        return getFormatDate(dateFormat.format(date) + " 00:00:00");
    }

    /**
     * 获取当天23:59:59点的时间字符串
     */
    public static Date getDateEndTime(Date date) {
        if (null == date) {
            return null;
        }
        FastDateFormat dateFormat = FastDateFormat.getInstance(DATE_FORMAT_DAY);
        return getFormatDate(dateFormat.format(date) + " 23:59:59");
    }

    /**
     * 获取当天0点的时间字符串
     */
    public static Date getMonthFirstTime(Date date) {
        if (null == date) {
            return null;
        }
        FastDateFormat dateFormat = FastDateFormat.getInstance(DATE_FORMAT_MONTH);
        return getFormatDate(dateFormat.format(date) + "-01 00:00:00");
    }

    /**
     * 获取当天23:59:59点的时间字符串
     */
    public static Date getMonthEndTime(Date date) {
        Date nextMonthDay = getOffsetMonth(date, 1);
        Date monthFirstTime = getMonthFirstTime(nextMonthDay);
        if (monthFirstTime == null) {
            return null;
        }
        return new Date(monthFirstTime.getTime() - 1);
    }

    /**
     * 获取当天0点的时间字符串
     */
    public static String getDateFirstTimeStr(Date date) {
        FastDateFormat dateFormat = FastDateFormat.getInstance(DATE_FORMAT_DAY);
        return dateFormat.format(date) + " 00:00:00";
    }

    /**
     * 获取指定时间的前一天00:00:00
     */
    public static String getTheDayBefore(String dateStr) {
        return getTheDayBefore(getFormatDate(dateStr, DATE_FORMAT_DEFAULT));
    }

    /**
     * 获取指定时间的前一天00:00:00
     *
     * @param date 指定的时间
     * @return String
     */
    public static String getTheDayBefore(Date date) {
        Date offsetDay = getOffsetDay(date, -1);
        return getDateFirstTimeStr(offsetDay);
    }

    /**
     * 获取指定时间的后几天00:00:00
     *
     * @param date   指定时间
     * @param offset 后第几天
     * @return
     */
    public static String getTheDayAfter(Date date, int offset) {
        Date offsetDay = getOffsetDay(date, offset);
        return getDateFirstTimeStr(offsetDay);
    }

    /**
     * 获取指定时间的前一天00:00:00
     *
     * @param date 指定的时间
     * @return String
     */
    public static Date getOffsetMonth(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, offset);
        return calendar.getTime();
    }

    /**
     * 获取指定时间的前一天00:00:00
     *
     * @param date 指定的时间
     * @return String
     */
    public static Date getOffsetDay(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, offset);
        return calendar.getTime();
    }

    /**
     * 获取指定时间的相对于小时的偏移量
     *
     * @param date 指定的时间
     * @return String
     */
    public static String getOffsetHours(Date date, int offset, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, offset);
        Date dateTemp = calendar.getTime();

        return formatDateTime(dateTemp, dateFormat);
    }

    /**
     * 将date转为yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Date date, String format) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, format);
    }

    public static Date getFormatDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        if (dateStr.trim().length() == 4) {
            return getFormatDate(dateStr, DATE_FORMAT_YEAR);
        }
        if (dateStr.trim().length() == 6) {
            return getFormatDate(dateStr, DATE_FORMAT_NOJOINER_MONTH);
        }
        if (dateStr.trim().length() == 7) {
            return getFormatDate(dateStr, DATE_FORMAT_MONTH);
        }
        if (dateStr.trim().length() == 10) {
            return getFormatDate(dateStr, DATE_FORMAT_DAY);
        } else {
            return getFormatDate(dateStr, DATE_FORMAT_DEFAULT);
        }

    }

    /**
     * 根据指定的时间格式获取时间对象
     *
     * @param dateStr 时间字符串
     * @param format  格式字符串
     * @return 获取到的时间
     */
    public static Date getFormatDate(String dateStr, String format) {
        FastDateFormat dateFormat = FastDateFormat.getInstance(format);
        Date theDate = null;
        try {
            theDate = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            log.error("时间字符串格式错误,dateStr=" + dateStr + ",format=" + format, e);
        }

        return theDate;
    }

    /**
     * 获取指定时间的前一天00:00:00
     */
    public static String getOffsetDay(String dateStr, int offset) {
        Date offsetDay = getOffsetDay(getFormatDate(dateStr, DATE_FORMAT_DEFAULT), offset);
        return getDateFirstTimeStr(offsetDay);
    }

    /**
     * 获取指定时间的相对于时间类型和偏移量的时间
     *
     * @param date       时间对象
     * @param offsetType 偏移的类型
     * @param offset     偏移量
     * @param dateFormat 时间类型
     * @return 时间字符串
     */
    public static String getOffsetTime(Date date, int offsetType, int offset, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(offsetType, offset);
        Date dateTemp = calendar.getTime();

        return formatDateTime(dateTemp, dateFormat);
    }

    /**
     * 获取指定时间的相对于时间类型和偏移量的时间
     *
     * @param date       时间对象
     * @param offsetType 偏移的类型
     * @param offset     偏移量
     * @return 时间字符串
     */
    public static Date getOffsetDate(Date date, int offsetType, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(offsetType, offset);
        return calendar.getTime();
    }

    /**
     * 获取指定时间单位天数偏移量的时间
     *
     * @param offset 偏移量（正表示几天后，负表示几天前）
     * @return 时间字符串
     */
    public static String getOffsetDay(int offset) {
        Date offsetDate = getOffsetDate(new Date(), Calendar.DATE, offset);
        return formatDateTime(offsetDate, DATE_FORMAT_DAY);
    }

    /**
     * 获取指定时间单位月偏移量的时间
     *
     * @param offset 偏移量（正表示几月后，负表示几月前）
     * @return 时间字符串
     */
    public static String getOffsetMonth(int offset) {
        Date offsetDate = getOffsetDate(new Date(), Calendar.MONTH, offset);
        return formatDateTime(offsetDate, DATE_FORMAT_MONTH);
    }

    /**
     * 获取两个日期之间的天数
     */
    public static BigDecimal getDaysOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return new BigDecimal(afterTime - beforeTime).divide(new BigDecimal(DAY));
    }

    /**
     * 得到本日的前几年 如果number=2当日为2007,那么获得2005
     */
    public static String getDateBeforeYear(int number) {
        Date offsetDate = getOffsetDate(new Date(), Calendar.YEAR, number);
        return formatDateTime(offsetDate, DATE_FORMAT_YEAR);
    }

    /**
     * 获取两个日期之间的小时
     */
    public static BigDecimal getHoursOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return new BigDecimal(afterTime - beforeTime).divide(new BigDecimal(HOUR));
    }

    /**
     * 获取两个日期之间的分钟,向下取整
     */
    public static BigDecimal getMinutesOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return new BigDecimal(afterTime - beforeTime).divide(new BigDecimal(MINUTE), BigDecimal.ROUND_DOWN);
    }

    /**
     * 返回系统现在年份中指定月份的天数
     *
     * @param month 月份
     * @return 指定月的总天数
     */
    @SuppressWarnings("deprecation")
    public static int getMonthDayNum(int month) {
        Date date = new Date();
        int year = date.getYear() + 1900;
        return getMonthDayNum(year, month);
    }

    /**
     * 返回指定年份中指定月份的天数
     *
     * @param year  年份
     * @param month 月份
     * @return 指定月的总天数
     */
    public static int getMonthDayNum(int year, int month) {
        int[][] day = {{0, 30, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
                {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}};
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return day[1][month];
        } else {
            return day[0][month];
        }
    }

    /**
     * 判断是平年还是闰年
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400) == 0;
    }

    /**
     * 获得当前时间戳
     */
    public static Timestamp getTimeStamp() {
        Date submitDate = new Date();
        return getTimeStampOfDate(submitDate);
    }

    /**
     * 获得时间戳
     */
    public static Timestamp getTimeStampOfDate(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 获取当前的月份 2016-04
     */
    public static String getCurrentMonthStr() {
        Date currentDate = new Date();
        return formatDateTime(currentDate, DateTimeUtils.DATE_FORMAT_MONTH);
    }

    public static String getTimeFormatText(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > YEAR) {
            r = (diff / YEAR);
            return r + "年前";
        }
        if (diff > MONTH) {
            r = (diff / MONTH);
            return r + "个月前";
        }
        if (diff > DAY) {
            r = (diff / DAY);
            return r + "天前";
        }
        if (diff > HOUR) {
            r = (diff / HOUR);
            return r + "小时前";
        }
        if (diff > MINUTE) {
            r = (diff / MINUTE);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 获取两个日期之间的天数
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (double) DAY;
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 获取过去的天数
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
