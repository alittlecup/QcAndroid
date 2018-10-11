package cn.qingchengfit.student.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FollowRecordStatusListWrap {
  @SerializedName("track_status_list")
  List<FollowRecordStatus> statuses;

  public List<FollowRecordStatus> getStatuses() {
    return statuses;
  }
}
