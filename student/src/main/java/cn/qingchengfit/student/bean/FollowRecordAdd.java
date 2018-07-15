package cn.qingchengfit.student.bean;

import cn.qingchengfit.model.base.User;
import java.util.List;

public class FollowRecordAdd {
  String id;
  String model;
  String content;
  String track_type_id;
  String track_status_id;
  List<User> notice_users;
  String next_track_time;
  List<Attach> attachments;//附件列表，图片

}
