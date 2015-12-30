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
 * Created by Paper on 15/9/18 2015.
 */
public class AddGymPostBean {
    @SerializedName("name")
    public String name;
    @SerializedName("district_id")
    public int district_id;
    @SerializedName("contact")
    public String contact;
    @SerializedName("description")
    public String description;
    @SerializedName("id")
    public String id;
    @SerializedName("brand_name")
    public String brand_name;
    @SerializedName("photo")
    public String photo;
    @SerializedName("district")
    public QcCoachRespone.DataEntity.CoachEntity.DistrictEntity district;

    public AddGymPostBean(String name, int city, String contact, String description, String brand_name) {
        this.name = name;
        this.district_id = city;
        this.brand_name = brand_name;
        this.contact = contact;
        this.description = description;
    }

}
