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

    String position;
    String description;
    String group_course;
    String group_user;
    String private_course;
    String private_user;
    String sale;
    int gym_id;

    public AddWorkExperience(int coach_id) {
        this.coach_id = coach_id;
        this.gym_id = 0;
    }

    public AddWorkExperience(int coach_id, int gym_id, String start, String end, String position, String description, String group_course, String group_user, String private_course, String private_user, String sale) {
        this.coach_id = coach_id;
        this.start = start;
        this.end = end;
        this.gym_id = gym_id;

        this.position = position;
        this.description = description;
        this.group_course = group_course;
        this.group_user = group_user;
        this.private_course = private_course;
        this.private_user = private_user;
        this.sale = sale;
    }

    public int getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(int coach_id) {
        this.coach_id = coach_id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroup_course() {
        return group_course;
    }

    public void setGroup_course(String group_course) {
        this.group_course = group_course;
    }

    public String getGroup_user() {
        return group_user;
    }

    public void setGroup_user(String group_user) {
        this.group_user = group_user;
    }

    public String getPrivate_course() {
        return private_course;
    }

    public void setPrivate_course(String private_course) {
        this.private_course = private_course;
    }

    public String getPrivate_user() {
        return private_user;
    }

    public void setPrivate_user(String private_user) {
        this.private_user = private_user;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public int getGym_id() {
        return gym_id;
    }

    public void setGym_id(int gym_id) {
        this.gym_id = gym_id;
    }
}
