package cn.qingchengfit.saasbase.login.bean;

import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.Staff;
import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("session_id") public String session_id;
    @SerializedName("session_name") public String session_name;
    @SerializedName("session_domain") public String session_domain;
    @SerializedName("staff") public Staff staff;
    @SerializedName("coach") public Staff coach;
    @SerializedName("user") public Personage user;
    public String wechat_openid;
}
