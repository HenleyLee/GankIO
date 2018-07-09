package com.liyunlong.gankio.utils;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 *
 * @author liyunlong
 * @date 2018/7/5 14:56
 */
public class DateHelper {

    /**
     * 获取目标时间和当前时间之间的差距
     *
     * @param date date
     * @return String
     */
    public static String getTimestampString(Date date) {
        Date curDate = new Date();
        long splitTime = curDate.getTime() - date.getTime();
        if (splitTime < DateUtils.MINUTE_IN_MILLIS) {
            return "刚刚";
        }
        if (splitTime < DateUtils.HOUR_IN_MILLIS) {
            return String.format(Locale.getDefault(), "%d分钟前", splitTime / DateUtils.MINUTE_IN_MILLIS);
        }

        if (splitTime < DateUtils.DAY_IN_MILLIS) {
            return String.format(Locale.getDefault(), "%d小时前", splitTime / DateUtils.HOUR_IN_MILLIS);
        }
        if (splitTime < (30 * DateUtils.DAY_IN_MILLIS)) {
            return String.format(Locale.getDefault(), "%d天前", splitTime / DateUtils.DAY_IN_MILLIS);
        }
        String format = "M月d日 HH:mm";
        return (new SimpleDateFormat(format, Locale.CHINA)).format(date);
    }

    /**
     * 将Date格式的日期转为字符串格式的日期
     *
     * @param time   time
     * @param format format
     */
    public static String date2String(long time, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(time);
    }

}
