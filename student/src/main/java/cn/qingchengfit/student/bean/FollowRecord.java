package cn.qingchengfit.student.bean;

import android.content.Context;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.student.bingdings.BindingUtils;
import cn.qingchengfit.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FollowRecord {
  String id;
  String content;
  String track_type_id;
  String track_status;
  List<User> notice_users;
  String next_track_time;
  List<Attach> image_attachments;//附件列表，图片
  User created_by;
  String created_at;
  public String getFollowMethodString(Context context){

    try {
      int x = Integer.parseInt(track_type_id);
      return BindingUtils.getFollowRecordMethod(x,context);
    }catch (Exception e){
      return "";
    }

  }


  public String getImagePos(int pos){
    if (image_attachments != null &&pos < image_attachments.size() ){
      return image_attachments.get(pos).getLink();
    }else return "";
  }

  public String getFollowStatusString(){
    if (track_status == null)
      return "";
    return track_status;
  }

  public String getNotiOthers(){
    if (notice_users != null){
      return BindingUtils.getFollowRecordNotiOhters(notice_users,null);

    }else return "";

  }

  public String getFollowTimeString(){
    if (next_track_time == null)
      return "";
    else
      return DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(next_track_time));
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
    fr.track_status = UUID.randomUUID().toString();
    return fr;
  }
}

