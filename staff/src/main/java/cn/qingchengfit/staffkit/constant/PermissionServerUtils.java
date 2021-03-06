package cn.qingchengfit.staffkit.constant;

import java.util.HashMap;

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
 * Created by Paper on 16/8/10.
 */
public class PermissionServerUtils {

    /**
     * 场馆管理
     */
    public static final String STUDIO_LIST = "studio_list";
    public static final String STUDIO_LIST_CAN_WRITE = "studio_list_can_write";
    public static final String STUDIO_LIST_CAN_CHANGE = "studio_list_can_change";
    public static final String STUDIO_LIST_CAN_DELETE = "studio_list_can_delete";
    public static final String PERMISSIONSETTING = "permissionsetting";
    public static final String PERMISSIONSETTING_CAN_WRITE = "permissionsetting_can_write";
    public static final String PERMISSIONSETTING_CAN_CHANGE = "permissionsetting_can_change";
    public static final String PERMISSIONSETTING_CAN_DELETE = "permissionsetting_can_delete";

    public static final String MANAGE_STAFF = "manage_staff";
    public static final String MANAGE_STAFF_CAN_WRITE = "manage_staff_can_write";
    public static final String MANAGE_STAFF_CAN_CHANGE = "manage_staff_can_change";
    public static final String MANAGE_STAFF_CAN_DELETE = "manage_staff_can_delete";
    public static final String COACHSETTING = "coachsetting";
    public static final String COACHSETTING_CAN_WRITE = "coachsetting_can_write";
    public static final String COACHSETTING_CAN_CHANGE = "coachsetting_can_change";
    public static final String COACHSETTING_CAN_DELETE = "coachsetting_can_delete";
    //public static final String PERSONAL_MANAGE_MEMBERS = "personal_manage_members";
    //public static final String PERSONAL_MANAGE_MEMBERS_CAN_WRITE = "personal_manage_members_can_write";
    //public static final String PERSONAL_MANAGE_MEMBERS_CAN_CHANGE = "personal_manage_members_can_change";
    //public static final String PERSONAL_MANAGE_MEMBERS_CAN_DELETE = "personal_manage_members_can_delete";
    public static final String MANAGE_MEMBERS = "manage_members";
    public static final String MANAGE_MEMBERS_CAN_WRITE = "manage_members_can_write";
    public static final String MANAGE_MEMBERS_CAN_CHANGE = "manage_members_can_change";
    public static final String MANAGE_MEMBERS_CAN_DELETE = "manage_members_can_delete";
    public static final String MANAGE_MEMBERS_FOLLOW_STATUS_CAN_EDIT = "manage_members_follow_status_can_edit";
    public static final String MANAGE_COSTS = "manage_costs"; //会员卡
    public static final String MANAGE_MEMBERS_IS_ALL = "manage_members_is_all"; //
    public static final String MANAGE_COSTS_CAN_WRITE = "manage_costs_can_write";
    public static final String MANAGE_COSTS_CAN_CHANGE = "manage_costs_can_change";
    //public static final String PERSONAL_CARD_LIST = "personal_card_list";
    //public static final String PERSONAL_CARD_LIST_CAN_WRITE = "personal_card_list_can_write";
    //public static final String PERSONAL_CARD_LIST_CAN_CHANGE = "personal_card_list_can_change";
    public static final String CARDSETTING = "cardsetting";
    public static final String CARDSETTING_CAN_WRITE = "cardsetting_can_write";
    public static final String CARDSETTING_CAN_CHANGE = "cardsetting_can_change";
    public static final String CARDSETTING_CAN_DELETE = "cardsetting_can_delete";
    public static final String CARDBALANCE = "cardbalance";
    public static final String CARDBALANCE_CAN_CHANGE = "cardbalance_can_change";
    public static final String GIFTCARD = "giftcard";
    public static final String GIFTCARD_CAN_WRITE = "giftcard_can_write";
    public static final String GIFTCARD_CAN_CHANGE = "giftcard_can_change";
    public static final String TEAMARRANGE_CALENDAR = "teamarrange_calendar";
    public static final String TEAMARRANGE_CALENDAR_CAN_WRITE = "teamarrange_calendar_can_write";
    public static final String TEAMARRANGE_CALENDAR_CAN_CHANGE = "teamarrange_calendar_can_change";
    public static final String TEAMARRANGE_CALENDAR_CAN_DELETE = "teamarrange_calendar_can_delete";
    public static final String TEAMSETTING = "teamsetting";
    public static final String TEAMSETTING_CAN_WRITE = "teamsetting_can_write";
    public static final String TEAMSETTING_CAN_CHANGE = "teamsetting_can_change";
    public static final String TEAMSETTING_CAN_DELETE = "teamsetting_can_delete";
    public static final String PRIARRANGE_CALENDAR = "priarrange_calendar";
    public static final String PRIARRANGE_CALENDAR_CAN_WRITE = "priarrange_calendar_can_write";
    public static final String PRIARRANGE_CALENDAR_CAN_CHANGE = "priarrange_calendar_can_change";
    public static final String PRIARRANGE_CALENDAR_CAN_DELETE = "priarrange_calendar_can_delete";
    public static final String PRISETTING = "prisetting";
    public static final String PRISETTING_CAN_WRITE = "prisetting_can_write";
    public static final String PRISETTING_CAN_CHANGE = "prisetting_can_change";
    public static final String PRISETTING_CAN_DELETE = "prisetting_can_delete";
    public static final String ORDERS_DAY = "orders_day";
    public static final String ORDERS_DAY_CAN_WRITE = "orders_day_can_write";
    public static final String ORDERS_DAY_CAN_CHANGE = "orders_day_can_change";
    public static final String ORDERS_DAY_CAN_DELETE = "orders_day_can_delete";
    //public static final String PERSONAL_ORDERS_LIST = "personal_orders_list";
    //public static final String PERSONAL_ORDERS_LIST_CAN_WRITE = "personal_orders_list_can_write";
    //public static final String PERSONAL_ORDERS_LIST_CAN_CHANGE = "personal_orders_list_can_change";
    //public static final String PERSONAL_ORDERS_LIST_CAN_DELETE = "personal_orders_list_can_delete";
    public static final String PRIVATE_ORDER = "private_order";
    public static final String PRIVATE_ORDER_CAN_WRITE = "private_order_can_write";

