package cn.qingchengfit.saasbase.course.course.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * 场馆的评价
 */
public class ShopComment {
        @SerializedName("course") public CourseType course;
        @SerializedName("shops") public List<CommentShop> shops;
}