package cn.qingchengfit.saasbase.bill;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.bill.beans.BillTotal;
import cn.qingchengfit.saasbase.bill.beans.BusinessBill;
import cn.qingchengfit.saasbase.bill.filter.BillFilterFragment;
import cn.qingchengfit.saasbase.bill.items.ItemBill;
import cn.qingchengfit.saasbase.bill.presenter.BillSummary;
import cn.qingchengfit.saasbase.bill.presenter.BillTotalPresenter;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/10/11.
 */

//账单页面
@Leaf(module = "bill", path = "/home/list/") public class BillHomeFragment extends BaseFragment
    implements DrawerLayout.DrawerListener, BillTotalPresenter.MVPView {

  @BindView(R2.id.tv_bill_total_amount) TextView tvBillTotalAmount;
  @BindView(R2.id.tv_bill_withdraw) TextView tvBillWithdraw;
  @BindView(R2.id.tv_bill_can_withdraw) TextView tvBillCanWithdraw;
  @BindView(R2.id.tv_bill_settlement) TextView tvBillSettlement;
  @BindView(R2.id.recycler_bill) RecyclerView recyclerBill;
  @BindView(R2.id.container) LinearLayout filterContainer;
  @BindView(R2.id.tv_filter) TextView tvFilter;
  @BindView(R2.id.frag_bill_filter) FrameLayout fragBillFilter;
  @BindView(R2.id.drawer_filter) DrawerLayout drawerFilter;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @Inject BillTotalPresenter presenter;
  @Inject GymWrapper gymWrapper;
  private CommonFlexAdapter adapter;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_bill_home, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag_bill_filter, new BillFilterFragment())
        .commit();
    setToolbar(toolbar);
    presenter.qcGetBillList(gymWrapper.id());
    initView();
    return view;
  }

  private void initView(){
    adapter = new CommonFlexAdapter(itemList);
    recyclerBill.setAdapter(adapter);
  }

  private void setToolbar(Toolbar toolbar){
    initToolbar(toolbar);
    toolbar.inflateMenu(R.menu.menu_cash);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        //TODO 扫码
        return false;
      }
    });
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R2.id.tv_filter) public void onFilter() {
    if (drawerFilter.isDrawerOpen(GravityCompat.END)) {
      drawerFilter.closeDrawer(GravityCompat.END);
    } else {
      drawerFilter.openDrawer(GravityCompat.END);
    }
  }

  @Override public void onDrawerSlide(View drawerView, float slideOffset) {

  }

  @Override public void onDrawerOpened(View drawerView) {

  }

  @Override public void onDrawerClosed(View drawerView) {

  }

  @Override public void onDrawerStateChanged(int newState) {

  }

  @Override public void onGetTotal(BillTotal billTotal) {
    tvBillTotalAmount.setText(String.valueOf(billTotal.cash + billTotal.balance + billTotal.frozen_cash));
    tvBillWithdraw.setText(String.valueOf(billTotal.cash));
    tvBillCanWithdraw.setText(String.valueOf(billTotal.balance));
    tvBillSettlement.setText(String.valueOf(billTotal.frozen_cash));
  }

  @Override public void onGetBillList(List<BusinessBill> billList) {
    if (billList.size() > 0){
      itemList.clear();
    }

    BillSummary billSummary = presenter.getSummary(billList);

    for (BusinessBill bill : billList){
      itemList.add(new ItemBill(bill));
    }
    adapter.notifyDataSetChanged();
  }

  private void dispatchItem(List<BusinessBill> bills){
    for (BusinessBill bill : bills){
      //TODO 对月份的分组处理
    }
  }

}
