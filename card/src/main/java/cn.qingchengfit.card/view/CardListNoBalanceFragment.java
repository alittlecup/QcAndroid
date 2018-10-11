package cn.qingchengfit.card.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.item.CardItem;
import cn.qingchengfit.saasbase.cards.presenters.CardDetailPresenter;
import cn.qingchengfit.saasbase.cards.views.CardBalanceFragment;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saascommon.widget.bubble.BubbleViewUtil;
import cn.qingchengfit.utils.DialogUtils;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.items.IFlexible;
import javax.inject.Inject;

@Leaf(module = "card", path = "/list/nobalance") public class CardListNoBalanceFragment
    extends StaffCardListHomeFragment implements CardDetailPresenter.MVPView {
  @Inject IPermissionModel permissionModel;
  @Inject CardDetailPresenter detailPresenter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    cardListFragment.setFabVisible(false);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    delegatePresenter(detailPresenter,this);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.getMenu().clear();
  }

  @Override
  public void handleBubble(BubbleViewUtil bubbleViewUtil) {}

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    layoutCardOperate.setVisibility(View.GONE);
  }

  @Override public void onRefresh() {
    cardListFragment.initLoadMore(100, this);
    presenter.initpage();
    presenter.queryAllCards();
  }

  @Override public boolean onItemClick(int position) {
    IFlexible iFlexible = cardListFragment.getItem(position);
    if (iFlexible instanceof CardItem) {
      if (!permissionModel.check(PermissionServerUtils.MANAGE_COSTS_CAN_WRITE)) {
        DialogUtils.showAlert(getContext(), R.string.alert_permission_forbid);
        return true;
      }
      String id = ((CardItem) iFlexible).getRealCard().getId();
      detailPresenter.setCardId(id);
      detailPresenter.queryCardDetail();
      showLoadingTrans();
    }
    return true;
  }

  @Override public void onCardDetail(Card card) {
    hideLoadingTrans();
    Bundle b = new Bundle();
    b.putParcelable("card", card);
    b.putString("qcCallId", getActivity().getIntent().getStringExtra("qcCallId"));
    routeTo("/charge/", b);
  }
}
