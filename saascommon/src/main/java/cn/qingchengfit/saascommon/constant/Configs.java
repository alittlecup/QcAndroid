package cn.qingchengfit.saascommon.constant;

import cn.qingchengfit.Constants;
import cn.qingchengfit.saascommon.BuildConfig;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/12/1 2015.
 */
public class Configs {
  public static final String URL_QC_TRAIN = "mobile/staff/training/";
  public static final String SEPARATOR = "、";
  public static final String PREFER_SESSION = "qingcheng.session";
  public static final String PREFER_SESSION_ID = "session_id";
  public static final String PREFER_PHONE = "qingcheng.phone";
  public static final String PREFER_WORK_NAME = "qingcheng.work.name";
  public static final String PREFER_WORK_NAME_MIRROR = "qingcheng.work.name.mirror";
  public static final String PREFER_WORK_ID = "qingcheng.work.id";
  public static final String PREFER_COACH_ID = "qingcheng.coach.id";
  public static final String PREFER_USER_ID = "qingcheng.user.id";
  public static final String CUR_BRAND_ID = "qingcheng.current.brandid";
  public static final String EXTRA_GYM_SERVICE = "qingcheng.gym.service";
  public static final String EXTRA_GYM_SINGLE = "qingcheng.gym.single";
  public static final String EXTRA_GYM_STATUS = "qingcheng.gym.status";
  public static final String EXTRA_BRAND = "qingcheng.brand";
  public static final String EXTRA_COURSE_TYPE = "qingcheng.coursetype";
  public static final String EXTRA_CARD_ID = "qingcheng.card.id";
  public static final String EXTRA_REAL_CARD = "qingcheng.realcard";
  public static final String EXTRA_CARD_TYPE = "qingcheng.card.type";
  public static final String EXTRA_STUDENT_ID = "qingcheng.student.id";
  public static final String EXTRA_GYM_ID = "gym_id";
  public static final String EXTRA_STATEMENT_DATA = "qingcheng.statement_data";
  public static final String EXTRA_STATEMENT_SALE = "qingcheng.statement_sale";
  public static final String EXTRA_STATEMENT_SIGNIN = "qingcheng.statement_signin";
  public static final String EXTRA_SIGNIN_SEARCH = "qingcheng.signin.search";
  public static final String EXTRA_PERMISSION_KEY = "qingcheng.permission.key";
  public static final String EXTRA_PERMISSION_METHOD = "qingcheng.permission.method";
  public static final String EXTRA_STATEMENT_DATA_DAY_TYPE = "qingcheng.statement_data.daytype";

  public static final String ROUTER = "router";  //路由的key

  public static final int TYPE_GROUP = 0;    //团课
  public static final int TYPE_PRIVATE = 1;  //私教

  public static final int CATEGORY_VALUE = 1;    //储值卡
  public static final int CATEGORY_TIMES = 2;    //次卡
  public static final int CATEGORY_DATE = 3;    //期限卡
  public static final int OFFDAY_ON = 1;    //请假中
  public static final int OFFDAY_OFF = 2;    //取消请假
  public static final int INIT_TYPE_GUIDE = 0;  //引导
  public static final int INIT_TYPE_ADD = 1;    //新建健身房
  public static final int INIT_TYPE_CHOOSE = 2; //管理修改
  /**
   * data update
   */

  public static final String PREFER_CARD_UPDATE = "qingcheng.card.update";
  public static final String PREFER_STUDENT_UPDATE = "qingcheng.student.update";
  public static final String ABOUT_US = "http://cloud.qingchengfit.cn/";
  //教练助手下载页面
  public static final String DOWNLOAD_TRAINER = "http://fir.im/qingchengfit";
  public static final String DOWNLOAD_MANAGE = "http://fir.im/qcfit";
  public static final String HEADER_STUDENT_MALE =
      "https://img.qingchengfit.cn/977ad17699c4e4212b52000ed670091a.png";
  public static final String HEADER_STUDENT_FEMALE =
      "https://img.qingchengfit.cn/f1ac90184acb746e4fbdef4b61dcd6f6.png";
  public static final String HEADER_COACH_MALE =
      "https://img.qingchengfit.cn/75656eb980b79e7748041f830332cc62.png";
  public static final String HEADER_COACH_FEMALE =
      "https://img.qingchengfit.cn/7f362320fb3c82270f6c9c623e39ba92.png";
  public static final String WECHAT_GUIDE = "mobile/weixin/guide/";
  public static final String GYM_GUIDE = "mobile/gym/guide/";
  /**
   * 服务端支付方法值
   */
  public static final int CHARGE_MODE_CASH = 1; //现金
  //    public static final String  = "/mobile/weixin/guide/";
  public static final int CHARGE_MODE_CARD = 2; //卡
  public static final int CHARGE_MODE_TRANSFER = 3; //转账
  public static final int CHARGE_MODE_OTHER = 4; //人工
  public static final int CHARGE_MODE_AUTO = 5; //赠送
  public static final int CHARGE_MODE_WEIXIN = 6; //微信
  public static final int CHARGE_MODE_WEIXIN_QRCODE = 7; //二维码
  /**
   * 积分方式
   */

