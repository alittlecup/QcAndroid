package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.common.Course;
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
 * Created by Paper on 16/4/29 2016.
 */
public class QcResponseRealcardHistory extends QcResponse {

    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("total_count") public int total_count;
        @SerializedName("pages") public int pages;
        @SerializedName("current_page") public int current_page;
        @SerializedName("stat") public Stat stat;
        @SerializedName("card_histories") public List<CardHistory> card_histories;
    }

    public class Stat {
        @SerializedName("total_cost") public float total_cost;
        @SerializedName("total_account") public float total_account;
    }

    public class CardHistory {
        @SerializedName("created_at") public String created_at;
        @SerializedName("created_by_name") public String created_by_name;
        @SerializedName("account") public String account;

        @SerializedName("order") public Order order;
        @SerializedName("cost") public String cost;
        @SerializedName("remarks") public String remarks;
        @SerializedName("type") public String type;
        @SerializedName("type_int") public int type_int;
        @SerializedName("photo") public String photo;
        @SerializedName("id") public String id;
    }

    public class Order {
        @SerializedName("username") public String username;
        @SerializedName("count") public String count;
        @SerializedName("start") public String start;
        @SerializedName("course") public Course course;
    }
}
