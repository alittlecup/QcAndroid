package cn.qingchengfit.staffkit.views.gym;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventInitApp;
import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.GymFuntion;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.qrcode.views.QRActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.export.ImportExportActivity;
import cn.qingchengfit.staffkit.views.schedule.ScheduleActivity;
import cn.qingchengfit.staffkit.views.signin.SignInActivity;
import cn.qingchengfit.staffkit.views.statement.ContainerActivity;
import cn.qingchengfit.staffkit.views.student.StudentActivity;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;

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
 * Created by Paper on 2017/1/16.
 */

public class GymFunctionFactory {

    public static final String MODULE_NONE = "";
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
    @Inject SerPermisAction serPermisAction;//// TODO: 2017/8/14 修改成注入

    @Inject
    public GymFunctionFactory() {
    }

    //导入导出
    public static final String REPORT_EXPORT = "/report/export";

    public static final String STUDENT_IMPORT = "/manage/members/import";
    public static final String STUDENT_EXPORT = "/manage/members/export";
    public static final String CARD_IMPORT = "/cards/import";
    public static final String CARD_EXPORT = "/cards/export";

    //用户协议
    public static final String USER_PROTOCOL = "/user/protocol";
    public static final String USER_PROTOCOL_URL = "/protocol/staff/";

    public static GymFuntion instanceGymFuntion(String module) {
        GymFuntion gym = new GymFuntion.Builder().moduleName(module).text(getModuleTxt(module)).img(getDrawableRes(module)).build();
        return gym;
    }

    @DrawableRes private static int getDrawableRes(String module) {
        switch (module.toLowerCase()) {
            case MODULE_NONE:
                return 0;
            case MODULE_WORKSPACE_ORDER_LIST:
                return R.drawable.module_workbench_orders;
            case MODULE_WORKSPACE_GROUP:
                return R.drawable.moudule_order_group;
            case MODULE_WORKSPACE_PRIVATE:
                return R.drawable.moudule_order_private;
            case MODULE_WORKSPACE_ORDER_SIGNIN:
                return R.drawable.module_workbench_checkin;
            case MODULE_FINACE_ONLINE:
                return R.drawable.moudule_fi_online;
            case MODULE_FINANCE_CARD:
                return R.drawable.moudule_fi_card_statement;
            case MODULE_FINANCE_COURSE:
                return R.drawable.moudule_fi_course_statement;
            case MODULE_FINANCE_MARK:
                return R.drawable.moudule_fi_mark_statement;
            case MODULE_FINANCE_SALE:
                return R.drawable.moudule_fi_sale_statement;
            case MODULE_FINANCE_SIGN_IN:
                return R.drawable.moudule_fi_signin_statement;

            case MODULE_OPERATE_ACTIVITY:
                return R.drawable.moudule_op_activity;
            case MODULE_OPERATE_AD:
                return R.drawable.moudule_op_ad;
            case MODULE_OPERATE_ANOUNCE:
                return R.drawable.moudule_op_anounce;
            case MODULE_OPERATE_REGIST:
                return R.drawable.moudule_op_registe;
            case MODULE_OPERTAT_KOUBEI:
                return R.drawable.moudule_op_koubei;
            case MODULE_OPERATE_SCORE:
                return R.drawable.moudule_op_score;
            case MODULE_OPERATE_COMPETITION:
                return R.drawable.moudule_op_competition;

            case MODULE_MANAGE_COACH:
                return R.drawable.moudule_manage_trainer;
            case MODULE_MANAGE_STAFF:
                return R.drawable.moudule_manage_staff;

            case MODULE_SERVICE_FREE:
                return R.drawable.moudule_service_free_train;
            case MODULE_SERVICE_GROUP:
                return R.drawable.moudule_service_group;
            case MODULE_SERVICE_PRIVATE:
                return R.drawable.moudule_service_private;
            case MODULE_SERVICE_SHOP:
                return R.drawable.moudule_service_shop;

            case MODULE_WARDROBE:
                return R.drawable.moudule_gym_wardrobe;
            case MODULE_GYM_INFO:
                return R.drawable.moudule_gym_info;
            case MODULE_GYM_TIME:
                return R.drawable.moudule_gym_time;
            case MODULE_GYM_SITE:
                return R.drawable.moudule_gym_site;
            case MODULE_HOME:
                return R.drawable.moudule_gym_homepage;
            case MODULE_MSG:
                return R.drawable.moudule_gym_msg;
            case MODULE_WECHAT:
                return R.drawable.moudule_gym_wechat;

            case MODULE_STUDENT:
                return R.drawable.moudule_student;
            case MODULE_STUDENT_CARDS:
                return R.drawable.moudule_student_card;
            case MODULE_STUDENT_BODY_TEST:
                return R.drawable.moudule_student_body_test;
            case REPORT_EXPORT:
                return R.drawable.ic_module_report_export;
            default:
                return 0;
        }
    }

