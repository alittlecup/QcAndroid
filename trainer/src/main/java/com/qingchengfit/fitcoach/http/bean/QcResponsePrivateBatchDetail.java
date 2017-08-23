package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.TimeRepeat;
import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import com.qingchengfit.fitcoach.bean.BatchOpenRule;
import com.qingchengfit.fitcoach.bean.Rule;
import com.qingchengfit.fitcoach.bean.Space;
import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/5/4 2016.
 */
public class QcResponsePrivateBatchDetail extends QcResponse {

    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("batch") public BatchDetail batch;
    }

    public class BatchDetail {
        @SerializedName("id") public String id;
        @SerializedName("shop") public QcScheduleBean.Shop shop;
        @SerializedName("max_users") public int max_users;
        @SerializedName("from_date") public String from_date;
        @SerializedName("to_date") public String to_date;
        @SerializedName("spaces") public List<Space> spaces;
        @SerializedName("course") public Course course;
        @SerializedName("rule") public List<Rule> rule;
        @SerializedName("teacher") public QcSchedulesResponse.Teacher teacher;
        @SerializedName("time_repeats") public List<TimeRepeat> time_repeats;
        public BatchOpenRule open_rule;
    }
}
