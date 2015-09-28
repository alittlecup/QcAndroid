package com.paper.paperbaselibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static Date formatDateFromServer(String s) {
        s = s.replace("T", " ");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
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

    public static String getDateMonth(Date d) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM", Locale.CHINA);
        return formatter.format(d);
    }


}
