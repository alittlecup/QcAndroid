package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.common.Course;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/10/14 2015.
 */
public class QcResponseCourse extends QcResponse {

    @SerializedName("data") public Courses data;

    public class Courses {
        @SerializedName("shop") public Shop shop;
        @SerializedName("service") public CoachService service;
    }

    public class Shop {
        @SerializedName("private_url") public String private_url;
        @SerializedName("team_url") public String team_url;
        @SerializedName("user_count") public int user_count;
        @SerializedName("courses_count") public int courses_count;
        @SerializedName("team_count") public int team_count;
        @SerializedName("courses") public List<Course> courses;
    }
}
