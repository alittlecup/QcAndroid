package cn.qingchengfit.utils;

import android.content.Context;
import android.graphics.Color;
import cn.qingchengfit.model.body.OptionBody;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.model.responese.CardTplOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/11/14.
 */

public class CardBusinessUtils {

  public static int[] getDefaultCardbgColor(int type) {
    int[] colors = new int[2];
    switch (type) {
      case 1:
        colors[0] = Color.parseColor("#48484f");
        colors[1] = Color.parseColor("#a7a7b5");
        break;
      case 2:
        colors[0] = Color.parseColor("#5a6a8e");
        colors[1] = Color.parseColor("#a4b2ce");
        break;
      default:
        colors[0] = Color.parseColor("#a99f84");
        colors[1] = Color.parseColor("#dacebd");
        break;
    }
    return colors;
  }

  public static String getCardBlance(Card card) {
    switch (card.getType()) {
      case 2:
        return ((Float) card.getBalance()).intValue() + "次";
      case 3:
        return ((Float) card.getBalance()).intValue() + "天";
      default:
        //return CmStringUtils.getFloatDot2(card.getBalance()) + "元";
        return "";
    }
  }

  public static String getCardTypeCategoryUnit(int type, Context context) {
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
    o.days       = body.days;
    o.charge     = body.charge;
    o.for_staff  = body.for_staff;
    o.price      = body.price;
    o.id         = body.id;
    return o;
  }

  public static String supportChargeAndCreate(boolean charge,boolean create){
    List<String> ret = new ArrayList<>();
    if (charge) ret.add("充值");
    if (create) ret.add("购卡");
    return CmStringUtils.List2StrChinese(ret);
  }

}
