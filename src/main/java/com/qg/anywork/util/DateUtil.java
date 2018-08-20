package com.qg.anywork.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ming
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
