package com.guoliang.commonlib.entity;

import com.blankj.utilcode.util.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateTimeUtils {
    static public long date2TimeStamp(String dateStr, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateStr);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
        return date.getTime();
    }

    static public long getTodayStartTime() {
        return getDayStartTime(System.currentTimeMillis());
    }

    static public long getTodayEndTime() {
        return getTodayStartTime() + 24l * 3600l * 1000l - 1;
    }

    static public long getDayStartTime(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        String dateStr = "" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        return date2TimeStamp(dateStr, "yyyy-MM-dd");
    }

    static public long getDayEndTime(long timeStamp) {
        return getDayStartTime(timeStamp) + 24l * 3600l * 1000l - 1;
    }

    static public long getWeekStartTime(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

        // 置为：1为周一、2为周二。。。7为周日
        --weekDay;
        if (weekDay == 0) {
            weekDay = 7;
        }

        // 计算到周一的某个时间
        long mondayTimeStamp = timeStamp - 24l * 3600l * 1000l * (weekDay - 1);

        // 计算到周一的起始时间
        return getDayStartTime(mondayTimeStamp);
    }

    public static long getWeekEndTime(long timeStamp) {
        long startTime = getWeekStartTime(timeStamp);
        return startTime + 7l * 24l * 3600l * 1000l - 1;
    }

    static public long getPresentWeekStartTime() {
        return getWeekStartTime(System.currentTimeMillis());
    }

    public static long getPresentWeekEndTime() {
        return getWeekEndTime(System.currentTimeMillis());
    }

    static public long getMonthStartTime(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 计算到1号的某个时间
        long firstTimeStamp = timeStamp - 24l * 3600l * 1000l * (monthDay - 1);

        // 计算到1号的起始时间
        return getDayStartTime(firstTimeStamp);
    }

    static public long getPresentMonthStartTime() {
        return getMonthStartTime(System.currentTimeMillis());
    }

    public static long getPresentMonthEndTime() {
        long timeStamp = System.currentTimeMillis();

        return getMonthEndTime(timeStamp);
    }

    public static long getMonthEndTime(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        long startTime = getMonthStartTime(timeStamp);
        long days = (long)getMonthLastDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        return startTime + days * 24l * 3600l * 1000l - 1;
    }

    /**
     * 取得当月天数
     **/
    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 得到指定月的天数
     **/
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
