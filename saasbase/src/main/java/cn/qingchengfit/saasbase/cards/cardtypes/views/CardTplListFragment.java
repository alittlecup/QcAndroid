package cn.qingchengfit.saasbase.cards.cardtypes.views;

import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.cardtypes.item.CardTplItem;
import cn.qingchengfit.views.fragments.BaseListFragment;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
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
 * Created by Paper on 2017/8/14.
 */
public class CardTplListFragment extends BaseListFragment {
  
  @Override public String getFragmentName() {
    return CardTplListFragment.class.getName();
  }

  @Override protected void addDivider() {
    rv.addItemDecoration(new FlexibleItemDecoration(getContext())
        .addItemViewType(R.layout.item_card_type)
        .withOffset(15)
        .withTopEdge(true)
        .withLeftEdge(true)
        .withRightEdge(true)
    );
  }

  public void setCardtpls(List<CardTpl> list){
    stopRefresh();
    if (commonFlexAdapter != null) {
      commonFlexAdapter.clear();
      if (list != null) {
        for (CardTpl cardTpl : list) {
          commonFlexAdapter.addItem(generateItem(cardTpl));
        }
      }
    }
  }


  protected CardTplItem generateItem(CardTpl cardTpl){
    return new CardTplItem(cardTpl);
  }

  @Override public int getNoDataIconRes() {
    return R.drawable.vd_img_empty_universe;
  }

  @Override public String getNoDataStr() {
    return "暂无会员卡种类";
  }

}
