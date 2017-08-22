package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.Course;
import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.staffkit.usecase.bean.QcScheduleBean;
import com.google.gson.annotations.SerializedName;
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
 * Created by Paper on 16/4/22 2016.
 */
public class QcResponseBtaches extends QcResponse {

    @SerializedName("data") public Data data;

    public static class Batch {
        @SerializedName("shop") public QcScheduleBean.Shop shop;
        @SerializedName("plan_id") public String plan_id;
        @SerializedName("max_users") public int max_users;
        @SerializedName("from_date") public String from_date;
        @SerializedName("rule") public List<Rule> rule;
        @SerializedName("teacher") public Staff teacher;
        @SerializedName("course") public Course course;
        @SerializedName("space") public List<Space> spaces;
        @SerializedName("time_repeats") public List<Time_repeat> time_repeats;
        @SerializedName("to_date") public String to_date;
        @SerializedName("id") public String id;
    }

    public class Data {

        @SerializedName("total_count") public String total_count;
        @SerializedName("pages") public String pages;
        @SerializedName("current_page") public String current_page;

        @SerializedName("batches") public List<Batch> batches;
    }
}
