package cn.qingchengfit.model.responese;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CourseTypes {
    @SerializedName("courses") public List<CourseType> courses;
}