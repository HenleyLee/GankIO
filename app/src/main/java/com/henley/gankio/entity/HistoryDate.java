package com.henley.gankio.entity;

import android.text.TextUtils;

/**
 * 历史日期
 *
 * @author Henley
 * @date 2018/7/5 14:02
 */
public class HistoryDate {

    private String year;
    private String month;
    private String day;

    public static HistoryDate parse(String date) {
        if (TextUtils.isEmpty(date) || !date.contains("-")) {
            return null;
        }
        String[] split = date.split("-");
        HistoryDate historyDate = new HistoryDate();
        historyDate.year = split[0];
        historyDate.month = split[1];
        historyDate.day = split[2];
        return historyDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

}
