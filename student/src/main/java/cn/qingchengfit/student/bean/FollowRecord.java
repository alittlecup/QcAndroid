package cn.qingchengfit.student.bean;

import cn.qingchengfit.model.base.User;
import cn.qingchengfit.saascommon.constant.Configs;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FollowRecord {
  String id;
  String content;
  String track_type_id;
  String track_status_id;
  List<User> notice_users;
  String next_track_time;
  List<Attach> image_attachments;//附件列表，图片
  User created_by;

  public String getFollowMethodString(){
    return "";
  }

  public String getFollowStatusString(){
    return "";
  }

  public String getNotiOthers(){
    return "";
  }

  public String getFollowTimeString(){
    return "2017-12-20 11:00";
  }

  public String getAvatar(){
    if (created_by != null){
      return created_by.avatar;
    }else return "";
  }
  public String getName(){
    if (created_by != null){
      return created_by.username;
    }else return "";
  }

  public String getContent() {
    return content;
  }

  public int getAttachSize(){
    if (image_attachments != null)
      return image_attachments.size();
    else return 0;
  }


  public static FollowRecord mock(){
    FollowRecord fr = new FollowRecord();
    fr.created_by = new User();
    fr.created_by.username = "纸团";
    fr.created_by.avatar = "https://zoneke-img.b0.upaiyun.com/473c0770d3490415066df9db998f31ce.png";
    fr.content ="假装在跟进";
    fr.image_attachments = new ArrayList<>();
    Attach attach = new Attach("https://zoneke-img.b0.upaiyun.com/473c0770d3490415066df9db998f31ce.png");
    fr.image_attachments.add(attach);
    fr.image_attachments.add(attach);
    fr.image_attachments.add(attach);
    fr.track_status_id = UUID.randomUUID().toString();
    return fr;
  }
}

