package cn.qingchengfit.saasbase.login.bean;

import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.Staff;
import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("session_id") public String session_id;
    @SerializedName("session_name") public String session_name;
    @SerializedName("staff") public Staff staff;
    @SerializedName("user") public Personage user;
}
