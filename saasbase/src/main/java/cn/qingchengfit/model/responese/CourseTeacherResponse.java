package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.Shop;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CourseTeacherResponse {
    @SerializedName("teachers") public List<CourseTeacher> teachers;
    @SerializedName("course") public CourseType courseDetail;
    @SerializedName("shop") public Shop shop;
}