package com.qingchengfit.fitcoach.reciever;

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
 * Created by Paper on 15/12/10 2015.
 */
public class PushBean {
    //    {"url":"http:\/\/feature2.qingchengfit.cn\/mobile\/schedule\/7031\/details\/","photo":"http:\/\/zoneke-img.b0.upaiyun.com\/dff71b1cca9db02065d2477e4dbd0d0f.jpg","created_at":"2015-12-10T19:53:36","read_at":"","sender":"Let`s Move Test"
    @SerializedName("url") public String url;
    @SerializedName("notification_id") public int notification_id;
}