    public static final String CHECKIN_HELP = "checkin_help";
    public static final String CHECKIN_HELP_CAN_WRITE = "checkin_help_can_write";
    public static final String CHECKIN_REPORT = "checkin_report";
    public static final String LOCKER_SETTING = "locker_setting";
    public static final String LOCKER_SETTING_CAN_WRITE = "locker_setting_can_write";
    public static final String LOCKER_SETTING_CAN_CHANGE = "locker_setting_can_change";
    public static final String LOCKER_SETTING_CAN_DELETE = "locker_setting_can_delete";
    public static final String CHECKIN_LIST = "checkin_list";
    public static final String CHECKIN_LIST_CAN_CHANGE = "checkin_help_can_change";
    public static final String CHECKIN_LIST_CAN_DEL = "checkin_help_can_delete";
    //public static final String PERSONAL_CHECKIN_LIST = "personal_checkin_list";
    //public static final String PERSONAL_CHECKIN_LIST_CAN_CHANGE = "personal_checkin_list_can_change";
    public static final String CHECKIN_SETTING = "checkin_setting";
    public static final String CHECKIN_SETTING_CAN_CHANGE = "checkin_setting_can_change";
    public static final String ACTIVITY_SETTING = "activity_setting";
    public static final String ACTIVITY_SETTING_CAN_WRITE = "activity_setting_can_write";
    public static final String ACTIVITY_SETTING_CAN_CHANGE = "activity_setting_can_change";
    public static final String COMMODITY_LIST = "commodity_list";
    public static final String COMMODITY_CATEGORY = "commodity_category";
    public static final String COMMODITY_MARKET = "commodity_market";//商品销售
    public static final String COMMODITY_LIST_CAN_WRITE = "commodity_list_can_write";
    public static final String COMMODITY_INVENTORY = "commodity_inventory";
    public static final String COMMODITY_INVENTORY_CAN_WRITE = "commodity_inventory_can_write";
    public static final String COMMODITY_INVENTORY_CAN_CHANGE = "commodity_inventory_can_change";
    public static final String COMMODITY_REPORTS = "commodity_reports";
    public static final String COMMODITY_REPORTS_CAN_DELETE = "commodity_reports_can_delete";
    public static final String CLASS_REPORT = "class_report";
    //public static final String PERSONAL_CLASS_REPORT = "personal_class_report";
    public static final String SALES_REPORT = "sales_report";
    //public static final String PERSONAL_SALES_REPORT = "personal_sales_report";
    public static final String COST_REPORT = "cost_report";
    public static final String CARD_REPORT = "card_report";
    public static final String MEASURESETTING = "measuresetting";
    public static final String MEASURESETTING_CAN_WRITE = "measuresetting_can_write";
    public static final String MEASURESETTING_CAN_CHANGE = "measuresetting_can_change";
    public static final String MEASURESETTING_CAN_DELETE = "measuresetting_can_delete";
    public static final String PLANSSETTING = "planssetting";
    public static final String PLANSSETTING_CAN_WRITE = "planssetting_can_write";
    public static final String PLANSSETTING_CAN_CHANGE = "planssetting_can_change";
    public static final String PLANSSETTING_CAN_DELETE = "planssetting_can_delete";
    public static final String WECHATSETTING = "wechatsetting";
    public static final String COMMENTS_REPORT = "comments_report";
    public static final String NOTICE = "notice";
    public static final String NOTICE_CAN_WRITE = "notice_can_write";
    public static final String NOTICE_CAN_CHANGE = "notice_can_change";
    public static final String PAY_BILLS = "pay_bills";
    public static final String PAY_BILLS_CAN_WRITE = "pay_bills_can_write";
    public static final String PAY_SETTING = "pay_setting";
    public static final String PAY_SETTING_CAN_WRITE = "pay_setting_can_write";
    public static final String PAY_SETTING_CAN_DELETE = "pay_setting_can_delete";
    public static final String MOBILE_ADVERTISEMENT_SETTING = "mobile_advertisement_setting";
    public static final String MOBILE_ADVERTISEMENT_SETTING_CAN_WRITE = "mobile_advertisement_setting_can_write";
    public static final String MOBILE_ADVERTISEMENT_SETTING_CHANGE = "mobile_advertisement_setting_change";
    public static final String MOBILE_ADVERTISEMENT_SETTING_CAN_DELETE = "mobile_advertisement_setting_can_delete";
    public static final String KOUBEI = "koubei";
    public static final String KOUBEI_CAN_WRITE = "koubei_can_write";
    public static final String KOUBEI_CAN_CHANGE = "koubei_can_change";
    public static final String SCORE_SETTING = "score_setting";
    public static final String SCORE_SETTING_CAN_CHANGE = "score_setting_can_change";
    public static final String SCORE_RANK = "score_rank";

