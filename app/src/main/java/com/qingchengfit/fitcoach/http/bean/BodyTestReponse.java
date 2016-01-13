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
 * Created by Paper on 16/1/13 2016.
 */
public class BodyTestReponse extends QcResponse {

    @SerializedName("data")
    public Data data;

    public class Data{
        @SerializedName("measures")
        public List<BodyTestMeasure> measures;
        @SerializedName("current_page")
        public String current_page;
        @SerializedName("pages")
        public String pages;
    }



    public class BodyTestMeasure{
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("id")

        public String id;
    }
}
