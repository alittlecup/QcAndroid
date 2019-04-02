package cn.qingchengfit.saasbase.constant;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.utils.AppUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/4/12.
 */

public class ConstantNotification {

  public static final int TYPE_FITNESS_CHECK_IN = 1;              //健身房-签到-工作人员
  public static final int TYPE_FITNESS_CHECK_OUT = 2;             //健身房-签出-工作人员
  public static final int TYPE_FITNESS_ORDER = 3;             //健身房-预约-教练
  public static final int TYPE_FITNESS_ORDER_CANCEL = 4;              //健身房-取消预约-教练
  public static final int TYPE_FITNESS_SCORE = 5;             //健身房-评分-教练
  public static final int TYPE_FITNESS_TEAM_ERROR = 6;                //健身房-团课人数不足-教练
  public static final int TYPE_FITNESS_COACH = 7;             //健身房-新建教练-教练
  public static final int TYPE_FITNESS_CHECK_IN_CONFIRM = 8;              //健身房-签到确认-工作人员
  public static final int TYPE_FITNESS_CHECK_OUT_CONFIRM = 9;             //健身房-签出确认-工作人员
  public static final int TYPE_FITNESS_ASK_PHOTO = 10;                //身房-请求上传照片-教练
  public static final int TYPE_FITNESS_SELLER_CHANGED = 11;               //健身房-变更销售-工作人员
  public static final int TYPE_FITNESS_WITHOUT_SELLER = 12;               //健身房-未分配销售提醒-工作人员
  public static final int TYPE_FITNESS_REMIND_CARD_BALANCE = 13;              //健身房-会员卡余额不足提醒-工作人员
  public static final int TYPE_FITNESS_COACH_REST = 14;               //健身房-通知教练休息时间-教练
  public static final int TYPE_FITNESS_AFTER_CARD_ORDER_DONE = 15;
  //健身房-在线购卡/冲卡成功-工作人员
  public static final int TYPE_FITNESS_FOLLOW_RECORD = 17; //会员跟进
  public static final int TYPE_FITNESS_REMIND_TRACK = 18; //会员跟进下一次跟进提醒通知
  public static final int TYPE_FITNESS_REMIND_BIRTHDAY = 19; //健身房-生日提醒-工作人员
  public static final int TYPE_GYM_POSITION_APPLY = 21; //职位申请
  public static final int TYPE_GYM_STAFF_SUCCESS = 22; //申请工作人员通过
  public static final int TYPE_GYM_STAFF_FAIL = 23; //申请工作人员拒绝
  public static final int TYPE_GYM_CAOCH_SUCCESS = 24; //申请教练通过
  public static final int TYPE_GYM_COACH_FAIL = 25; //申请教练拒绝
  public static final int TYPE_FITNESS_TRAINER_CHANGED = 16;                //健身房-教练变更
  public static final int TYPE_MEETING_PAY = 10001;               //会议-支付成功-购票人
  public static final int TYPE_MEETING_TICKET = 10002;                //会议-报名成功-参会人
  public static final int TYPE_MEETING_PAY_CERTIFICATES = 10003;              //会议-上传凭证成功-学员
  public static final int TYPE_MEETING_SCHEDULE = 10004;              //会议-课程预约成功-学员
  public static final int TYPE_MEETING_SCHEDULE_CANCEL = 10005;               //会议-课程预约取消-学员
  public static final int TYPE_MEETING_CERTIFICATES = 10006;              //会议-发送证书-学员
  public static final int TYPE_MEETING_PAY_CHECK = 10007;             //会议-转账审核通过-学员
  public static final int TYPE_MEETING_PAY_CHECK_FAIL = 10008;                //会议-转账审核不通过-学员
  public static final int TYPE_MEETING_TICKET_REFUND = 10009;                //会议-作废门票-学员
  public static final int TYPE_MEETING_TRADE_REFUND = 10010;                //会议-取消订单-学员

  public static final int TYPE_CLOUD_MESSAGE_COACH = 20001;               //系统-发送通知-教练
  public static final int TYPE_CLOUD_MESSAGE_STAFF = 20002;               //系统-发送通知-工作人员
  public static final int TYPE_CLOUD_COMMENT = 20003;               //评论-收到评论
  public static final int TYPE_CLOUD_COMMPETITION_STAFF = 20004;               //赛事——工作人员
  public static final int TYPE_CLOUD_JOB_FAIR_CHECK = 20005;               //招聘--招聘会审核
  public static final int TYPE_CLOUD_JOB_FAIR_ADD_GYM = 20006;               //招聘——招聘会新增场馆

