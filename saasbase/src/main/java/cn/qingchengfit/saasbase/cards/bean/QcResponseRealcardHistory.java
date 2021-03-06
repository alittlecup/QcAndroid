package cn.qingchengfit.saasbase.cards.bean;

import cn.qingchengfit.model.base.Course;
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

    public Stat stat;
    public List<CardHistory> card_histories;
    public int pages;

    public class Stat {
        @SerializedName("total_cost") public float total_cost;
        @SerializedName("total_account") public float total_account;
    }

    public class CardHistory {
        @SerializedName("created_at") public String created_at;
        @SerializedName("created_by_name") public String created_by_name;
        @SerializedName("account") public String account;

        public int card_type;
        @SerializedName("order") public Order order;
        @SerializedName("cost") public float cost;
        @SerializedName("remarks") public String remarks;
        @SerializedName("type") public String type;
        @SerializedName("type_int") public int type_int;
        @SerializedName("photo") public String photo;
        @SerializedName("id") public String id;
        public String start;
        public String end;
    }

    public class Order {
        @SerializedName("username") public String username;
        @SerializedName("count") public String count;
        @SerializedName("start") public String start;
        @SerializedName("course") public Course course;
    }
}
