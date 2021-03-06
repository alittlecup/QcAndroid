package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.network.response.QcResponse;
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
 * Created by Paper on 16/1/9 2016.
 */
public class QcOneCourseResponse extends QcResponse {
    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("course") public Course course;
    }

    public class Course {
        @SerializedName("length") public int length;
        @SerializedName("photo") public String photo;
        @SerializedName("name") public String name;
        @SerializedName("is_private") public boolean is_private;
        @SerializedName("capacity") public String capacity;
        @SerializedName("id") public String id;
    }
}
