package cn.qingchengfit.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;
import cn.qingchengfit.bean.StudentBean;
import cn.qingchengfit.bean.TeacherImpression;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.statement.model.QcResponseSaleDetail;
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
 * Created by Paper on 2017/4/15.
 */

public class BusinessUtils {
    /**
     * 变更销售-会员状态
     *
     * @param view view
     * @param status status
     * 0 # 新注册
     * 1 # 跟进中
     * 2 # 会员
     * 3 # 非会员
     */
    public static void studentStatus(TextView view, int status) {
        String statuStr = "";
        Drawable drawable = new ColorDrawable();

        view.setText(statuStr);
        drawable.setBounds(0, 0, 26, 26);
        view.setCompoundDrawablePadding(10);
        view.setCompoundDrawables(drawable, null, null, null);
    }

    public static List<String> qcstudentIds2List(List<QcStudentBean> students, Context context) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i) instanceof Personage) ret.add(context.getString(R.string.chat_user_id_header, students.get(i).getId()));
        }

        return ret;
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

    public static List<String> students2strs(List<StudentBean> students) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            ret.add(students.get(i).username);
        }

        return ret;
    }
    public static List<String> qcstudents2strs(List<QcStudentBean> students) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            ret.add(students.get(i).username);
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

    public static List<CardTpl> card2Card_tpl(@NonNull List<QcResponseSaleDetail.Card> cards) {
        List<CardTpl> ret = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            ret.add(new CardTpl(cards.get(i).name, cards.get(i).card_type, "", cards.get(i).card_tpl_id, ""));
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

    public static List<String> getPayMethods(Context context, List<Integer> integers) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < integers.size(); i++) {
            ret.add(getPayMethod(context, integers.get(i)));
        }
        return ret;
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



  public static <T extends Personage> List<String> PersonIdsExSu(List<T> students) {
    List<String> ret = new ArrayList<>();
    for (int i = 0; i < students.size(); i++) {
      if (!students.get(i).is_superuser) {
        ret.add(students.get(i).getId());
      }
    }
        return ret;
    }


    public static String students2str(List<StudentBean> students) {
        String ret = "";
        for (int i = 0; i < students.size(); i++) {
            ret = ret.concat(TextUtils.isEmpty(ret) ? "" : "、").concat(students.get(i).username);
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

    public static List<String> impress2Str(List<TeacherImpression> impressions) {

        List<String> ret = new ArrayList<>();
        if (impressions == null) return ret;
        for (int i = 0; i < impressions.size(); i++) {
            ret.add(impressions.get(i).comment);
        }
        return ret;
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
}
