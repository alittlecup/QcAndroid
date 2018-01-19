package cn.qingchengfit.saasbase.course.course.network.response;

import cn.qingchengfit.saasbase.course.course.bean.CourseTeacher;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.model.base.Shop;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CourseTeacherWrapper {
    @SerializedName("teachers") public List<CourseTeacher> teachers;
    @SerializedName("course") public CourseType courseDetail;
    @SerializedName("shop") public Shop shop;
}