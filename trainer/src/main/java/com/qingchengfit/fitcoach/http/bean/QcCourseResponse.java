package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import java.util.List;

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
 * Created by Paper on 15/10/14 2015.
 */
public class QcCourseResponse extends QcResponse {

    @SerializedName("data") public Courses data;

    public class Courses {
        @SerializedName("courses") public List<Course> courses;
    }

    public class Course {
        @SerializedName("id") public int id;
        @SerializedName("name") public String name;
        @SerializedName("photo") public String photo;
    }
}
