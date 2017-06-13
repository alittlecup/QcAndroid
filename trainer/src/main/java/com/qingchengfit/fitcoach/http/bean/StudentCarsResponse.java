package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import java.util.List;

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
 * Created by Paper on 16/1/13 2016.
 */
public class StudentCarsResponse extends QcResponse {
    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("cards") public List<Card> cards;
    }

    public class Card {
        @SerializedName("name") public String name;
        @SerializedName("valid_from")//通用时间
        public String valid_from;
        @SerializedName("users") public String users;
        @SerializedName("id") public String id;
        @SerializedName("account") public String account;
        @SerializedName("type") public int type;    //1储值 2次卡 3 时间卡
        @SerializedName("valid_to") public String valid_to;
        @SerializedName("start") public String start;
        @SerializedName("url") public String url;
        @SerializedName("end") public String end;
        @SerializedName("times") public String times;
        @SerializedName("check_valid") public boolean check_valid;
    }
}
