package cn.qingchengfit.saasbase.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.network.body.OptionBody;
import cn.qingchengfit.utils.CmStringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
 * Created by Paper on 2017/8/15.
 */

public class CardBusinessUtils {
  public static String getCardTypeCategoryStr(int type, Context context) {
    switch (type) {
      case 2:
        return context.getResources().getStringArray(R.array.cardtype_category)[1];
      case 3:
        return context.getResources().getStringArray(R.array.cardtype_category)[2];
      default:
        return context.getResources().getStringArray(R.array.cardtype_category)[0];
    }
  }

  public static String getCardTypeCategorySimple(int type, Context context) {
    switch (type) {
      case 2:
        return context.getResources().getStringArray(R.array.cardtype_category_simple)[1];
      case 3:
        return context.getResources().getStringArray(R.array.cardtype_category_simple)[2];
      default:
        return context.getResources().getStringArray(R.array.cardtype_category_simple)[0];
    }
  }

  public static String getCardTypeCategoryStrHead(int type, Context context) {
    switch (type) {
      case 2:
        return context.getResources().getStringArray(R.array.cardtype_category)[1].substring(0, 1);
      case 3:
        return context.getResources().getStringArray(R.array.cardtype_category)[2].substring(0, 1);
      default:
        return context.getResources().getStringArray(R.array.cardtype_category)[0].substring(0, 1);
    }
  }

  public static int[] getDefaultCardbgColor(int type) {
    int[] colors = new int[2];
    switch (type) {
      case 1:
        colors[0] = Color.parseColor("#a99f84");
        colors[1] = Color.parseColor("#dacebd");
        break;
      case 2:
        colors[0] = Color.parseColor("#5a6a8e");
        colors[1] = Color.parseColor("#a4b2ce");
        break;
      default:
        colors[0] = Color.parseColor("#48484f");
        colors[1] = Color.parseColor("#a7a7b5");
        break;
    }
    return colors;
  }

  public static String getCardBlance(Card card) {
    switch (card.getType()) {
      case 2:
        return ((((Float) card.getBalance()).intValue() == card.getBalance())
          ? ((Float) card.getBalance()).intValue() :String.format(Locale.CHINA,"%.1f",card.getBalance()))+ "次";
      case 3:
        return (((Float) card.getBalance()).intValue() < 0 ? 0
          : ((Float) card.getBalance()).intValue()) + "天";
      default:
        return CmStringUtils.getFloatDot2(card.getBalance()) + "元";
    }
  }

  public static String getCardTypeCategoryUnit(int type) {
    switch (type) {
      case 2:
        return "次";
      case 3:
        return "天";
      default:
        return "元";
    }
  }

  public static CardTplOption optionBody2Option(OptionBody body) {
    CardTplOption o = new CardTplOption();
    o.can_charge = body.can_charge;
    o.can_create = body.can_create;
    o.limit_days = body.limit_days;
    o.days = body.days;
    o.charge = body.charge;
    o.for_staff = body.for_staff;
    o.price = TextUtils.isEmpty(body.price) ? 0f : Float.parseFloat(body.price);
    o.id = body.id;
    return o;
  }

  public static String supportChargeAndCreate(boolean charge, boolean create) {
    List<String> ret = new ArrayList<>();
    if (charge) ret.add("充值");
    if (create) ret.add("购卡");
    return CmStringUtils.List2StrChinese(ret);
  }
}
