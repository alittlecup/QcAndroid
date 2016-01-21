package com.qingchengfit.fitcoach.http.bean;

import com.google.gson.annotations.SerializedName;

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
 * Created by Paper on 15/8/13 2015.
 */
public class User {
    @SerializedName("username")
    public String username;

    @SerializedName("phone")
    public String phone;
    @SerializedName("id")
    public String id;
    @SerializedName("city")
    public String city;
    @SerializedName("description")
    public String desc;
    @SerializedName("avatar")
    public String avatar;

    @SerializedName("address")
    public String address;
    @SerializedName("joined_at")
    public String joined_at;
    @SerializedName("hidden_phone")
    public String hidden_phone;
    @SerializedName("date_of_birth")
    public String date_of_birth;

    @SerializedName("gender")
    public int gender;

}
