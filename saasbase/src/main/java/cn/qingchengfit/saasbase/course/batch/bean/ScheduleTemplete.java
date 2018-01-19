package cn.qingchengfit.saasbase.course.batch.bean;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ScheduleTemplete {

    @SerializedName("max_users") public int max_users;

    @SerializedName("time_repeats") public ArrayList<Time_repeat> time_repeats;

    @SerializedName("rule") public ArrayList<Rule> rule;

    public boolean is_free;
}