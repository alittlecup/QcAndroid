package cn.qingchengfit.saasbase.report.bean;

import com.google.gson.annotations.SerializedName;

public class StatementGlanceResp {

    @SerializedName("week") public StatementGlance week;
    @SerializedName("today") public StatementGlance today;
    @SerializedName("month") public StatementGlance month;
}