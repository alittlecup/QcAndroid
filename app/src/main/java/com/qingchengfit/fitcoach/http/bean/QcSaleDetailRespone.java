package com.qingchengfit.fitcoach.http.bean;

import android.support.annotation.Nullable;
import cn.qingchengfit.model.base.User;
import com.google.gson.annotations.SerializedName;
import com.qingchengfit.fitcoach.bean.Card;
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
 * Created by Paper on 15/10/13 2015.
 */

public class QcSaleDetailRespone extends QcResponse {
    @SerializedName("data") public DetailData data;

    public static class DetailData {
        @SerializedName("total_cost") public int total_cost;
        @SerializedName("total_account") public int total_account;
        @SerializedName("histories") public List<History> histories;
    }

    public static class History {
        @SerializedName("users") public List<User> users;
        @SerializedName("seller_name") public String seller_name;
        @SerializedName("created_at") public String created_at;
        @SerializedName("remarks") public String remarks;
        @Nullable @SerializedName("card") public Card card;
        @SerializedName("account") public int account;
        @SerializedName("cost") public int cost;
    }
}
