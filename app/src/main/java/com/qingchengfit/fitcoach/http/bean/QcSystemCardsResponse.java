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
 * Created by Paper on 15/10/16 2015.
 */
public class QcSystemCardsResponse extends QcResponse {

    @SerializedName("data") public Cards data;

    public static class Cards {
        @SerializedName("card_tpls") public List<Card> card_tpls;
    }

    public static class Card {
        @SerializedName("type_name") public String type_name;
        @SerializedName("description") public String description;
        @SerializedName("name") public String name;
        @SerializedName("type") public int type;
        @SerializedName("id") public int id;
    }
}