    /**
     * 团课预约限制
     */
    public static final String TEAM_COURSE_LIMIT = "team_course_limit";
    public static final String TEAM_COURSE_LIMIT_CAN_WRITE = "team_course_limit_can_change";
    /**
     * * 团课短信通知设置 team_course_msg_setting
     */
    public static final String TEAM_COURSE_MSG_SETTING = "team_course_msg_setting";
    public static final String TEAM_COURSE_MSG_SETTING_CAN_CHANGE = "team_course_msg_setting_can_change";

    /**
     * * 私教预约限制 private_course_limit
     */
    public static final String PRIVATE_COURSE_LIMIT = "private_course_limit";
    public static final String PRIVATE_COURSE_LIMIT_CAN_CHANGE = "private_course_limit_can_change";

    /**
     * * 私教短信通知设置 private_course_msg_setting
     */
    public static final String PRIVATE_COURSE_MSG_SETTING = "private_course_msg_setting";
    public static final String PRIVATE_COURSE_MSG_SETTING_CAN_CHANGE = "private_course_msg_setting_can_change";

    /**
     * 职位与权限 工作人员权限
     */
    public static final String POSITION_SETTING = "position_setting";
    public static final String POSITION_SETTING_CAN_WRITE = "position_setting_can_write";
    public static final String POSITION_SETTING_CAN_CHANGE = "position_setting_can_change";
    public static final String POSITION_SETTING_CAN_DELETE = "position_setting_can_delete";

    /**
     * 教练权限管理
     */
    public static final String COACH_PERMISSION_SETTING = "coach_permission_setting";
    public static final String COACH_PERMISSION_SETTING_CAN_CHANGE = "coach_permission_setting_can_change";

    /**
     * 场地管理 space_setting
     */
    public static final String SPACE_SETTING = "space_setting";
    public static final String SPACE_SETTING_CAN_WRITE = "space_setting_can_write";
    public static final String SPACE_SETTING_CAN_CHANGE = "space_setting_can_change";
    public static final String SPACE_SETTING_CAN_DELETE = "space_setting_can_delete";

    /**
     * 会员端页面配置
     */
    public static final String SHOP_HOME_SETTING = "shop_home_setting";
    public static final String SHOP_HOME_SETTING_CAN_CHANGE = "shop_home_setting_can_change";

    /**
     * 签到更衣柜联动
     */
    public static final String CHECKIN_LOCKER_LINK_NEW = "locker_distribute";
    public static final String CHECKIN_LOCKER_LINK_CAN_CHANGE_NEW = "locker_distribute_setting_can_change";

    /**
     * 签到大屏幕
     */
    public static final String CHECKIN_SCREEN = "checkin_screen";
    public static final String CHECKIN_SCREEN_CAN_WRITE = "checkin_screen_can_write";
    public static final String CHECKIN_SCREEN_CAN_CHANGE = "checkin_screen_can_change";
    /**
     * 通知权限
     */
    public static final String MESSAGESETTING = "messagesetting";
    public static final String MESSAGEDETAILS = "messagedetails";
    public static final String MESSAGECHANNELS = "messagechannels";
    public static final String MESSAGESETTING_CAN_WRITE = "messagechannels_can_write";

    //导入导出
    public static final String STUDENT_EXPORT = "manage_members_export";
    public static final String STUDENT_IMPORT = "manage_members_import";
    public static final String CARD_EXPORT = "manage_cards_export";
    public static final String CARD_IMPORT = "manage_cards_export";


    public static boolean checkServerPermission(HashMap<String, Boolean> permission, String key) {
        return permission.get(key);
    }
}
