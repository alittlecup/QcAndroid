package com.qingchengfit.fitcoach;

import android.os.Environment;
import cn.qingchengfit.Constants;
import cn.qingchengfit.utils.PreferenceUtils;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/8/5 2015.
 */
public class Configs {

  public static final int TYPE_PRIVATE = 1;  //私教
  public static final int TYPE_GROUP = 2;    //团课
  public static final String[] STRINGS_WEEKDAY = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };

  public static final String SIGN_PAUSE = "、";
  public static final String EXTRA_CARD_TYPE = "qingcheng.card.type";
  /**
   * 默认头像
   */
  public static final String HEADER_STUDENT_MALE =
      "http://zoneke-img.b0.upaiyun.com/977ad17699c4e4212b52000ed670091a.png";
  public static final String HEADER_STUDENT_FEMALE =
      "http://zoneke-img.b0.upaiyun.com/f1ac90184acb746e4fbdef4b61dcd6f6.png";
  public static final String HEADER_COACH_MALE =
      "http://zoneke-img.b0.upaiyun.com/75656eb980b79e7748041f830332cc62.png";
  public static final String HEADER_COACH_FEMALE =
      "http://zoneke-img.b0.upaiyun.com/7f362320fb3c82270f6c9c623e39ba92.png";
  /**
   * 会员预览页面
   */
  public static final String HOST_STUDENT_PREVIEW = "fitness/redirect/coaches/%s/saas/share/";
  public static final String HOST_RESUME = "mobile/coaches/%d/share/index/";
  public static final String HOST_ORDERS = "mobile/trades/home/";
  public static final String HOST_EDUCATION = "mobile/educations/";
  public static final String WECHAT_GUIDE = "mobile/weixin/guide/";
  public static final String SCHEDULE_REST = "fitness/redirect/coach/rest/";
  public static final String SCHEDULE_GROUP = "fitness/redirect/coach/group/";
  public static final String SCHEDULE_PRIVATE = "fitness/redirect/coach/private/";
  /**
   * 订单中心：http://cloudtest.qingchengfit.cn/mobile/trades/home/
   * 学习经历：http://cloudtest.qingchengfit.cn/mobile/educations/
   */

  public static final String CHANNEL_CARD = "CARD";
  public static final int CATEGORY_VALUE = 1;    //储值卡
  public static final int CATEGORY_TIMES = 2;    //次卡
  public static final int CATEGORY_DATE = 3;    //期限卡
  /**
   * 服务端交易方式
   */
  public static final int TRADE_CHARGE = 1;  //充值
  public static final int TRADE_EXPENSE = 2;
  public static final int TRADE_CHARGE_FIRST = 3; //开卡
  public static final int TRADE_REFUND = 14; //扣费
  public static final int TRADE_PRESENT = 13;  //赠送
  /**
   * 服务端支付方法值
   */
  public static final int CHARGE_MODE_CASH = 1; //现金
  //    public static final String  = "/mobile/weixin/guide/";
  public static final int CHARGE_MODE_CARD = 2; //卡
  public static final int CHARGE_MODE_TRANSFER = 3; //转账
  public static final int CHARGE_MODE_OTHER = 4; //其他
  public static final int CHARGE_MODE_AUTO = 5; //赠送
  public static final int CHARGE_MODE_WEIXIN = 6; //微信
  public static final int CHARGE_MODE_WEIXIN_QRCODE = 7; //二维码
  public static String APP_ID;
  public static String SEPARATOR = ",";
  /**
   * http config
   */
  //    public static boolean isDebug = true;
  public static boolean isDebug = false;
  public static String Server =getServerByBuildFLAVOR();
  public static String HOST_NAMESPACE_0 = "http://.qingchengfit.cn";
  public static String HOST_NAMESPACE_1 = "http://.qingchengfit.com";
  public static String PRIVATE_PRIVEIW = Configs.Server + "fitness/redirect/user/private/";
  public static String GROUP_PRIVEIW = Configs.Server + "fitness/redirect/user/group/";
  //app名称
  public static String APPNAME = "QingChengCoach";
  //私有外部路径
  public static String ExternalPath =
      Environment.getExternalStorageDirectory().getPath() + "/" + APPNAME + "/";
  public static String ExternalCache =
      Environment.getExternalStorageDirectory().getPath() + "/" + APPNAME + "/Cache/";
  //Camera图片位置
  public static String CameraPic =
      Environment.getExternalStorageDirectory().getPath() + "/" + APPNAME + "/Cache/camera_tmp.jpg";
  public static String CameraCrop =
      Environment.getExternalStorageDirectory().getPath() + "/" + APPNAME + "/Cache/crop_tmp.jpg";
  public static String PREFER_SESSION = "session_id";

  //用户协议
  public static final String USER_PROTOCOL_URL = "protocol/staff/";


  private static String getServerByBuildFLAVOR(){
    switch (BuildConfig.FLAVOR){
      case "SIT":
        return PreferenceUtils.getPrefString(App.AppContex, "debug_ip", Constants.ServerDebug);
      case "UAT":
        return Constants.ServerMirror;
      case "product":
        return Constants.Server;
    }
    return Constants.Server;
  }
}
