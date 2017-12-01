package cn.qingchengfit.saasbase.constant;

public class ShopConfigs {
  /**
   *  "team_sms_user_after_user_order_save": {
   "name": u"会员预约团课预约时，是否提醒会员？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "team_sms_teacher_after_user_order_cancel": {
   "name": u"当会员取消团课预约时，是否提醒教练？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "team_sms_user_after_user_order_cancel": {
   "name": u"当会员取消团课预约时，是否提醒会员？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "team_sms_user_after_teacher_order_save": {
   "name": u"教练／工作人员为会员代约团课后，是否提醒会员？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "team_sms_teacher_after_teacher_order_save": {
   "name": u"教练／工作人员为会员代约团课后，是否提醒教练？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "team_sms_teacher_after_teacher_order_cancel": {
   "name": u"教练／工作人员为会员取消团课后，是否提醒会员？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "team_sms_user_after_teacher_order_cancel": {
   "name": u"教练／工作人员为会员取消团课后，是否提醒教练？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "team_before_remind_user": {
   "name": u"会员在团课开始前是否收到提醒短信？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "team_before_remind_user_minutes": {
   "name": u"会员在团课开课前多少分钟收到提醒短信",
   "value": u"1440",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "team_course_remind_teacher": {
   "name": u"团课未达到最少上课人数，是否提醒教练？",
   "value": u"0",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "team_course_remind_teacher_minutes": {
   "name": u"团课未达到最少上课人数，提前多少分钟提醒教练",
   "value": u"240",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },

   # 私教预约限制
   "private_minutes_can_order": {
   "name": u"会员在私教开始前多少分钟不能预约？",
   "value": u"60",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "private_minutes_can_cancel": {
   "name": u"会员在私教开始前多少分钟不能取消预约？",
   "value": u"720",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },

   # 私教预约短信通知
   "private_sms_teacher_after_user_order_save": {
   "name": u"会员预约私教预约时，是否提醒教练？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "private_sms_user_after_user_order_save": {
   "name": u"会员预约私教预约时，是否提醒会员？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "private_sms_teacher_after_user_order_cancel": {
   "name": u"当会员取消私教预约时，是否提醒教练？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "private_sms_user_after_user_order_cancel": {
   "name": u"当会员取消私教预约时，是否提醒会员？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "private_sms_user_after_teacher_order_save": {
   "name": u"教练／工作人员为会员代约私教后，是否提醒会员？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "private_sms_teacher_after_teacher_order_save": {
   "name": u"教练／工作人员为会员代约私教后，是否提醒教练？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "private_sms_teacher_after_teacher_order_cancel": {
   "name": u"教练／工作人员为会员取消私教后，是否提醒会员？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "private_sms_user_after_teacher_order_cancel": {
   "name": u"教练／工作人员为会员取消私教后，是否提醒教练？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "private_before_remind_user": {
   "name": u"会员在私教开始前是否收到提醒短信？",
   "value": u"1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "private_before_remind_user_minutes": {
   "name": u"会员在私教开课前多少分钟收到提醒短信",
   "value": u"1440",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "private_course_remind_teacher": {
   "name": u"私教未达到最少上课人数，是否提醒教练？",
   "value": u"0",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "private_course_remind_teacher_minutes": {
   "name": u"私教未达到最少上课人数，提前多少分钟提醒教练",
   "value": u"240",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },

   # 未分类
   "before_minutes_can_sign": {
   "name": u"会员在课程开始前多少分钟可以签到？",
   "value": "120",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "after_minutes_can_sign": {
   "name": u"会员在课程开始后多少分钟可以签到？",
   "value": "30",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "private_slice_minutes": {
   "name": u"私教预约时的最小时间间隔（单位秒）",
   "value": "1800",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "value_card_remind": {
   "name": u"储值卡提醒金额",
   "value": "500",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "times_card_remind": {
   "name": "次卡提醒次数",
   "value": "10",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "time_card_remind": {
   "name": "期限卡提醒天数",
   "value": "30",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "sms_verify_code": {
   "name": u"是否发送验证码？（一定要选是）",
   "value": "1",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0"
   },
   "send_sms_after_charge_card": {
   "name": u"会员卡充值后是否发送确认短信？",
   "value": u"0",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0",
   },
   "withdraw_limit": {
   "name": u"每周可提现次数",
   "value": u"2",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "withdraw_enable": {
   "name": u"提现引导开关",
   "value": u"0",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0",
   },
   "pay_by_qrcode": {
   "name": u"使用微信二维码支付",
   "value": u"0",
   "callback": lambda x: x == "1",
   "to_db": lambda x: x in (True, "1") and "1" or "0",
   },
   "user_checkin_with_locker": {
   "name": u"更衣柜与签到联动",
   "value": u"1",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "check_out_with_return_locker":{
   "name": u"签出时自动归还更衣柜",
   "value": u"1",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "is_set_user_photos":{
   "name": u"是否设置了会员照片",
   "value": u"0",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   "sms_num": {
   "name": u"剩余短信条数",
   "value": "10000",
   "callback": lambda x: int(x) if x else 0,
   "to_db": lambda x: unicode(x),
   },
   *
   *
   *
   */

