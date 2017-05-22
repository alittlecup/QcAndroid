package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.model.base.CoachService;
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
 * Created by Paper on 15/10/15 2015.
 */
public class QcAllStudentResponse extends QcResponse {

    @SerializedName("data")
    public Ship data;

    public static class ShipData {
        @SerializedName("services")
        public List<Ship> ships;
    }

    public static class Ship {

        @SerializedName("ships")
        public List<QcStudentBean> users;
        @SerializedName("service")
        public CoachService service;
    }
}
