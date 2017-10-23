package cn.qingchengfit.pos.cashier;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.cashier.event.RefreshCashierEvent;
import cn.qingchengfit.pos.cashier.model.Cashier;
import cn.qingchengfit.pos.setting.ItemCashier;
import cn.qingchengfit.pos.setting.StaffInfoParams;
import cn.qingchengfit.pos.setting.presenter.CashierPresenter;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.BaseListFragment;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * Created by fb on 2017/10/19.
 */

@Leaf(module = "setting", path = "/cashier/list/") public class CashierListFragment
    extends BaseListFragment
    implements CashierPresenter.MVPView, FlexibleAdapter.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {

  private Toolbar toolbar;
  private TextView toolbarTitle;
  @Inject CashierPresenter presenter;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    LinearLayout root = (LinearLayout) inflater.inflate(R.layout.fragment_cashier_list, null);
    root.addView(view, 1);
    toolbar = (Toolbar) root.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) root.findViewById(R.id.toolbar_title);
    delegatePresenter(presenter, this);
    setToolbar(toolbar);
    initListener(this);
    initBus();
    presenter.qcGetCashier();
    return root;
  }

  private void initBus(){
    RxBusAdd(RefreshCashierEvent.class).onBackpressureLatest().subscribe(new Action1<RefreshCashierEvent>() {
      @Override public void call(RefreshCashierEvent refreshCashierEvent) {
        presenter.qcGetCashier();
      }
    });
  }

  private void setToolbar(Toolbar toolbar) {
    initToolbar(toolbar);
    toolbarTitle.setText("收银员");
    toolbar.inflateMenu(R.menu.menu_add);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        routeTo(AppUtils.getRouterUri(getContext(), "/setting/cashier/add/"), null);
        return false;
      }
    });
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public int getNoDataIconRes() {
    return R.drawable.vd_img_empty_universe;
  }

  @Override public String getNoDataStr() {
    return null;
  }

  @Override public void onAddSuccess() {

  }

  @Override public void onDeleteSuccess() {

  }

  @Override public void onModifySuccess() {

  }

  @Override public void onGetCashier(List<Cashier> cashierList) {
    if (cashierList.size() > 0) {
      itemList.clear();
      for (Cashier cashier : cashierList) {
        itemList.add(new ItemCashier(cashier));
      }
      setDatas(itemList, 1);
    }
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = itemList.get(position);
    if (item instanceof  ItemCashier) {
      routeTo(AppUtils.getRouterUri(getContext(), "setting/cashier/detail/"),
        new StaffInfoParams().cashier(((ItemCashier) itemList.get(position)).getData()).build());
    }
    return false;
  }

  @Override public void onRefresh() {
    presenter.qcGetCashier();
  }
}