  /*
   *场馆消息
   */
  public static final String GYM_NOTIFICATION_STR = "gym";
  public static final int[] GYM_NOTIFICATION = {
      TYPE_FITNESS_SELLER_CHANGED, TYPE_FITNESS_WITHOUT_SELLER, TYPE_FITNESS_REMIND_CARD_BALANCE,
      TYPE_FITNESS_AFTER_CARD_ORDER_DONE, TYPE_FITNESS_FOLLOW_RECORD, TYPE_FITNESS_REMIND_TRACK,
      TYPE_FITNESS_REMIND_BIRTHDAY,TYPE_GYM_POSITION_APPLY,TYPE_GYM_STAFF_SUCCESS,TYPE_GYM_STAFF_FAIL
  };
  public static final int[] GYM_NOTIFICATION_TRAINER = {
      TYPE_FITNESS_ORDER, TYPE_FITNESS_ORDER_CANCEL, TYPE_FITNESS_SCORE, TYPE_FITNESS_TEAM_ERROR,
      TYPE_FITNESS_COACH, TYPE_FITNESS_ASK_PHOTO, TYPE_FITNESS_COACH_REST,
      TYPE_FITNESS_TRAINER_CHANGED,TYPE_GYM_CAOCH_SUCCESS,TYPE_GYM_COACH_FAIL
  };

  /*
     系统通知
   */
  public static final String SYSTEM_NOTIFICATION_STR = "system";
  public static final int[] SYSTEM_NOTIFICATION =
      { TYPE_CLOUD_MESSAGE_STAFF, TYPE_CLOUD_JOB_FAIR_CHECK, TYPE_CLOUD_JOB_FAIR_ADD_GYM };
  public static final int[] SYSTEM_NOTIFICATION_TRAINER =
      { TYPE_CLOUD_MESSAGE_COACH, TYPE_CLOUD_JOB_FAIR_CHECK, TYPE_CLOUD_JOB_FAIR_ADD_GYM };

  /*
   *学习培训
   */
  public static final String STUDY_TRAIN_NOTIFICATION_STR = "meeting";
  public static final int[] STUDY_TRAIN_NOTIFICATION = {
      TYPE_MEETING_PAY, TYPE_MEETING_TICKET, TYPE_MEETING_PAY_CERTIFICATES, TYPE_MEETING_SCHEDULE,
      TYPE_MEETING_SCHEDULE_CANCEL, TYPE_MEETING_CERTIFICATES, TYPE_MEETING_PAY_CHECK,
      TYPE_MEETING_PAY_CHECK_FAIL, TYPE_MEETING_TICKET_REFUND, TYPE_MEETING_TRADE_REFUND
  };

  public static final int[] STUDY_TRAIN_NOTIFICATION_TRAINER = {
      TYPE_MEETING_PAY, TYPE_MEETING_TICKET, TYPE_MEETING_PAY_CERTIFICATES, TYPE_MEETING_SCHEDULE,
      TYPE_MEETING_SCHEDULE_CANCEL, TYPE_MEETING_CERTIFICATES, TYPE_MEETING_PAY_CHECK,
      TYPE_MEETING_PAY_CHECK_FAIL, TYPE_MEETING_TICKET_REFUND, TYPE_MEETING_TRADE_REFUND
  };

  /*
   * 签到处理
   */
  public static final String CHECKIN_NOTI_STR = "checkin";
  public static final int[] CHECKIN_NOTIFICATION = {
      TYPE_FITNESS_CHECK_IN, TYPE_FITNESS_CHECK_OUT, TYPE_FITNESS_CHECK_IN_CONFIRM,
      TYPE_FITNESS_CHECK_OUT_CONFIRM
  };
  public static final int[] CHECKIN_NOTIFICATION_TRAINER = {
      TYPE_FITNESS_CHECK_IN, TYPE_FITNESS_CHECK_OUT, TYPE_FITNESS_CHECK_IN_CONFIRM,
      TYPE_FITNESS_CHECK_OUT_CONFIRM
  };

  /**
   * 赛事训练营
   */
  public static final String COMPETITION_NOTI_STR = "competition";
  public static final int[] COMPETITION_NOTIFICATION = { TYPE_CLOUD_COMMPETITION_STAFF };

  /*
   * 收到回复
   */
  public static final String COMMENT_NOTIFICATION_STR = "comment";
  public static final int[] COMMENT_NOTIFICATION = { TYPE_CLOUD_COMMENT };

  public static final String JOB_NOTIFICATION_STR = "job";
  public static final String[] NOTISORDERS = {
      GYM_NOTIFICATION_STR, SYSTEM_NOTIFICATION_STR, STUDY_TRAIN_NOTIFICATION_STR, CHECKIN_NOTI_STR,
      COMPETITION_NOTI_STR, COMMENT_NOTIFICATION_STR
  };
  public static final String[] NOTISORDERS_TRAINER = {
      GYM_NOTIFICATION_STR, SYSTEM_NOTIFICATION_STR, STUDY_TRAIN_NOTIFICATION_STR, CHECKIN_NOTI_STR,
      COMMENT_NOTIFICATION_STR
  };

