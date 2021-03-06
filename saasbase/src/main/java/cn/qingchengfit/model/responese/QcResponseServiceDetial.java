package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.network.response.QcResponse;
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
 * Created by Paper on 15/12/30 2015.
 */
public class QcResponseServiceDetial extends QcResponse {
    @SerializedName("data") public ServiceDetailData data;

    public static class ServiceDetailData {
        @SerializedName("service") public ServiceDetail service;
    }

    public static class ServiceDetail {
        @SerializedName("model") public String model;
        @SerializedName("type") public int type;
        @SerializedName("id") public long id;
        @SerializedName("name") public String name;
        @SerializedName("color") public String color;
        @SerializedName("photo") public String photo;
        @SerializedName("courses_count") public int courses_count;
        @SerializedName("users_count") public int users_count;

        @SerializedName("courses") public List<CourseTypeSample> courses;

        @SerializedName("users") public List<StudentBean> users;
    }
}
