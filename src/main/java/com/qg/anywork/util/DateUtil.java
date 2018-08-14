package com.qg.anywork.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换器
 *
 * @author FunriLy
 * @date 2017/8/19
 * From small beginnings comes great things.
 */
public class DateUtil {

    private static ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static Date longToDate(long longTime) {
        return new Date(longTime);
    }

    public static String format(Date date) {
        return threadLocal.get().format(date);
    }
}
