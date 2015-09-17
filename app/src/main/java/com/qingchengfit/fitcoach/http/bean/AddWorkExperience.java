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
 * Created by Paper on 15/9/17 2015.
 */
public class AddWorkExperience {
    int coach_id;
    String start;
    String end;
    String city;
    String name;
    String position;
    String description;
    String group_course;
    String group_user;
    String private_course;
    String private_user;
    String sale;

    public AddWorkExperience(int coach_id, String start, String end, String city, String name, String position, String description, String group_course, String group_user, String private_course, String private_user, String sale) {
        this.coach_id = coach_id;
        this.start = start;
        this.end = end;
        this.city = city;
        this.name = name;
        this.position = position;
        this.description = description;
        this.group_course = group_course;
        this.group_user = group_user;
        this.private_course = private_course;
        this.private_user = private_user;
        this.sale = sale;
    }

}
