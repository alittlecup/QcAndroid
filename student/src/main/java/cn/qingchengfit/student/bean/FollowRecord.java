package cn.qingchengfit.student.bean;

import android.content.Context;
import android.text.TextUtils;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.student.bingdings.BindingUtils;
import cn.qingchengfit.utils.DateUtils;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FollowRecord {
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  String id;
  String content;
  String track_type_id;
  String track_status;
  List<String> notice_users;
  String next_track_time;
  @SerializedName(value = "attachments") List<Attach> image_attachments;//附件列表，图片
  User created_by;
  String created_at;
  String naire_answer_history_id;

  public String getShop() {
    return shop;
  }

  String shop;


  public String getCreatedTimeStr() {
    return DateUtils.getNotifacationTimeStr(DateUtils.formatDateFromServer(created_at));
  }

  public String getTrack_type_id() {
    return track_type_id;
  }

  public String getNaire_answer_history_id() {
    return naire_answer_history_id;
  }

  public String getFollowMethodString(Context context) {

    try {
      int x = Integer.parseInt(track_type_id);
      return BindingUtils.getFollowRecordMethod(x, context);
    } catch (Exception e) {
      return "--";
    }
  }

  public String getImagePos(int pos) {
    if (image_attachments != null && pos < image_attachments.size()) {
      return image_attachments.get(pos).getLink();
    } else {
      return "";
    }
  }

  public String getFollowStatusString() {
    if (TextUtils.isEmpty(track_status)){
      return "";
    }
    return track_status;
  }

  public String getNotiOthers() {
    if (notice_users != null) {
      return BindingUtils.getFollowRecordNotiOhtersNames(notice_users, null);
    } else {
      return "--";
    }
  }

  public String getFollowTimeString() {
    if (TextUtils.isEmpty(next_track_time)) {
      return "";
    } else {
      return DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(next_track_time));
    }
  }

  public String getAvatar() {
    if (created_by != null) {
      return created_by.avatar;
    } else {
      return "";
    }
  }

  public String getName() {
    if (created_by != null) {
      return created_by.username;
    } else {
      return "";
    }
  }

  public String getContent() {
    return content;
  }

  public int getAttachSize() {
    if (image_attachments != null) {
      return image_attachments.size();
    } else {
      return 0;
    }
  }
}

