package com.qingchengfit.fitcoach.bean;

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
 * Created by Paper on 15/11/17 2015.
 */
public class MeetingBean {
    public String title;
    public String time;
    public String address;
    public String img;
    public String url;

    public MeetingBean() {
    }

    public MeetingBean(String title, String time, String address, String img) {
        this.title = title;
        this.time = time;
        this.address = address;
        this.img = img;
    }
}
