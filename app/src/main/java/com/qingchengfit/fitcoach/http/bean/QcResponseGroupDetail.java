package com.qingchengfit.fitcoach.http.bean;

import com.google.gson.annotations.SerializedName;
import com.qingchengfit.fitcoach.bean.base.Course;

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
 * Created by Paper on 16/4/30 2016.
 */
public class QcResponseGroupDetail extends QcResponse {

    @SerializedName("data")
    public Data data;


    public class Data {
        @SerializedName("course")
        public Course course;
        @SerializedName("batches")
        public List<GroupBatch> batches;
    }


    public class GroupBatch {
        @SerializedName("teacher")
        public QcSchedulesResponse.Teacher teacher;
        @SerializedName("from_date")
        public String from_date;
        @SerializedName("to_date")
        public String to_date;
        @SerializedName("id")
        public String id;


    }


}
