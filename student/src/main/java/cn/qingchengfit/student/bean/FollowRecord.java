package cn.qingchengfit.student.bean;

import android.content.Context;
import android.text.TextUtils;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.student.bingdings.BindingUtils;
import cn.qingchengfit.utils.DateUtils;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FollowRecord {
  String id;
  String content;
  String track_type_id;
  String track_status;
  List<String> notice_users;
  String next_track_time;
  @SerializedName(value = "attachments") List<Attach> image_attachments;//附件列表，图片
  User created_by;
  String created_at;

  public String getShop() {
    return shop;
  }

  String shop;


  public String getCreatedTimeStr() {
    return DateUtils.getNotifacationTimeStr(DateUtils.formatDateFromServer(created_at));
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
      return DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(next_track_time));
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

  public static FollowRecord mock() {
    FollowRecord fr = new FollowRecord();
    fr.created_by = new User();
    fr.created_by.username = "纸团";
    fr.created_by.avatar = "https://zoneke-img.b0.upaiyun.com/473c0770d3490415066df9db998f31ce.png";
    fr.content = "假装在跟进";
    fr.image_attachments = new ArrayList<>();
    Attach attach =
        new Attach("https://zoneke-img.b0.upaiyun.com/473c0770d3490415066df9db998f31ce.png");
    fr.image_attachments.add(attach);
    fr.image_attachments.add(attach);
    fr.image_attachments.add(attach);
    fr.track_status = UUID.randomUUID().toString();
    return fr;
  }
}

