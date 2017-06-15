package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.common.Card;
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
 * Created by Paper on 15/12/31 2015.
 */
public class QcResponseCards extends QcResponse {
    @SerializedName("data") public CardData data;

    public static class CardData {
        @SerializedName("systems") public List<CardSystem> systems;
    }

    public static class CardSystem {
        @SerializedName("card_tpls") public List<Card> card_tpls;
        @SerializedName("system_id") public int system_id;
    }

    //    public class User {
    //        @SerializedName("phone")
    //        public String phone;
    //        @SerializedName("id")
    //        public String id;
    //        @SerializedName("username")
    //        public String name;
    //    }
}
