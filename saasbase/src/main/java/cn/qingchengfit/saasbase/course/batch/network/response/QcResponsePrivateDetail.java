package cn.qingchengfit.saasbase.course.batch.network.response;

import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCourse;
import com.google.gson.annotations.SerializedName;
import java.util.List;

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
        @SerializedName("course") public BatchCourse course;
        @SerializedName("from_date") public String from_date;
        @SerializedName("to_date") public String to_date;
        @SerializedName("id") public String id;
    }
}