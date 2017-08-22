package com.qingchengfit.fitcoach.bean;

import java.util.Date;

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
 * Created by Paper on 15/10/13 2015.
 */
public class StatementBean {

    public Date date;
    public String picture;
    public String title;
    public String content;
    public boolean header;
    public boolean bottom;
    public boolean year;
    public String url;

    public StatementBean() {
    }

    public StatementBean(Date date, String picture, String title, String content, boolean header, boolean bottom, boolean year,
        String url) {
        this.date = date;
        this.picture = picture;
        this.title = title;
        this.content = content;
        this.header = header;
        this.bottom = bottom;
        this.year = year;
        this.url = url;
    }
}
