package com.qingchengfit.fitcoach.http;

import cn.qingchengfit.model.base.TimeRepeat;
import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import com.qingchengfit.fitcoach.bean.BatchOpenRule;
import com.qingchengfit.fitcoach.bean.Rule;
import java.util.ArrayList;

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
 * Created by Paper on 16/5/9 2016.
 */
public class QcResponseBtachTemplete extends QcResponse {

    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("max_users") public int max_users;

        @SerializedName("time_repeats") public ArrayList<TimeRepeat> time_repeats;

        @SerializedName("rule") public ArrayList<Rule> rule;
        public BatchOpenRule open_rule;
    }
}
