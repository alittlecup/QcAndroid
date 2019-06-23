package cn.qingchengfit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.QcResponseSaleDetail;
import cn.qingchengfit.model.responese.SigninReportDetail;
import cn.qingchengfit.model.responese.TeacherImpression;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.MainActivity;
import cn.qingchengfit.staffkit.R;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

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
 * Created by Paper on 16/7/6 2016.
 */
public class BusinessUtils {

    public static void reOpenApp(Context context) {
        try {
            Intent toMain = new Intent(context, MainActivity.class);
            toMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            toMain.putExtra(MainActivity.MAIN_ACTION, -1);
            context.startActivity(toMain);
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        } catch (Exception e) {
            Timber.e("重新打开App失败");
        }
    }

    public static String getStudentStatus(int s) {
        switch (s) {
            case 0:
                return "新注册";
            case 1:
                return "已接洽";
            case 2:
                return "会员";
            default:
                return "未知状态";
        }
    }

    /**
     * 获取交易类型
     */
    public static List<String> getTradeTypes(Context context, List<Integer> trades) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < trades.size(); i++) {
            ret.add(getTradeType(context, trades.get(i)));
        }
        return ret;
    }

    public static int getTradeTypeServer(int i) {
        switch (i) {
            case 0:
                return Configs.TRADE_CHARGE;
            case 1:
                return Configs.TRADE_CHARGE_FIRST;
            case 3:
                return Configs.TRADE_PRESENT;
            case 2:
                return Configs.TRADE_REFUND;
            default:
                return Configs.TRADE_CHARGE;
        }
    }

    /**
     * 获取交易类型
     */
    public static String getTradeType(Context context, int i) {
        switch (i) {
            case Configs.TRADE_CHARGE_FIRST:
                return context.getString(R.string.new_buy_card);
            case Configs.TRADE_CHARGE:
                return context.getString(R.string.charge);
            case Configs.TRADE_PRESENT:
                return context.getString(R.string.present);
            case Configs.TRADE_REFUND:
                return context.getString(R.string.refund);
            default:
                return context.getString(R.string.charge);
        }
    }

    /**
     * 转换支付方式
     */
    public static String getPayMethod(Context cont, int i) {
        switch (i) {
            case Configs.CHARGE_MODE_CARD:
                return cont.getString(R.string.credit_pay);
            case Configs.CHARGE_MODE_CASH:
                return cont.getString(R.string.cash_pay);
            case Configs.CHARGE_MODE_TRANSFER:
                return cont.getString(R.string.transit_pay);
            case Configs.CHARGE_MODE_WEIXIN:
                return cont.getString(R.string.wechat_pay);
            case Configs.CHARGE_MODE_WEIXIN_QRCODE:
                return cont.getString(R.string.wechat_code);
            default:
                return cont.getString(R.string.other);
        }
    }

    public static int getPayMethodServer(int i) {
        switch (i) {
            case 0:
                return Configs.CHARGE_MODE_WEIXIN_QRCODE;
            case 1:
                return Configs.CHARGE_MODE_WEIXIN;
            case 2:
                return Configs.CHARGE_MODE_CASH;
            case 3:
                return Configs.CHARGE_MODE_CARD;
            case 4:
                return Configs.CHARGE_MODE_TRANSFER;
            case 5:
                return Configs.CHARGE_MODE_OTHER;
            case 6:
                return Configs.CHARGE_MODE_AUTO;
            default:
                return Configs.CHARGE_MODE_OTHER;
        }
    }

    public static List<String> getPayMethods(Context context, List<Integer> integers) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < integers.size(); i++) {
            ret.add(getPayMethod(context, integers.get(i)));
        }
        return ret;
    }

    public static List<String> students2strs(List<StudentBean> students) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            ret.add(students.get(i).getUsername());
        }

        return ret;
    }
