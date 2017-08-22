package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.Staff;
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
 * //团课排期
 * //Created by yangming on 16/11/24.
 */

public class GroupCourseSchedule {
    @SerializedName("teacher") public Staff teacher;
    @SerializedName("from_date") public String from_date;
    @SerializedName("to_date") public String to_date;
    @SerializedName("id") public String id;
}
