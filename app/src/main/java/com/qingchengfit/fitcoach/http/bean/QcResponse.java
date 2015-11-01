package com.qingchengfit.fitcoach.http.bean;

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
 * <p> http响应基本结构
 * Created by Paper on 15/7/30 2015.
 */
public class QcResponse {
    @SerializedName("status")
    public int status;
    @SerializedName("msg")
    public String msg;
    @SerializedName("info")
    public String info;
//    @SerializedName("data")
//    public String data;
    @SerializedName("level")
    public String level;
    @SerializedName("error_code")
    public String error_code;


}