public static List<String> qcstudents2strs(List<QcStudentBean> students) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            ret.add(students.get(i).getUsername());
        }

        return ret;
    }

    public static List<String> students2ids(List<QcStudentBean> students) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            ret.add(students.get(i).id());
        }

        return ret;
    }

    public static List<String> userBean2strs(List<SigninReportDetail.CheckinsBean.UserBean> students) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            ret.add(students.get(i).getUsername());
        }

        return ret;
    }

    public static String students2str(List<StudentBean> students) {
        String ret = "";
        for (int i = 0; i < students.size(); i++) {
            ret = ret.concat(TextUtils.isEmpty(ret) ? "" : "、").concat(students.get(i).getUsername());
        }

        return ret;
    }

    public static String qcstudents2str(List<QcStudentBean> students) {
        String ret = "";
        for (int i = 0; i < students.size(); i++) {
            ret = ret.concat(TextUtils.isEmpty(ret) ? "" : "、").concat(students.get(i).getUsername());
        }

        return ret;
    }

    public static String studentIds2str(List<StudentBean> students) {
        String ret = "";
        for (int i = 0; i < students.size(); i++) {
            ret = ret.concat(TextUtils.isEmpty(ret) ? "" : "、").concat(students.get(i).id);
        }

        return ret;
    }

    public static String qcstudentIds2str(List<QcStudentBean> students) {
        String ret = "";
        for (int i = 0; i < students.size(); i++) {
            ret = ret.concat(TextUtils.isEmpty(ret) ? "" : "、").concat(students.get(i).id);
        }

        return ret;
    }

    public static List<String> qcstudentIds2List(List<QcStudentBean> students) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            ret.add(students.get(i).getId());
        }

        return ret;
    }

    public static List<String> qcstudentIds2ListChat(List<QcStudentBean> students, Context context) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            ret.add(context.getString(R.string.chat_user_id_header, students.get(i).getId()));
        }

        return ret;
    }



    public static List<String> coach2Strs(List<Staff> coaches) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < coaches.size(); i++) {
            ret.add(coaches.get(i).username);
        }
        return ret;
    }

    public static List<String> coach2ids(List<Staff> coaches) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < coaches.size(); i++) {
            ret.add(coaches.get(i).id);
        }
        return ret;
    }

    public static List<String> teacher2Str(List<Staff> teachers) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < teachers.size(); i++) {
            ret.add(teachers.get(i).username);
        }
        return ret;
    }

    public static List<String> saler2Str(List<Staff> salers) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < salers.size(); i++) {
            if (salers.get(i).username != null) ret.add(salers.get(i).username);
        }
        return ret;
    }

    public static List<String> impress2Str(List<TeacherImpression> impressions) {

        List<String> ret = new ArrayList<>();
        if (impressions == null) return ret;
        for (int i = 0; i < impressions.size(); i++) {
            ret.add(impressions.get(i).comment);
        }
        return ret;
    }

    public static List<String> shop2Str(List<Shop> shops) {
        List<String> ret = new ArrayList<>();
        if (shops == null) return ret;
        for (int i = 0; i < shops.size(); i++) {
            ret.add(shops.get(i).name);
        }
        return ret;
    }

    public static List<CardTpl> card2Card_tpl(@NonNull List<QcResponseSaleDetail.Card> cards) {
        List<CardTpl> ret = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            ret.add(new CardTpl(cards.get(i).name, cards.get(i).card_type, "", cards.get(i).card_tpl_id, ""));
        }
        return ret;
    }

    public static List<CardTpl> card2Card_tpl2(@NonNull List<SigninReportDetail.CheckinsBean.CardBean> cards) {
        List<CardTpl> ret = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            ret.add(new CardTpl(cards.get(i).getName(), cards.get(i).getCard_tpl_type(), "", cards.get(i).getCard_tpl_id(), ""));
        }
        return ret;
    }

    public static String service2strs(List<CoachService> coachServices) {
        if (coachServices == null) return "";
        String ret = "";
        for (int i = 0; i < coachServices.size(); i++) {
            ret = ret.concat(coachServices.get(i).getName()).concat(",");
        }
        return ret;
    }

    public static String service2ids(List<CoachService> coachServices) {
        if (coachServices == null) return "";
        String ret = "";
        for (int i = 0; i < coachServices.size(); i++) {
            ret = ret.concat(coachServices.get(i).getName()).concat(",");
        }
        return ret;
    }

    public static <T extends Personage> List<String> PersonIdsExSu(List<T> students) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            if (!students.get(i).is_superuser) {
                ret.add(students.get(i).getId());
            }
        }
        return ret;
    }

    public static String getOrderPayChannel(int x) {
        switch (x) {
            case Configs.DEAL_ADMIN:
                return "人工操作";
            case Configs.DEAL_ALIPAY:
                return "在线支付（支付宝）";
            case Configs.DEAL_CARD:
            case Configs.DEAL_CASH:
                return "线下续费）";
            case Configs.DEAL_WECHAT:
            case Configs.DEAL_WEIXIN_QR:
                return "在线支付（微信支付）";
            default:
                return "";
        }
    }
}
