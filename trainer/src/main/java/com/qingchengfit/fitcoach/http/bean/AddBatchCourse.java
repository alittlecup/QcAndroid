package com.qingchengfit.fitcoach.http.bean;

import java.util.List;

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
 * Created by Paper on 16/1/8 2016.
 */
public class AddBatchCourse {
    public String model;
    public String id;
    public String course_id;
    public String from_date;
    public String to_date;
    public List<WeekTime> time_repeats;

    public static class WeekTime {
        public int weekday;
        public String start;
        public String end;
    }
}