    public static int getModuleStatus(String module) {
        if (module == null) return 0;
        switch (module.toLowerCase()) {
            case MODULE_STUDENT_CARDS:
            case MODULE_MANAGE_STAFF:
            case MODULE_FINANCE_CARD:
            case MODULE_FINANCE_MARK:
            case MODULE_FINANCE_SALE:
            case MODULE_FINANCE_SIGN_IN:
            case MODULE_OPERATE_ACTIVITY:
            case MODULE_OPERATE_AD:
            case MODULE_OPERATE_ANOUNCE:
            case MODULE_OPERATE_REGIST:
            case MODULE_OPERTAT_KOUBEI:
            case MODULE_OPERATE_SCORE:
            case MODULE_SERVICE_SHOP:
            case MODULE_WECHAT:
            case MODULE_GYM_SITE:
                return 1;

            case MODULE_WORKSPACE_ORDER_LIST:
            case MODULE_WORKSPACE_GROUP:
            case MODULE_WORKSPACE_PRIVATE:
            case MODULE_WORKSPACE_ORDER_SIGNIN:
            case MODULE_MANAGE_COACH:
            case MODULE_SERVICE_FREE:
            case MODULE_SERVICE_GROUP:
            case MODULE_SERVICE_PRIVATE:
            case MODULE_WARDROBE:
            case MODULE_GYM_INFO:
            case MODULE_GYM_TIME:
            case MODULE_HOME:
            case MODULE_MSG:
            case MODULE_STUDENT:
            case MODULE_STUDENT_BODY_TEST:
            case MODULE_NONE:
            case MODULE_FINACE_ONLINE:
            case MODULE_FINANCE_COURSE:
            default:
                return 0;
        }
    }

    @StringRes private static int getModuleTxt(String module) {
        switch (module.toLowerCase()) {
            case MODULE_NONE:
                return R.string.none;
            case MODULE_WORKSPACE_ORDER_LIST:
                return R.string.module_workspace_order;
            case MODULE_WORKSPACE_GROUP:
                return R.string.module_workspace_group;
            case MODULE_WORKSPACE_PRIVATE:
                return R.string.module_workspace_private;
            case MODULE_WORKSPACE_ORDER_SIGNIN:
                return R.string.module_workspace_signin;
            case MODULE_FINACE_ONLINE:
                return R.string.module_fi_online;
            case MODULE_FINANCE_CARD:
                return R.string.module_fi_card_statement;
            case MODULE_FINANCE_COURSE:
                return R.string.module_fi_course_statement;
            case MODULE_FINANCE_MARK:
                return R.string.module_fi_mark_statement;
            case MODULE_FINANCE_SALE:
                return R.string.module_fi_sale_statement;
            case MODULE_FINANCE_SIGN_IN:
                return R.string.module_fi_signin_statement;

            case MODULE_OPERATE_ACTIVITY:
                return R.string.module_op_acitivty;
            case MODULE_OPERATE_AD:
                return R.string.module_op_ad;
            case MODULE_OPERATE_ANOUNCE:
                return R.string.module_op_anounce;
            case MODULE_OPERATE_REGIST:
                return R.string.module_op_registe;
            case MODULE_OPERTAT_KOUBEI:
                return R.string.module_op_koubei;
            case MODULE_OPERATE_SCORE:
                return R.string.module_op_score;
            case MODULE_OPERATE_COMPETITION:
                return R.string.competition;

            case MODULE_MANAGE_COACH:
                return R.string.module_manage_trainer;
            case MODULE_MANAGE_STAFF:
                return R.string.module_manage_staff;

            case MODULE_SERVICE_FREE:
                return R.string.module_service_free;
            case MODULE_SERVICE_GROUP:
                return R.string.module_service_group;
            case MODULE_SERVICE_PRIVATE:
                return R.string.module_service_private;
            case MODULE_SERVICE_SHOP:
                return R.string.module_service_shop;

            case MODULE_WARDROBE:
                return R.string.module_gym_wardrobe;
            case MODULE_GYM_INFO:
                return R.string.module_gym_info;
            case MODULE_GYM_TIME:
                return R.string.module_gym_time;
            case MODULE_GYM_SITE:
                return R.string.module_gym_site;
            case MODULE_HOME:
                return R.string.module_gym_homepage;
            case MODULE_MSG:
                return R.string.module_gym_msg;
            case MODULE_WECHAT:
                return R.string.module_gym_wechat;

            case MODULE_STUDENT:
                return R.string.module_student;
            case MODULE_STUDENT_CARDS:
                return R.string.module_student_cards;
            case MODULE_STUDENT_BODY_TEST:
                return R.string.module_student_bodytest;
            case REPORT_EXPORT:
                return R.string.module_export_name;

            default:
                return R.string.none;
        }
    }