  public static String getNotiQueryJson(Context context) {
    try {
      int apptype = AppUtils.getCurApp(context); //0 是教练App
      JSONObject jsonObject = new JSONObject();
      if (apptype == 0) {
        jsonObject.put(GYM_NOTIFICATION_STR, getArray(GYM_NOTIFICATION_TRAINER));
        jsonObject.put(SYSTEM_NOTIFICATION_STR, getArray(SYSTEM_NOTIFICATION_TRAINER));
        jsonObject.put(STUDY_TRAIN_NOTIFICATION_STR, getArray(STUDY_TRAIN_NOTIFICATION_TRAINER));
        //jsonObject.put(CHECKIN_NOTI_STR,getArray(CHECKIN_NOTIFICATION));
        jsonObject.put(COMMENT_NOTIFICATION_STR, getArray(COMMENT_NOTIFICATION));
      } else {
        jsonObject.put(GYM_NOTIFICATION_STR, getArray(GYM_NOTIFICATION));
        jsonObject.put(SYSTEM_NOTIFICATION_STR, getArray(SYSTEM_NOTIFICATION));
        jsonObject.put(STUDY_TRAIN_NOTIFICATION_STR, getArray(STUDY_TRAIN_NOTIFICATION));
        jsonObject.put(CHECKIN_NOTI_STR, getArray(CHECKIN_NOTIFICATION));
        jsonObject.put(COMPETITION_NOTI_STR, getArray(COMPETITION_NOTIFICATION));
        jsonObject.put(COMMENT_NOTIFICATION_STR, getArray(COMMENT_NOTIFICATION));
      }
      return jsonObject.toString();
    } catch (Exception e) {
      return "";
    }
  }

  private static JSONArray getArray(int[] ints) {
    JSONArray jsonArray = new JSONArray();
    for (int i = 0; i < ints.length; i++) {
      jsonArray.put(ints[i]);
    }
    return jsonArray;
  }

  @DrawableRes public static int getNotiDrawable(String type) {
    switch (type) {
      case SYSTEM_NOTIFICATION_STR:
        return R.drawable.vd_notification_system;
      case STUDY_TRAIN_NOTIFICATION_STR:
        return R.drawable.vd_notification_meetting;
      case CHECKIN_NOTI_STR:
        return R.drawable.vd_notification_checkin;
      case COMPETITION_NOTI_STR:
        return R.drawable.vd_notification_competition;
      case COMMENT_NOTIFICATION_STR:
        return R.drawable.vd_notification_comment;
      default:
        return R.drawable.vd_notification_gym;
    }
  }

  @StringRes public static int getNotiStr(String type) {
    switch (type) {
      case SYSTEM_NOTIFICATION_STR:
        return R.string.notification_system;
      case STUDY_TRAIN_NOTIFICATION_STR:
        return R.string.notification_meetting;
      case CHECKIN_NOTI_STR:
        return R.string.notification_checkin;
      case COMMENT_NOTIFICATION_STR:
        return R.string.notification_comment;
      case COMPETITION_NOTI_STR:
        return R.string.notification_competition;
      case GYM_NOTIFICATION_STR:
        return R.string.notification_gym;
      default:
        return R.string.notification_job;
    }
  }

  public static String getCategloreStr(Context context, String type) {
    int appType = AppUtils.getCurApp(context);
    switch (type) {
      case SYSTEM_NOTIFICATION_STR:
        return StringUtils.array2str(
            appType == 0 ? SYSTEM_NOTIFICATION_TRAINER : SYSTEM_NOTIFICATION);
      case STUDY_TRAIN_NOTIFICATION_STR:
        return StringUtils.array2str(
            appType == 0 ? STUDY_TRAIN_NOTIFICATION_TRAINER : STUDY_TRAIN_NOTIFICATION);
      case CHECKIN_NOTI_STR:
        return StringUtils.array2str(
            appType == 0 ? CHECKIN_NOTIFICATION_TRAINER : CHECKIN_NOTIFICATION);
      case COMPETITION_NOTI_STR:
        return StringUtils.array2str(COMPETITION_NOTIFICATION);
      case COMMENT_NOTIFICATION_STR:
        return StringUtils.array2str(COMMENT_NOTIFICATION);
      default:
        return StringUtils.array2str(appType == 0 ? GYM_NOTIFICATION_TRAINER : GYM_NOTIFICATION);
    }
  }
}
