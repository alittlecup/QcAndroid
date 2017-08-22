package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.Course;
import com.google.gson.annotations.SerializedName;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //上课记录
 * //Created by yangming on 16/11/22.
 */

public class Schedule {
    @SerializedName("start") public String start;
    @SerializedName("end") public String end;
    @SerializedName("url") public String url;
    @SerializedName("id") public long id;
    @SerializedName("course") public Course course;
    @SerializedName("teacher") public Staff teacher;
}
