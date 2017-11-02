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
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.setting.StaffInfoParams;
import cn.qingchengfit.pos.setting.presenter.CashierPresenter;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.saasbase.staff.items.StaffItem;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.BaseListFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
  @Inject LoginStatus loginStatus;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RxBus.getBus().register(EventSaasFresh.StaffList.class)
      .compose(this.<EventSaasFresh.StaffList>bindToLifecycle())
      .compose(this.<EventSaasFresh.StaffList>doWhen(FragmentEvent.CREATE_VIEW))
      .subscribe(new BusSubscribe<EventSaasFresh.StaffList>() {
        @Override public void onNext(EventSaasFresh.StaffList eventSaasFresh) {
          onRefresh();
        }
      });
  }

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
    return root;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    presenter.qcGetCashier();
  }

  @Override protected void addDivider() {
    rv.setBackgroundResource(R.color.white);
    rv.addItemDecoration(new FlexibleItemDecoration(getContext())
        .withDivider(R.drawable.divider_grey_left_margin)
        .withOffset(1)
        .withBottomEdge(true)
    );
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

  @Override public void onGetCashier(List<Staff> cashierList) {
    if (cashierList.size() > 0) {
      itemList.clear();
      for (Staff cashier : cashierList) {
        itemList.add(new StaffItem(cashier));
      }
      setDatas(itemList, 1);
    }
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = itemList.get(position);
    if (item instanceof  StaffItem) {
      Staff staff = ((StaffItem) item).getStaff();
      if (staff.is_superuser && !staff.id.equalsIgnoreCase(loginStatus.staff_id())){
        showAlert("该收银员为超级管理员\n仅本人可以查看信息");
        return true;
      }
      routeTo(AppUtils.getRouterUri(getContext(), "setting/cashier/detail/"),
        new StaffInfoParams().cashier(staff).build());
    }
    return false;
  }

  @Override public void onRefresh() {
    presenter.qcGetCashier();
  }
}
