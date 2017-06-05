package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.model.base.Course;
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
 * Created by Paper on 16/4/30 2016.
 */
public class QcResponsePrivateDetail extends QcResponse {

    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("coach") public PrivateCoach coach;
        @SerializedName("batches") public List<PrivateBatch> batches;
    }

    public class PrivateCoach {
        @SerializedName("username") public String username;
        @SerializedName("user_id") public String user_id;
        @SerializedName("courses_count") public String course_count;
        @SerializedName("users_count") public String users_count;
        @SerializedName("avatar") public String avatar;
        @SerializedName("id") public String id;
    }

    public class PrivateBatch {
        @SerializedName("course") public Course course;
        @SerializedName("from_date") public String from_date;
        @SerializedName("to_date") public String to_date;
        @SerializedName("id") public String id;
    }
}
