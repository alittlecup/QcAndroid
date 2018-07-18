package cn.qingchengfit.student.bingdings;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.FollowRecordStatus;
import cn.qingchengfit.utils.DateUtils;
import com.squareup.phrase.Phrase;
import java.util.List;

public class BindingUtils {

  /**
   * 跟进方式，int转string
   */
  public static String getFollowRecordMethod(int x,Context context){
    String[] array =  context.getResources().getStringArray(R.array.st_follow_record_method);
    if (x >0 && x<= array.length){
      return array[x-1];
    }else return "";
  }

  /**
   * 跟进状态
   */
  public static String getFollowRecordStatus(FollowRecordStatus status){
    if (status != null)
      return status.getTrack_status();
    else return "";
  }

  /**
   * 通知他人
   */
  public static String getFollowRecordNotiOhters(List<User> users,Context context){
    if (users != null &&users.size() >0){

      if (users.size() > 2){
        return
          users.get(0).username+","+users.get(1).username+"等"+users.size()+"人";
      }else if (users.size() == 2){
        return users.get(0).username+","+
          users.get(1).username;

      }else {
        return users.get(0).username;
      }
    }else return "";
  }

  public static String getNextFollowTime(String serverTime){
    return DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(serverTime));
  }

}
