package com.qingchengfit.fitcoach.http.bean;

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
 * Created by Paper on 15/10/3 2015.
 */
public class ScheduleBean {
    public long time;       //时间
    public String color;    //颜色
    public String title;  //课程名称
    public String gymname;//健身房名称
    public int count; //预约人数
    public long timeEnd; //结束时间
    public String pic_url; //图片
    public String intent_url;
    public int type;//0  休息  1:schedule
}
