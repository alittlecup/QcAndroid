package cn.qingchengfit.saasbase.course.course.network.response;

import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import com.google.gson.annotations.SerializedName;

public class CourseTypeWrap {
    @SerializedName("course") public CourseType courseDetail;
}