    public  void getJumpIntent(String module, CoachService coachService, Brand brand, GymStatus gymStatus, BaseFragment fragment) {
      if (coachService == null) {
        RxBus.getBus().post(new EventInitApp());
        return;
      }
        switch (module) {
            /****健身服务
             * 团课  私教 自由训练 商店
             *
             */
            case MODULE_SERVICE_PRIVATE:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.PRIARRANGE_CALENDAR)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }
                fragment.routeTo("course","/batches/private/list/",null);
                return;
            case MODULE_SERVICE_GROUP:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.TEAMARRANGE_CALENDAR)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }
                fragment.routeTo("course","/batches/group/list/",null);
                return;

            case MODULE_SERVICE_FREE:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.CHECKIN_HELP)
                    && !serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.CHECKIN_LOCKER_LINK)
                    && !serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.CHECKIN_LOCKER_LINK_NEW)
                    && !serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.CHECKIN_SCREEN)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }
                Intent toSignInConfigs = new Intent(fragment.getActivity(), SignInActivity.class);
                toSignInConfigs.setAction(fragment.getString(R.string.qc_action));
                toSignInConfigs.setData(Uri.parse("checkin/config"));
              toSignInConfigs.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                fragment.startActivity(toSignInConfigs);
                return;
            case MODULE_SERVICE_SHOP:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.COMMODITY_INVENTORY)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }
                goQrScan(fragment, module, null, coachService);
                return;

            /**会员管理
             * 会员  会员卡  会员体测
             *
             */
            case MODULE_STUDENT:
                Intent toStu = new Intent(fragment.getActivity(), StudentActivity.class);
                fragment.startActivity(toStu);
                return;
            case REPORT_EXPORT:
                Intent toExo = new Intent(fragment.getActivity(), ImportExportActivity.class);
                toExo.putExtra("type", ImportExportActivity.TYPE_RECORD);
                fragment.startActivity(toExo);
                return;
            /**
             * 会员卡
             */
            case MODULE_STUDENT_CARDS:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.MANAGE_COSTS)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }
                //Intent toCard = new Intent(fragment.getActivity(), CardActivity.class);
                //fragment.startActivity(toCard);
                fragment.routeTo("card","/list/home/",null);
                return;
            case MODULE_STUDENT_BODY_TEST:
                goQrScan(fragment, module, null, coachService);
                return;
            /**内部管理
             * 工作人员  教练
             */
            case MODULE_MANAGE_STAFF:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.MANAGE_STAFF)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }

                break;
            case MODULE_MANAGE_COACH:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.COACHSETTING)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }
                break;

            /** 运营推广
             * 会员积分  活动   场馆主页广告
             * 介入口碑  注册送卡 场馆公告
             */
            case MODULE_OPERATE_SCORE:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.SCORE_SETTING)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }
                break;
            case MODULE_OPERATE_ACTIVITY:
                goQrScan(fragment, module, PermissionServerUtils.ACTIVITY_SETTING, coachService);
                return;
            case MODULE_OPERATE_AD:
            case MODULE_OPERATE_ANOUNCE:
                goQrScan(fragment, module, PermissionServerUtils.NOTICE, coachService);
                return;
            case MODULE_OPERATE_REGIST:
                goQrScan(fragment, module, PermissionServerUtils.GIFTCARD, coachService);
                return;
            case MODULE_OPERTAT_KOUBEI:
                goQrScan(fragment, module, PermissionServerUtils.KOUBEI, coachService);
                return;
            case MODULE_OPERATE_COMPETITION:
                WebActivity.startWeb(Configs.Server + Configs.URL_QC_TRAIN + "?gym_id=" + coachService.getGym_id(), fragment.getContext());
                return;

            /** 财务与报表
             * 在线支付    课程报表  会员卡销售报表
             * 评分报表    会员卡报表  签到报表
             */
            case MODULE_FINACE_ONLINE:
                goQrScan(fragment, module, PermissionServerUtils.PAY_BILLS, coachService);
                return;
            case MODULE_FINANCE_COURSE:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.COST_REPORT)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }

                break;
            case MODULE_FINANCE_SALE:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.SALES_REPORT)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }

                break;
            case MODULE_FINANCE_SIGN_IN:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.CHECKIN_REPORT)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }

                break;
            case MODULE_FINANCE_MARK:
                goQrScan(fragment, module, PermissionServerUtils.COMMENTS_REPORT, coachService);
                return;
            case MODULE_FINANCE_CARD:
                goQrScan(fragment, module, PermissionServerUtils.CARD_REPORT, coachService);
                return;
            /**场馆管理
             *
             * 场馆休息     营业时间    场地
             * 短信        更衣柜       场馆主页
             * 场馆微信公众号
             *
             */
            case MODULE_GYM_SITE://场地管理
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.SPACE_SETTING)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }
                break;
            case MODULE_WARDROBE:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.LOCKER_SETTING)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }

                break;
            case MODULE_HOME:
                goQrScan(fragment, module, null, coachService);
                return;
            case MODULE_MSG:
                break;
            case MODULE_GYM_INFO:
                Intent toGymInfo = new Intent(fragment.getActivity(), GymActivity.class);
                toGymInfo.putExtra(GymActivity.GYM_TO, GymActivity.GYM_INFO);
                fragment.startActivity(toGymInfo);
                return;
            case MODULE_GYM_TIME:
                goQrScan(fragment, module, PermissionServerUtils.STUDIO_LIST, coachService);
                return;
            case MODULE_WECHAT:
                goQrScan(fragment, module, null, coachService);
                return;

            /** 工作台
             *
             * 课程预约       代约课程    代约私教
             * 入场签到
             */
            case MODULE_WORKSPACE_ORDER_LIST:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.ORDERS_DAY)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }
                fragment.startActivity(new Intent(fragment.getContext(), ScheduleActivity.class));
                return;
            case MODULE_WORKSPACE_GROUP:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.ORDERS_DAY)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }
                WebActivity.startWeb(
                    Configs.Server + Configs.SCHEDULE_GROUP + "?id=" + coachService.getId() + "&model=" + coachService.getModel(),
                    fragment.getContext());
                return;
            case MODULE_WORKSPACE_PRIVATE:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.ORDERS_DAY)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }
                WebActivity.startWeb(
                    Configs.Server + Configs.SCHEDULE_PRIVATE + "?id=" + coachService.getId() + "&model=" + coachService.getModel(),
                    fragment.getContext());

                return;
            case MODULE_WORKSPACE_ORDER_SIGNIN:
                if (!serPermisAction.check(coachService.getId(), coachService.getModel(), PermissionServerUtils.CHECKIN_HELP)) {
                    DialogUtils.showAlert(fragment.getContext(), R.string.alert_permission_forbid);
                    return;
                }
                Intent toSignIn = new Intent(fragment.getActivity(), SignInActivity.class);
                fragment.startActivity(toSignIn);
                return;
            case MODULE_MANAGE_STAFF_ADD:
                break;
            default:
                return;
        }
        Intent toStatement = new Intent(fragment.getActivity(), ContainerActivity.class);
        toStatement.putExtra("router", module);
        fragment.startActivity(toStatement);
    }

    public void goQrScan(final BaseFragment context, final String toUrl, String Permission, final CoachService mCoachService) {
        if (mCoachService.getId() == null){
            return;
        }
        if (serPermisAction.check(mCoachService.getId(), mCoachService.getModel(), Permission) || Permission == null) {

            Intent toScan = new Intent(context.getActivity(), QRActivity.class);
            toScan.putExtra(QRActivity.LINK_URL,
                Configs.Server + "app2web/?id=" + mCoachService.getId() + "&model=" + mCoachService.getModel() + "&module=" + toUrl);
            context.startActivity(toScan);
        } else {
            context.showAlert(R.string.alert_permission_forbid);
        }
    }
}
