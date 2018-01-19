package com.qingchengfit.fitcoach.Utils;

import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.bean.TeacherImpression;
import com.qingchengfit.fitcoach.http.bean.QcScheduleBean;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 获取交易类型
     *
     * @param context
     * @param trades
     * @return
     */
    //    public static List<String> getTradeTypes(Context context, List<Integer> trades) {
    //        List<String> ret = new ArrayList<>();
    //        for (int i = 0; i < trades.size(); i++) {
    //            ret.add(getTradeType(context, trades.get(i)));
    //        }
    //        return ret;
    //    }

    //    public static int getTradeTypeServer(int i) {
    //        switch (i) {
    //            case 0:
    //                return Configs.TRADE_CHARGE_FIRST;
    //            case 1:
    //                return Configs.TRADE_CHARGE;
    //            case 2:
    //                return Configs.TRADE_PRESENT;
    //            case 3:
    //                return Configs.TRADE_REFUND;
    //            default:
    //                return Configs.TRADE_CHARGE;
    //        }
    //    }

    /**
     * 获取交易类型
     *
     * @param context
     * @param i
     * @return
     */
    //    public static String getTradeType(Context context, int i) {
    //        switch (i) {
    //            case Configs.TRADE_CHARGE_FIRST:
    //                return context.getString(R.string.new_buy_card);
    //            case Configs.TRADE_CHARGE:
    //                return context.getString(R.string.charge);
    //            case Configs.TRADE_PRESENT:
    //                return context.getString(R.string.present);
    //            case Configs.TRADE_REFUND:
    //                return context.getString(R.string.refund);
    //            default:
    //                return context.getString(R.string.charge);
    //        }
    //    }

    /**
     * 转换支付方式
     */
    //    public static String getPayMethod(Context cont, int i) {
    //        switch (i) {
    //            case Configs.CHARGE_MODE_CARD:
    //                return cont.getString(R.string.credit_pay);
    //            case Configs.CHARGE_MODE_CASH:
    //                return cont.getString(R.string.cash_pay);
    //            case Configs.CHARGE_MODE_TRANSFER:
    //                return cont.getString(R.string.transit_pay);
    //            case Configs.CHARGE_MODE_WEIXIN:
    //                return cont.getString(R.string.wechat_pay);
    //            case Configs.CHARGE_MODE_WEIXIN_QRCODE:
    //                return cont.getString(R.string.wechat_code);
    //            default:
    //                return cont.getString(R.string.other);
    //        }
    //
    //    }

    //    public static int getPayMethodServer(int i) {
    //        switch (i) {
    //            case 0:
    //                return Configs.CHARGE_MODE_WEIXIN_QRCODE;
    //            case 1:
    //                return Configs.CHARGE_MODE_WEIXIN;
    //            case 2:
    //                return Configs.CHARGE_MODE_CASH;
    //            case 3:
    //                return Configs.CHARGE_MODE_CARD;
    //            case 4:
    //                return Configs.CHARGE_MODE_TRANSFER;
    //            case 5:
    //                return Configs.CHARGE_MODE_OTHER;
    //            default:
    //                return Configs.CHARGE_MODE_OTHER;
    //        }
    //    }

    //    public static List<String> getPayMethods(Context context, List<Integer> integers) {
    //        List<String> ret = new ArrayList<>();
    //        for (int i = 0; i < integers.size(); i++) {
    //            ret.add(getPayMethod(context, integers.get(i)));
    //        }
    //        return ret;
    //    }
    //
    //
    //    public static List<String> students2strs(List<StudentBean> students) {
    //        List<String> ret = new ArrayList<>();
    //        for (int i = 0; i < students.size(); i++) {
    //            ret.add(students.get(i).getUsername());
    //        }
    //
    //        return ret;
    //    }
    //
    //    public static List<String> userBean2strs(List<SigninReportDetail.CheckinsBean.UserBean> students) {
    //        List<String> ret = new ArrayList<>();
    //        for (int i = 0; i < students.size(); i++) {
    //            ret.add(students.get(i).getUsername());
    //        }
    //
    //        return ret;
    //    }
    //
    //    public static String students2str(List<StudentBean> students) {
    //        String ret = "";
    //        for (int i = 0; i < students.size(); i++) {
    //            ret = ret.concat(TextUtils.isEmpty(ret) ? "" : "、").concat(students.get(i).getUsername());
    //        }
    //
    //        return ret;
    //    }
    //public static String studentIds2str(List<StudentBean> students) {
    //        String ret = "";
    //        for (int i = 0; i < students.size(); i++) {
    //            ret = ret.concat(TextUtils.isEmpty(ret) ? "" : "、").concat(students.get(i).id);
    //        }
    //
    //        return ret;
    //    }
    //
    //    public static List<String> coach2Strs(List<Coach> coaches) {
    //        List<String> ret = new ArrayList<>();
    //        for (int i = 0; i < coaches.size(); i++) {
    //            ret.add(coaches.get(i).name);
    //        }
    //        return ret;
    //    }
    //
    //    public static List<String> coach2ids(List<Coach> coaches) {
    //        List<String> ret = new ArrayList<>();
    //        for (int i = 0; i < coaches.size(); i++) {
    //            ret.add(coaches.get(i).id);
    //        }
    //        return ret;
    //    }
    //
    //    public static List<String> teacher2Str(List<QcSchedulesResponse.Teacher> teachers) {
    //        List<String> ret = new ArrayList<>();
    //        for (int i = 0; i < teachers.size(); i++) {
    //            ret.add(teachers.get(i).username);
    //        }
    //        return ret;
    //    }
    //
    //    public static List<String> saler2Str(List<QcResponseSalers.Saler> salers) {
    //        List<String> ret = new ArrayList<>();
    //        for (int i = 0; i < salers.size(); i++) {
    //            if (salers.get(i).username != null)
    //                ret.add(salers.get(i).username);
    //
    //        }
    //        return ret;
    //    }
    public static List<String> impress2Str(List<TeacherImpression> impressions) {

        List<String> ret = new ArrayList<>();
        if (impressions == null) return ret;
        for (int i = 0; i < impressions.size(); i++) {
            ret.add(impressions.get(i).comment);
        }
        return ret;
    }

    public static List<String> shop2Str(List<QcScheduleBean.Shop> shops) {
        List<String> ret = new ArrayList<>();
        if (shops == null) return ret;
        for (int i = 0; i < shops.size(); i++) {
            ret.add(shops.get(i).name);
        }
        return ret;
    }

    //    public static List<Card_tpl> card2Card_tpl(@NonNull List<QcResponseSaleDetail.Card> cards) {
    //        List<Card_tpl> ret = new ArrayList<>();
    //        for (int i = 0; i < cards.size(); i++) {
    //            ret.add(new Card_tpl(cards.get(i).name, cards.get(i).card_type + "", "", cards.get(i).card_tpl_id, ""));
    //        }
    //        return ret;
    //    }
    //
    //    public static List<Card_tpl> card2Card_tpl2(@NonNull List<SigninReportDetail.CheckinsBean.CardBean> cards) {
    //        List<Card_tpl> ret = new ArrayList<>();
    //        for (int i = 0; i < cards.size(); i++) {
    //            ret.add(new Card_tpl(cards.get(i).getName(), cards.get(i).getCard_tpl_type() + "", "", cards.get(i).getCard_tpl_id(), ""));
    //        }
    //        return ret;
    //    }

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
}
