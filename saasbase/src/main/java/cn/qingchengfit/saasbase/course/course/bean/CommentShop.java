package cn.qingchengfit.saasbase.course.course.bean;

import cn.qingchengfit.utils.StringPropertyable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * 场馆评价中 场馆相关的评价
 */
public class CommentShop implements StringPropertyable{
        @SerializedName("id") public Long id;
        @SerializedName("name") public String name;
        @SerializedName("logo") public String logo;

        @SerializedName("teacher_score") public float teacher_score;
        @SerializedName("course_score") public float course_score;
        @SerializedName("service_score") public float service_score;
        @SerializedName("impressions") public List<TeacherImpression> impressions;

        @Override public String getStringProperty() {
                return name;
        }
}