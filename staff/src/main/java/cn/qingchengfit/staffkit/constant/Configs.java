package cn.qingchengfit.staffkit.constant;

import cn.qingchengfit.Constants;

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
    //public static final String URL_DEBUG = "http://cloudmirror.qingchengfit.cn/";
    //    public static final String URL_DEBUG = "http://c1.qingchengfit.cn/";
    //    public static final String URL_DEBUG = "http://192.168.1.11:8000/";
    //    public static final String URL_DEBUG = "http://192.168.1.96:7777/";
    //public static final String URL_RELEASE = "http://cloud.qingchengfit.cn/";
    public static final String URL_QC_TRAIN = "mobile/staff/training/";
    public static final String SEPARATOR = "、";
    public static final String PREFER_SESSION = "qingcheng.session";
    public static final String PREFER_PHONE = "qingcheng.phone";
    public static final String PREFER_WORK_NAME = "qingcheng.work.name";
    public static final String PREFER_WORK_ID = "qingcheng.work.id";
    public static final String PREFER_USER_ID = "qingcheng.user.id";
    public static final String CUR_BRAND_ID = "qingcheng.current.brandid";
    public static final String EXTRA_GYM_SERVICE = "qingcheng.gym.service";
    //    public static final String EXTRA_GYM_MODEL = "qingcheng.gym.model";
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
    public static final int TYPE_PRIVATE = 1;  //私教
    public static final String ROUTER = "router";  //路由的key
    //    public static final String EXTRA_STATEMENT_DATA_DAY = "qingcheng.statement_data.day";
    //    public static final String EXTRA_STATEMENT_DATA_WEEK = "qingcheng.statement_data.week";
    //    public static final String EXTRA_STATEMENT_DATA_MONTH = "qingcheng.statement_data.month";
    //    public static final String EXTRA_STATEMENT_SALE_DAY = "qingcheng.statement_sale.day";
    //    public static final String EXTRA_STATEMENT_SALE_WEEK = "qingcheng.statement_sale.week";
    //    public static final String EXTRA_STATEMENT_SALE_MONTH = "qingcheng.statement_sale.month";
    public static final int TYPE_GROUP = 0;    //团课
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
    public static final String HEADER_STUDENT_MALE = "http://zoneke-img.b0.upaiyun.com/977ad17699c4e4212b52000ed670091a.png";
    public static final String HEADER_STUDENT_FEMALE = "http://zoneke-img.b0.upaiyun.com/f1ac90184acb746e4fbdef4b61dcd6f6.png";
    public static final String HEADER_COACH_MALE = "http://zoneke-img.b0.upaiyun.com/75656eb980b79e7748041f830332cc62.png";
    public static final String HEADER_COACH_FEMALE = "http://zoneke-img.b0.upaiyun.com/7f362320fb3c82270f6c9c623e39ba92.png";
    public static final String WECHAT_GUIDE = "mobile/weixin/guide/";
    public static final String GYM_GUIDE = "mobile/gym/guide/";
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
    /**
     * 积分方式
     */

    public static final String CACLU_SCORE_CHARGE = "chargecard"; //充卡算积分
    public static final String CACLU_SCORE_BUY = "buycard"; //
    /**
     * 续费状态
     */

    public static final int RENEWAL_FAIL = 0; //已创建
    public static final int RENEWAL_CREATE = 1; //已完成
    public static final int RENEWAL_DONE = 2; //已取消
    public static final int RENEWAL_CANCEL = 3; //已失败
    public static final int RENEWAL_TBC = 4; //待处理
    public static final int RENEWAL_EXPIRE = 5; //已过期
    /**
     * 服务端交易方式
     */
    public static final int TRADE_CHARGE = 1;  //充值
    public static final int TRADE_EXPENSE = 2;
    public static final int TRADE_CHARGE_FIRST = 3; //开卡
    public static final int TRADE_REFUND = 14; //扣费
    public static final int TRADE_PRESENT = 13;  //赠送
    /**
     * 支付渠道
     */
    public static final String CHANNEL_CARD = "CARD";
    public static final String CHANNEL_ONLINE = "ONLINE";
    /**
     * 服务端订单支付类型
     */
    public static final int DEAL_NONE = 0;  //无
    public static final int DEAL_CASH = 1;  //现金
    public static final int DEAL_CARD = 2;  //卡
    public static final int DEAL_TRANSFER = 3;  //转账
    public static final int DEAL_ADMIN = 4;  //人工
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
    public static final String IMAGE_ALL = "http://qcresource.b0.upaiyun.com/ic_all_normal.png";
    //public static String Server = !BuildConfig.FLAVOR.equals("product") ? Constants.Server:(BuildConfig.DEBUG?Constants.ServerMirror:Constants.Server);
    public static String Server = Constants.Server;
    public static String URL_QC_FIND = Server + "mobile/staff/discover/";
    public static String APP_ID = "wx2beb386a0021ed3f";    //微信appid
}
