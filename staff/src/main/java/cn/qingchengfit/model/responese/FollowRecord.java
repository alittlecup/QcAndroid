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
 * //
 * //Created by yangming on 16/11/18.
 */

public class FollowRecord {
    @SerializedName("user") public String user;
    @SerializedName("created_by") public Staff created_by;
    @SerializedName("created_at") public String created_at;
    @SerializedName("id") public long id;
    @SerializedName("content") public String content;
    @SerializedName("type") public String type;
    @SerializedName("link") public String link;
    @SerializedName("thumbnail") public String thumbnail;
}
