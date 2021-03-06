package com.qingchengfit.fitcoach.http.bean;

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
 * Created by Paper on 15/12/29 2015.
 */
public class ShopBean {
    @SerializedName("id") public long id;
    @SerializedName("address") public String address;
    @SerializedName("name") public String name;
    @SerializedName("open_time") public List<QcPrivateGymReponse.OpenTime> open_time;
}