  /**
   * 会员在团课开始前多少分钟不能预约？
   */
  public static final String TEAM_MINUTES_CAN_ORDER = "team_minutes_can_order";
  /**
   * 会员在团课开始前多少分钟不能取消预约
   */
  public static final String TEAM_MINUTES_CAN_CANCEL = "team_minutes_can_cancel";
  /**
   * "team_sms_teacher_after_user_order_save": {
   * "name": u"会员预约团课预约时，是否提醒教练？",
   * "value": u"1",
   * "callback": lambda x: x == "1",
   * "to_db": lambda x: x in (True, "1") and "1" or "0"
   * },
   */
  public static final String TEAM_SMS_TEACHER_AFTER_USER_ORDER_SAVE =
      "team_sms_teacher_after_user_order_save";
  /**
   *
   */
  public static final String TEAM_SMS_USER_AFTER_USER_ORDER_SAVE =
      "team_sms_user_after_user_order_save";
  /**
   *
   */
  public static final String TEAM_SMS_TEACHER_AFTER_USER_ORDER_CANCEL =
      "team_sms_teacher_after_user_order_cancel";
  public static final String TEAM_SMS_USER_AFTER_USER_ORDER_CANCEL =
      "team_sms_user_after_user_order_cancel";
  public static final String TEAM_SMS_USER_AFTER_TEACHER_ORDER_SAVE =
      "team_sms_user_after_teacher_order_save";
  public static final String TEAM_SMS_TEACHER_AFTER_TEACHER_ORDER_SAVE =
      "team_sms_teacher_after_teacher_order_save";
  public static final String TEAM_SMS_TEACHER_AFTER_TEACHER_ORDER_CANCEL =
      "team_sms_teacher_after_teacher_order_cancel";
  public static final String TEAM_SMS_USER_AFTER_TEACHER_ORDER_CANCEL =
      "team_sms_user_after_teacher_order_cancel";
  public static final String TEAM_BEFORE_REMIND_USER = "team_before_remind_user";
  public static final String TEAM_BEFORE_REMIND_USER_MINUTES = "team_before_remind_user_minutes";
  public static final String TEAM_COURSE_REMIND_TEACHER = "team_course_remind_teacher";
  public static final String TEAM_COURSE_REMIND_TEACHER_MINUTES =
      "team_course_remind_teacher_minutes";
  public static final String PRIVATE_MINUTES_CAN_ORDER = "private_minutes_can_order";
  public static final String PRIVATE_MINUTES_CAN_CANCEL = "private_minutes_can_cancel";
  public static final String PRIVATE_SMS_TEACHER_AFTER_USER_ORDER_SAVE =
      "private_sms_teacher_after_user_order_save";
  public static final String PRIVATE_SMS_USER_AFTER_USER_ORDER_SAVE =
      "private_sms_user_after_user_order_save";
  public static final String PRIVATE_SMS_TEACHER_AFTER_USER_ORDER_CANCEL =
      "private_sms_teacher_after_user_order_cancel";
  public static final String PRIVATE_SMS_USER_AFTER_USER_ORDER_CANCEL =
      "private_sms_user_after_user_order_cancel";
  public static final String PRIVATE_SMS_USER_AFTER_TEACHER_ORDER_SAVE =
      "private_sms_user_after_teacher_order_save";
  public static final String PRIVATE_SMS_TEACHER_AFTER_TEACHER_ORDER_SAVE =
      "private_sms_teacher_after_teacher_order_save";
  public static final String PRIVATE_SMS_TEACHER_AFTER_TEACHER_ORDER_CANCEL =
      "private_sms_teacher_after_teacher_order_cancel";
  public static final String PRIVATE_SMS_USER_AFTER_TEACHER_ORDER_CANCEL =
      "private_sms_user_after_teacher_order_cancel";
  public static final String PRIVATE_BEFORE_REMIND_USER = "private_before_remind_user";
  public static final String PRIVATE_BEFORE_REMIND_USER_MINUTES =
      "private_before_remind_user_minutes";
  //非法配置
  //public static final String PRIVATE_COURSE_REMIND_TEACHER = "private_course_remind_teacher";
  //public static final String PRIVATE_COURSE_REMIND_TEACHER_MINUTES = "private_course_remind_teacher_minutes";

