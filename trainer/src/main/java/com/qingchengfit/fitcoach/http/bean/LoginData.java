package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.model.base.User;
import com.google.gson.annotations.SerializedName;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/8/5 2015.
 */
public class LoginData {
    @SerializedName("session_id") public String session_id;
    @SerializedName("session_name") public String session_name;
    @SerializedName("user") public User user;
    @SerializedName("coach") public Coach coach;
}
