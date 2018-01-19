package cn.qingchengfit.saasbase.cards.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import cn.qingchengfit.saasbase.cards.item.CardTplItem;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.items.IFlexible;

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
 * Created by Paper on 2017/8/16.
 */
@Leaf(module = "card" , path = "/choose/cardtpl/")
public class ChooseCardTplForBuyCardFragment extends CardTplsHomeInGymFragment {

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("选择会员卡种类");
  }

  /**
   *  点击事件发送消息
   */
  @Override public boolean onItemClick(int i) {
    IFlexible item =fragmentList.get(viewpager.getCurrentItem()).getItem(i);
    if (item instanceof CardTplItem){
      if (((CardTplItem) item).getCardTpl().is_enable)
        routeTo("/pay/",new cn.qingchengfit.saasbase.cards.views.CardBuyParams().cardTpl(((CardTplItem) item).getCardTpl()).build());
      else {
        showAlert("已停用的会员卡种类不能用于购买会员卡");
      }
    }
    return true;
  }
}
