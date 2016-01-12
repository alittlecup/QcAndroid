package com.paper.paperbaselibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/9/16 2015.
 */
public class DateUtils {
    public static final Long MONTH_TIME = 31 * 24 * 60 * 60 * 1000l;
    public static final Long DAY_TIME = 24 * 60 * 60 * 1000l;
    public static final Long HOUR_TIME = 60 * 60 * 1000l;

    public static Date formatDateFromServer(String s) {
        try {
            s = s.replace("T", " ");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date date = null;
            date = formatter.parse(s);
            return date;
        } catch (ParseException e) {
//            e.printStackTrace();
            return new Date();
        }catch (Exception e){
//            e.printStackTrace();
            return new Date();
        }

    }

    public static Date formatDateFromString(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = null;
        try {
            date = formatter.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date formatDateFromStringDot(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        Date date = null;
        try {
            date = formatter.parse(s);
        } catch (ParseException e) {
        }
        return date;
    }

    public static String getDateDay(Date d) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        return formatter.format(d);
    }

    public static String getServerDateDay(Date d) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return formatter.format(d);
    }
    public static String getServerDateDayAddMonth(Date d,int i) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return formatter.format(addDateMonth(d,i));
    }
    public static String getServerDay(Date d) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return formatter.format(d);
    }

    public static String getOnlyDay(Date d) {

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd", Locale.CHINA);
        return formatter.format(d);
    }


    public static String getDateMonth(Date d) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        return formatter.format(d);
    }

    public static String getTimeHHMM(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return formatter.format(d);
    }
    public static String formatToServer(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        return formatter.format(d)+"T"+formatter2.format(d);
    }

    public static Date formatDateHHMM(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
        Date date = null;
        try {
            date = formatter.parse(s);
        } catch (ParseException e) {
        }
        return date;
    }

    public static String formatDateToServer(String s) {

        return s.replace(".", "-");
    }
    public static Date addDateMonth(Date d,int i){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MONTH,i);
        return c.getTime();
    }
    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return getServerDateDay(c.getTime());
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        return getServerDateDay(c.getTime());
    }

    public static String getEndDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 0);
        return getServerDateDay(c.getTime());
    }
    public static String getEndDayOfMonthNew(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 0);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return getServerDateDay(c.getTime());
    }

    public static String getStartDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return getServerDateDay(c.getTime());
    }
    public static int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK);
        if (i == 1){
            return 7;
        }else return i-2;
    }


    public static long getToadayMidnight() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime().getTime();
    }

    public static long getDayMidnight(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }
    public static long getDayMid(Date d){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }
    public static int getYear(Date d){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.YEAR);
    }
}
