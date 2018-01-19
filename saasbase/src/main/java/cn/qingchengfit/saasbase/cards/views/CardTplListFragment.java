package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.animator.SlideInUpItemAnimator;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.item.CardTplItem;
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

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {

    View view =  super.onCreateView(inflater, container, savedInstanceState);
    if (view != null)
      view.setBackgroundResource(R.color.transparent);
    rv.setBackgroundResource(R.color.transparent);
    return view;
  }

  @Override public String getFragmentName() {
    return CardTplListFragment.class.getName();
  }

  @Override protected void addDivider() {
    rv.setItemAnimator(new SlideInUpItemAnimator());
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
      datas.clear();
      if (list != null) {
        for (CardTpl cardTpl : list) {
          datas.add(generateItem(cardTpl));
        }
      }
      if (datas.size() == 0)
        datas.add(commonNoDataItem);
      commonFlexAdapter.updateDataSet(datas,true);
    }
  }

  public int getItemCount(){
    if (commonFlexAdapter != null) {
      if (commonFlexAdapter.getItemCount() == 1 && commonFlexAdapter.getItem(0) instanceof CommonNoDataItem)
        return 0;
      else
        return commonFlexAdapter.getItemCount();
    }
    else return 0;
  }
  protected CardTplItem generateItem(CardTpl cardTpl){
    return new CardTplItem(cardTpl);
  }

  @Override public int getNoDataIconRes() {
    return R.drawable.vd_card_empty;
  }

  @Override public String getNoDataStr() {
    return "暂无会员卡种类";
  }
}
