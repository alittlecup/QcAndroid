package cn.qingchengfit.model.responese;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by yangming on 16/9/5.
 */
public class SignInSchdule {

    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("schedules") private List<SignInTasks.Schedule> schedules;

        public List<SignInTasks.Schedule> getSchedules() {
            return schedules;
        }

        public void setSchedules(List<SignInTasks.Schedule> schedules) {
            this.schedules = schedules;
        }
    }
}
