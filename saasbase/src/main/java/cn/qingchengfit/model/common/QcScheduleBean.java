package cn.qingchengfit.model.common;

import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.Staff;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QcScheduleBean {
    @SerializedName("count") public int count;
    @SerializedName("end") public String end;
    @SerializedName("start") public String start;
    @SerializedName("id") public int id;
    @SerializedName("url") public String url;
    @SerializedName("shop") public Shop shop;
    @SerializedName("course") public Course course;
    @SerializedName("users") public String users;
    @SerializedName("orders") public List<Order> orders;
    @SerializedName("teacher") public Staff teacher;


    public static class Order {
        @SerializedName("count") public int count;
        @SerializedName("username") public String username;
    }


}