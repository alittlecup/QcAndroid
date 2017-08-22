package com.qingchengfit.fitcoach.bean;

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
 * Created by Paper on 16/1/26 2016.
 */
public class UpdateVersion {
    @SerializedName("name") public String name;
    @SerializedName("changelog") public String changelog;
    @SerializedName("installUrl") public String installUrl;
    @SerializedName("direct_install_url") public String direct_install_url;
    @SerializedName("updated_at") public Long updated_at;
    @SerializedName("version") public int version;
}
