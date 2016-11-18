package com.qingchengfit.fitcoach.http.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.qingchengfit.staffkit.model.bean.CourseDetail;
import cn.qingchengfit.staffkit.model.bean.TeacherImpression;

public class QcResponseShopComment extends QcResponse {
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("scores")
        public ShopComment scores;
    }

    public static class ShopComment {
        @SerializedName("course")
        public CourseDetail course;
        @SerializedName("shops")
        public List<CommentShop> shops;
    }

    public static class CommentShop {
        @SerializedName("id")
        public Long id;
        @SerializedName("name")
        public String name;
        @SerializedName("logo")
        public String logo;

        @SerializedName("teacher_score")
        public float teacher_score;
        @SerializedName("course_score")
        public float course_score;
        @SerializedName("service_score")
        public float service_score;
        @SerializedName("impressions")
        public List<TeacherImpression> impressions;

    }
}