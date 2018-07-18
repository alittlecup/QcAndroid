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

  public void setId(String id) {
    this.id = id;
  }

  public void setModel(String model) {
    this.model = model;
  }

  private FollowRecordAdd(Builder builder) {
    id = builder.id;
    model = builder.model;
    content = builder.content;
    track_type_id = builder.track_type_id;
    track_status_id = builder.track_status_id;
    notice_users = builder.notice_users;
    next_track_time = builder.next_track_time;
    attachments = builder.attachments;
  }

  public static final class Builder {
    private String id;
    private String model;
    private String content;
    private String track_type_id;
    private String track_status_id;
    private List<User> notice_users;
    private String next_track_time;
    private List<Attach> attachments;

    public Builder() {
    }

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder model(String val) {
      model = val;
      return this;
    }

    public Builder content(String val) {
      content = val;
      return this;
    }

    public Builder track_type_id(String val) {
      track_type_id = val;
      return this;
    }

    public Builder track_status_id(String val) {
      track_status_id = val;
      return this;
    }

    public Builder notice_users(List<User> val) {
      notice_users = val;
      return this;
    }

    public Builder next_track_time(String val) {
      next_track_time = val;
      return this;
    }

    public Builder attachments(List<Attach> val) {
      attachments = val;
      return this;
    }

    public FollowRecordAdd build() {
      return new FollowRecordAdd(this);
    }
  }
}
