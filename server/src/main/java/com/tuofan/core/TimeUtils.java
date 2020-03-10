package com.tuofan.core;

import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    public static Date month(Integer before) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, -before);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date day(int before) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -before);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }
}