  public static final String BEFORE_MINUTES_CAN_SIGN = "before_minutes_can_sign";
  public static final String AFTER_MINUTES_CAN_SIGN = "after_minutes_can_sign";
  public static final String PRIVATE_SLICE_MINUTES = "private_slice_minutes";
  public static final String VALUE_CARD_REMIND = "value_card_remind";
  public static final String TIMES_CARD_REMIND = "times_card_remind";
  public static final String SMS_VERIFY_CODE = "sms_verify_code";
  public static final String TIME_CARD_REMIND = "time_card_remind";
  public static final String SEND_SMS_AFTER_CHARGE_CARD = "send_sms_after_charge_card";
  public static final String WITHDRAW_LIMIT = "withdraw_limit";
  public static final String WITHDRAW_ENABLE = "withdraw_enable";
  public static final String PAY_BY_QRCODE = "pay_by_qrcode";
  public static final String USER_CHECKIN_WITH_LOCKER = "user_checkin_with_locker";
  public static final String CHECK_OUT_WITH_RETURN_LOCKER = "check_out_with_return_locker";
  public static final String IS_SET_USER_PHOTOS = "is_set_user_photos";
  public static final String SMS_NUM = "sms_num";

  public static final String NOTI_TO_STAFF = "notify_to_staff_method";
  public static final String NOTIFY_TO_MEMBER_METHOD = "notify_to_member_method";

  //签课
  public static final String GROUP_SIGN_CLASS_OPEN = "group_course_check_in_is_open";
  public static final String GROUP_SIGN_CLASS_WAY = "group_course_check_in_type";
  public static final String GROUP_SIGN_CLASS_START = "group_before_minutes_can_check_in";
  public static final String GROUP_SIGN_CLASS_END = "group_after_minutes_can_check_in";
  public static final String PRIVATE_SIGN_CLASS_OPEN = "private_course_check_in_is_open";
  public static final String PRIVATE_SIGN_CLASS_WAY = "private_course_check_in_type";
  public static final String PRIVATE_SIGN_CLASS_START = "private_before_minutes_can_check_in";
  public static final String PRIVATE_SIGN_CLASS_END = "private_after_minutes_can_check_in";
}