  public static final String CACLU_SCORE_CHARGE = "chargecard"; //充卡算积分
  public static final String CACLU_SCORE_BUY = "buycard"; //
  /**
   * 支付渠道
   */
  public static final String CHANNEL_CARD = "CARD";
  public static final String CHANNEL_ONLINE = "ONLINE";
  /**
   * 服务端交易方式
   */
  public static final int TRADE_CHARGE = 1;  //充值
  public static final int TRADE_EXPENSE = 2;
  public static final int TRADE_CHARGE_FIRST = 3; //开卡
  public static final int TRADE_REFUND = 14; //扣费
  public static final int TRADE_PRESENT = 13;  //赠送
  public static final int TRADE_DEDUCTION = 23;  //扣费时填写扣费金额
  /**
   * 积分方式
   */
  public static final int RENEWAL_DONE = 2; //已取消
  public static final int RENEWAL_CANCEL = 3; //已失败
  public static final int RENEWAL_TBC = 4; //待处理
  public static final int RENEWAL_EXPIRE = 5; //已过期

  /**
   * 服务端订单支付类型
   */
  public static final int DEAL_NONE = 0;  //无
  public static final int DEAL_CASH = 1;  //现金
  public static final int DEAL_CARD = 2;  //卡
  public static final int DEAL_TRANSFER = 3;  //转账
  public static final int DEAL_ADMIN = 4;  //其他
  public static final int DEAL_QINGCHENG = 11;  //青城卡
  public static final int DEAL_WECHAT = 12;  //微信
  public static final int DEAL_WEIXIN_QR = 13;  //微信扫码
  public static final int DEAL_ALIPAY = 14;  //支付宝
  /**
   * 代约团课和代约私教
   */
  public static final String SCHEDULE_GROUP = "fitness/redirect/staff/group/";
  public static final String SCHEDULE_PRIVATE = "fitness/redirect/staff/private/";
  public static final String HOST_ORDERS = "mobile/trades/home/";
  public static String Server = Constants.Server;
  public static String URL_QC_FIND = Server + "mobile/staff/discover/";
  public static String APP_ID = "wx2beb386a0021ed3f";    //微信appid

  public static final String WEEX_TEST_PATH =
      " https://static.qingchengfit.cn/qc-commodity-weex/version_test.json";
  public static final String WEEX_RELEASE_PATH =
      " https://static.qingchengfit.cn/qc-commodity-weex/0.0.39/version.json";
  public static final String WEEX_PAGE_INDEX = "proxy_commodity.js";
  public static String QR_POINT_URL =
      BuildConfig.DEBUG ? "saotest.qingchengfit.cn" : "sao.qingchengfit.cn";
  public static final String WEB_HOW_TO_USE_BATCH_GROUP =
      "http://cloud.qingchengfit.cn/mobile/urls/e382d87968dd4f54a89bb5e5a933f779/";
  //团课
  public static final String WEB_HOW_TO_USE_BATCH_PRIVATE =
      "http://cloud.qingchengfit.cn/mobile/urls/34890304d8bc40ba9677ca8d99bcd02a/";
  public static final String WEB_TEMP_NEW_YEAR =
      "http://mp.weixin.qq.com/s?__biz=MzAxODAyODE5OQ==&mid=502628702&idx=1&sn=b77bdd8d6bff211b34a7fa5471efb679&chksm=03dda29634aa2b8062c043694f369f70ca4309846dad31192555c2658ac664270b249bfce6cc#rd";

  public static String getServerByBuildFLAVOR(String flavor) {
    String host = "";
    switch (flavor) {
      case "SIT":
        host = Constants.ServerDebug;
        break;
      case "UAT":
        host = Constants.ServerMirror;
        break;
      case "product":
      default:
        host = Constants.Server;
        break;
    }
    Server = host;
    URL_QC_FIND = Server + "mobile/staff/discover/";
    return host;
  }
}
