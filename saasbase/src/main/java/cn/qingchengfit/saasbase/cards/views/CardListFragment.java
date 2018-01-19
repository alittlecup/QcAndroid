package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import cn.qingchengfit.animator.SlideInRightItemAnimator;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.item.CardItem;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.views.fragments.BaseListFragment;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 2017/8/14.
 */
@Leaf(module = "card", path = "/card/list/")
public class CardListFragment extends BaseListFragment {

  @Inject IPermissionModel permissionModel;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    fabDrawable = R.drawable.ic_add_card;
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public String getFragmentName() {
    return CardListFragment.class.getName();
  }

  @Override protected void addDivider() {
    rv.setBackgroundResource(R.color.transparent);
    rv.setItemAnimator(new SlideInRightItemAnimator(new DecelerateInterpolator()));
    if (srl != null) srl.setBackgroundResource(R.color.transparent);
    rv.setPadding(15, 0, 15, 0);
    rv.addItemDecoration(
      new FlexibleItemDecoration(getContext()).addItemViewType(R.layout.item_saas_realcard));
  }

  @Override public void onClickFab() {
    super.onClickFab();
    if (permissionModel.check(PermissionServerUtils.MANAGE_COSTS_CAN_WRITE)){
      routeTo(AppUtils.getRouterUri(getContext(), "/card/choose/cardtpl/"), null);
    }else{
      DialogUtils.showAlert(getContext(), getResources().getString(R.string.buy_card_no_permission));
    }
  }

  public void setCardtpls(List<Card> list, int page) {
    stopRefresh();
    if (commonFlexAdapter != null) {
      List<IFlexible> datas = new ArrayList<>();
      if (list != null) {
        for (Card cardTpl : list) {
          datas.add(generateItem(cardTpl));
        }
        if (page == 1) {
          commonFlexAdapter.clear();
          if (datas.size() == 0) datas.add(commonNoDataItem);
          commonFlexAdapter.updateDataSet(datas, true);
        }else
          commonFlexAdapter.onLoadMoreComplete(datas, 500);
      } else {
        commonFlexAdapter.onLoadMoreComplete(null, 500);
      }
    }
  }

  protected CardItem generateItem(Card cardTpl) {
    return new CardItem(cardTpl);
  }

  @Override public int getNoDataIconRes() {
    return R.drawable.vd_card_empty;
  }

  @Override public String getNoDataStr() {
    return "暂无会员卡";
  }
}
