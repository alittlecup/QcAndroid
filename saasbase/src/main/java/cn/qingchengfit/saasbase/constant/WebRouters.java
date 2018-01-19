package cn.qingchengfit.saasbase.constant;

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
 * Created by Paper on 2017/12/1.
 */

public class WebRouters {
  /**
   * 工作台
   */
  public static final String MODULE_WORKSPACE_ORDER_LIST = "/orders/day";
  public static final String MODULE_WORKSPACE_GROUP = "/group/order";
  public static final String MODULE_WORKSPACE_PRIVATE = "/private/order";
  public static final String MODULE_WORKSPACE_ORDER_SIGNIN = "/checkin/help";

  /**
   * 健身房服务
   */
  public static final String MODULE_SERVICE_GROUP = "/teamarrange/calendar2";
  public static final String MODULE_SERVICE_PRIVATE = "/priarrange/calendar2";
  public static final String MODULE_SERVICE_FREE = "/checkin/setting";
  public static final String MODULE_SERVICE_SHOP = "/commodity/list";
  /**
   * 会员
   */
  public static final String MODULE_STUDENT = "/manage/members";
  public static final String MODULE_STUDENT_CARDS = "/manage/costs";
  public static final String MODULE_STUDENT_BODY_TEST = "/measure/setting";

  /**
   * 内部管理
   */
  public static final String MODULE_MANAGE_STAFF = "/manage/staff";
  public static final String MODULE_MANAGE_STAFF_ADD = "/manage/staff/add/";
  public static final String MODULE_MANAGE_COACH = "/coachsetting";
  /**
   * 运营推广
   */
  public static final String MODULE_OPERATE_SCORE = "/score/rank";
  public static final String MODULE_OPERATE_ACTIVITY = "/activity/setting";
  public static final String MODULE_OPERATE_AD = "/shop/home/setting";
  public static final String MODULE_OPERTAT_KOUBEI = "/koubei";
  public static final String MODULE_OPERATE_REGIST = "/giftcard";
  public static final String MODULE_OPERATE_ANOUNCE = "/notice";
  public static final String MODULE_OPERATE_COMPETITION = "/competition";

  /**
   * 财务
   */
  public static final String MODULE_FINACE_ONLINE = "/pay/bills";
  public static final String MODULE_FINANCE_COURSE = "/cost/report";
  public static final String MODULE_FINANCE_SALE = "/sales/report";
  public static final String MODULE_FINANCE_MARK = "/comments/report";
  public static final String MODULE_FINANCE_CARD = "/card/report";
  public static final String MODULE_FINANCE_SIGN_IN = "/checkin/report";

  /**
   * 场馆信息
   */
  public static final String MODULE_GYM_INFO = "/studio/setting?mb_module=info";//场馆管理
  public static final String MODULE_GYM_TIME = "/studio/setting?mb_module=time";
  public static final String MODULE_GYM_SITE = "/space/setting";
  public static final String MODULE_MSG = "/message/setting";
  public static final String MODULE_WARDROBE = "/locker/setting";
  public static final String MODULE_HOME = "/shop/home";
  public static final String MODULE_WECHAT = "/shop/weixin/setting";// 微信公众号

  /**
   * article 文章回复 资讯
   */
  public static final String MODULE_ARTICLE_COMMENT_REPLY = "/article/comment/reply";//
  public static final String MODULE_ARTICLE_COMMENT_LIST = "/article/comment/list";//

  public static final String PERMISSION_STAFF = "/position/setting";
  public static final String PERMISSION_TRAINER = "/coach/permission/setting";
  public static final String SIGNIN_SCREEN = "/checkin/screen/";
  public static final String PLANS_SETTING_GROUP = "/private/course/plans/setting";
  public static final String PLANS_SETTING_PRIVATE = "/checkin/screen/";

  //求职招聘消息列表
  public static final String RECRUIT_MESSAGE_LIST = "/recruit/message_list";

  //导入导出
  public static final String REPORT_EXPORT = "/report/export";

  public static final String STUDENT_IMPORT = "/manage/members/import";
  public static final String STUDENT_EXPORT = "/manage/members/export";
  public static final String CARD_IMPORT = "/cards/import";
  public static final String CARD_EXPORT = "/cards/export";

  //用户协议
  public static final String USER_PROTOCOL = "/user/protocol";
  public static final String USER_PROTOCOL_URL = "/protocol/staff/";

}
