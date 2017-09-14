package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.usecase.bean.QcScheduleBean;
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
 * Created by Paper on 15/10/12 2015.
 */
public class QcSchedulesResponse extends QcResponse {

    @SerializedName("data") public Data data;

    private QcSchedulesResponse() {
    }

    public static QcSchedulesResponse newInstance() {
        QcSchedulesResponse qcMyhomeResponse = new QcSchedulesResponse();
        return qcMyhomeResponse;
    }

    public static class Data {
        @SerializedName("rests") public List<Rest> rests;
        @SerializedName("shop") public QcScheduleBean.Shop shop;
        @SerializedName("schedules") public List<QcScheduleBean> schedules;
    }

    public static class Service {
        @SerializedName("rests") public List<Rest> rests;
        @SerializedName("service") public CoachService system;
        @SerializedName("schedules") public List<QcScheduleBean> schedules;
    }

    public static class Rest {
        @SerializedName("url") public String url;
        @SerializedName("start") public String start;
        @SerializedName("end") public String end;
        @SerializedName("teacher") public Staff teacher;
        @SerializedName("shop") public Shop shop;
        //        @SerializedName("service")
        //        public CoachService service;
    }
}
