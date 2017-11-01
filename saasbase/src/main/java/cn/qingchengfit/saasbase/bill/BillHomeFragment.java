package cn.qingchengfit.saasbase.bill;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import cn.qingchengfit.saasbase.bill.event.BillFilterEvent;
import cn.qingchengfit.saasbase.bill.filter.BillFilterFragment;
import cn.qingchengfit.saasbase.bill.items.ItemBill;
import cn.qingchengfit.saasbase.bill.items.ItemBillAccount;
import cn.qingchengfit.saasbase.bill.items.ItemDataEmpty;
import cn.qingchengfit.saasbase.bill.items.ItemMoreFooter;
import cn.qingchengfit.saasbase.bill.presenter.BillSummary;
import cn.qingchengfit.saasbase.bill.presenter.BillTotalPresenter;
import cn.qingchengfit.saasbase.bill.view.BillDetailParams;
import cn.qingchengfit.saasbase.qrcode.views.QRActivity;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * Created by fb on 2017/10/11.
 */

//账单页面
@Leaf(module = "bill", path = "/home/list/") public class BillHomeFragment extends BaseFragment
    implements DrawerLayout.DrawerListener, BillTotalPresenter.MVPView,
    ItemMoreFooter.OnFooterClickListener, FlexibleAdapter.OnItemClickListener {

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
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  private CommonFlexAdapter adapter;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();

  private boolean isLoadMore;
  private Calendar calendar;
  private String nowDate;
  private String lastDate;
  private String beforeLastDate;

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
    presenter.qcGetBillList(gymWrapper.id(), DateUtils.formatToServer(new Date()));
    presenter.qcGetBillTotal(gymWrapper.id());
    calendar = Calendar.getInstance();
    setDateTimes();
    initView();
    initBus();
    return view;
  }

  private void setDateTimes() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    nowDate = sdf.format(calendar.getTime());
    calendar.add(Calendar.MONTH, -1);
    lastDate = sdf.format(calendar.getTime());
    calendar.add(Calendar.MONTH, -1);
    beforeLastDate = sdf.format(calendar.getTime());
  }

  private void initBus() {
    RxBusAdd(BillFilterEvent.class).onBackpressureLatest()
        .subscribe(new Action1<BillFilterEvent>() {
          @Override public void call(BillFilterEvent billFilterEvent) {
            drawerFilter.closeDrawer(GravityCompat.END);
            if (billFilterEvent.map.size() > 0) {
              presenter.qcGetBillListbyFilter(gymWrapper.id(), billFilterEvent.map);
            } else {
              presenter.qcGetBillList(gymWrapper.id(), nowDate);
            }
          }
        });
  }

  private void initView() {
    adapter = new CommonFlexAdapter(itemList, this);
    recyclerBill.setLayoutManager(new LinearLayoutManager(getContext()));
    //recyclerBill.addItemDecoration(
    //    new FlexibleItemDecoration(getContext()).withDivider(R.drawable.divider_grey)
    //         
    recyclerBill.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    recyclerBill.setAdapter(adapter);
  }

  private void setToolbar(Toolbar toolbar) {
    initToolbar(toolbar);
    toolbarTitle.setText("账单");
    toolbar.inflateMenu(R.menu.menu_cash);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        QRActivity.start(getContext(), QRActivity.MODULE_PAY_CASH);
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
    tvBillTotalAmount.setText(
        String.format("%.2f", (billTotal.withdraw_sum + billTotal.can_withdraw_sum + billTotal.frozen_sum) / 100));
    tvBillWithdraw.setText(String.format("%.2f",billTotal.can_withdraw_sum / 100));
    tvBillCanWithdraw.setText(String.format("%.2f", billTotal.withdraw_sum / 100));
    tvBillSettlement.setText(String.format("%.2f", billTotal.frozen_sum / 100));
  }

  @Override public void onGetBillList(List<BusinessBill> billList) {
    if (!isLoadMore) {
      itemList.clear();
    }
    dealMonth(billList);
    adapter.addScrollableFooter(new ItemMoreFooter(this));
    adapter.notifyDataSetChanged();
  }

  @Override public void onGetBillListByFilter(List<BusinessBill> billList) {

    if (itemList.size() > 0) {
      itemList.clear();
    }

    if (adapter.getItem(adapter.getItemCount() - 1) instanceof ItemMoreFooter) {
      adapter.removeItem(adapter.getItemCount() - 1);
      adapter.notifyDataSetChanged();
    }

    dealFilterMonth(billList);
    adapter.notifyDataSetChanged();
  }

  private void dealFilterMonth(List<BusinessBill> billList) {
    HashMap<String, ArrayList<BusinessBill>> billMap = new HashMap<>();
    for (BusinessBill bill : billList) {
      String m = DateUtils.getYYMMfromServer(bill.created_at);
      if (billMap.containsKey(m)) {
        billMap.get(m).add(bill);
      } else {
        ArrayList<BusinessBill> list = new ArrayList<>();
        list.add(bill);
        billMap.put(m, list);
      }
    }

    for (String time : billMap.keySet()) {
      dispatchItem(billMap, time);
    }
  }

  private void dealMonth(List<BusinessBill> bills) {
    HashMap<String, ArrayList<BusinessBill>> billMap = new HashMap<>();
    billMap.put(lastDate, new ArrayList<BusinessBill>());
    billMap.put(nowDate, new ArrayList<BusinessBill>());
    billMap.put(beforeLastDate, new ArrayList<BusinessBill>());
    for (BusinessBill businessBill : bills) {
      String m = DateUtils.getYYMMfromServer(businessBill.created_at);
      if (billMap.containsKey(m)) {
        billMap.get(m).add(businessBill);
      }
    }

    dispatchItem(billMap, nowDate);
    dispatchItem(billMap, lastDate);
    dispatchItem(billMap, beforeLastDate);
  }

  private void dispatchItem(HashMap<String, ArrayList<BusinessBill>> map, String time) {
    if (map.get(time).size() == 0) {
      itemList.add(new ItemBillAccount(time, 0f, 0f));
      itemList.add(new ItemDataEmpty());
      return;
    }

    BillSummary summary = presenter.getSummary(map.get(time));
    itemList.add(new ItemBillAccount(DateUtils.getYYMMfromServer(map.get(time).get(0).created_at),
        summary.sum / 100, summary.reduce / 100));

    for (BusinessBill bill : map.get(time)) {
      itemList.add(new ItemBill(bill));
    }
  }

  @Override public void onLoadMore() {
    isLoadMore = true;
    if (adapter.getItem(adapter.getItemCount() - 1) instanceof ItemMoreFooter) {
      adapter.removeItem(adapter.getItemCount() - 1);
      adapter.notifyDataSetChanged();
    }
    calendar.add(Calendar.MONTH, -1);
    presenter.qcGetBillList(gymWrapper.id(), DateUtils.date2YYMMDDTHHMMSS(calendar.getTime()));
    setDateTimes();
  }

  @Override public boolean onItemClick(int position) {
    if (adapter.getItem(position) instanceof ItemBill) {
      BusinessBill bill = ((ItemBill) adapter.getItem(position)).getBill();
      routeTo(AppUtils.getRouterUri(getContext(), "/bill/detail/"), new BillDetailParams().orderNo(bill.order_no).build());
    }
    return false;
  }
}
