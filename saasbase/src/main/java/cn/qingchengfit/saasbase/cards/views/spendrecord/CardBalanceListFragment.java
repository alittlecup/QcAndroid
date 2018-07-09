package cn.qingchengfit.saasbase.cards.views.spendrecord;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.item.CardItem;
import cn.qingchengfit.saasbase.cards.views.CardBalanceFragment;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.utils.DialogUtils;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.items.IFlexible;
import javax.inject.Inject;

@Leaf(module = "card", path = "/balance/list") public class CardBalanceListFragment
    extends CardBalanceFragment {
  @Inject IPermissionModel permissionModel;

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("会员卡");
    toolbar.getMenu().clear();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    llBalanceCondition.setVisibility(View.GONE);
  }

  @Override public boolean onItemClick(int position) {
    IFlexible iFlexible = cardListFragment.getItem(position);
    if (iFlexible instanceof CardItem) {
      if (!permissionModel.check(PermissionServerUtils.MANAGE_COSTS_CAN_WRITE)) {
        DialogUtils.showAlert(getContext(), R.string.alert_permission_forbid);
        return true;
      }
      Bundle b = new Bundle();
      b.putParcelable("card", ((CardItem) iFlexible).getRealCard());
      routeTo("/charge/", b);
    }
    return true;
  }
}
