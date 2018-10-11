package cn.qingchengfit.student.bean;

import java.util.UUID;

public class FollowRecordStatus {
  String track_status;
  String id;
  public FollowRecordStatus(String track_status) {
    this.track_status = track_status;
  }

  public String getTrack_status() {
    return track_status;
  }

  public String getId() {
    return id;
  }

  public static FollowRecordStatus mock(){
    FollowRecordStatus s = new FollowRecordStatus(UUID.randomUUID().toString());
    return s;
  }
}
