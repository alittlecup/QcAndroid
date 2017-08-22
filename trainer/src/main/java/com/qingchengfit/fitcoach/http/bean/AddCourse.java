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
 * Created by Paper on 15/12/31 2015.
 */
public class AddCourse {
    public int id;
    public String model;
    public String name;
    public String photo;
    public int length;
    public boolean is_private;
    public String course_id;
    public String capacity;

    public AddCourse(int id, String model, String name, String photo, int length, boolean is_private, String capacity) {
        this.model = model;
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.length = length;
        this.is_private = is_private;
        this.capacity = capacity;
    }

    public AddCourse(int id, String model, int course_id) {
        this.id = id;
        this.model = model;
        this.course_id = course_id + "";
    }
}
