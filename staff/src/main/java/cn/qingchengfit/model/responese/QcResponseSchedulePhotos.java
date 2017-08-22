package cn.qingchengfit.model.responese;

import cn.qingchengfit.staffkit.usecase.bean.SchedulePhotos;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QcResponseSchedulePhotos extends QcResponse {
    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("total_count") public int total_count;
        @SerializedName("current_page") public int current_page;
        @SerializedName("pages") public int pages;
        @SerializedName("schedules") public List<SchedulePhotos> schedules;
    }
}