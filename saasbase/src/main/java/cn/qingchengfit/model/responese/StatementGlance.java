package cn.qingchengfit.model.responese;

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
 * //
 * //Created by yangming on 16/11/28.
 */

public class StatementGlance {
    @SerializedName("from_date") public String from_date;
    @SerializedName("to_date") public String to_date;
    @SerializedName("total_cost") public float total_cost;

    @SerializedName("user_count") public int user_count;
    @SerializedName("order_count") public int order_count;
    @SerializedName("course_count") public int course_count;
    @SerializedName("total_count") public int total_count;
}
