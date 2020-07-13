package com.ctfww.commonlib.utils;

import com.haibin.calendarview.Calendar;

import java.util.HashMap;

public class CalendarUtils {
    public static Calendar produceCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        //如果单独标记颜色、则会使用这个颜色
        calendar.setSchemeColor(color);
        calendar.setScheme(text);

        return calendar;
    }

    public static Calendar produceCalendar(int year, int month, int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);

        return calendar;
    }

    public static Calendar produceCalendar(int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setSchemeColor(color);
        calendar.setScheme(text);

        return calendar;
    }

    public static Calendar produceCalendar(long timeStamp) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        return produceCalendar(calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH) + 1, calendar.get(java.util.Calendar.DAY_OF_MONTH));
    }

    public static Calendar produceCalendar() {
        long timeStamp = System.currentTimeMillis();
        return produceCalendar(timeStamp);
    }

    public static HashMap<String, Calendar> getTodayFill() {
        HashMap<String, Calendar> hashMap = new HashMap<>();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        Calendar key = produceCalendar();
        Calendar value = produceCalendar(0xFF94C5FE, "今");
        hashMap.put(key.toString(), value);

        return hashMap;
    }

    public static HashMap<String, Calendar> appendTodayFill(HashMap<String, Calendar> hashMap, int year, int month) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int todayYear = calendar.get(java.util.Calendar.YEAR);
        if (todayYear != year) {
            return hashMap;
        }

        int todayMonth = calendar.get(java.util.Calendar.MONTH);
        if (todayMonth != month) {
            return hashMap;
        }

        Calendar key = produceCalendar();
        Calendar value = produceCalendar(0xFF288CFE, "今");
        hashMap.put(key.toString(), value);

        return hashMap;
    }
}
