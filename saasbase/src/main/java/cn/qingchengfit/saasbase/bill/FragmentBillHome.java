package cn.qingchengfit.saasbase.bill;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.bill.filter.FragmentBillFilter;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by fb on 2017/10/11.
 */

//账单页面
@Leaf(module = "bill", path = "/home/list/") public class FragmentBillHome extends BaseFragment {

  @BindView(R2.id.tv_bill_total_amount) TextView tvBillTotalAmount;
  @BindView(R2.id.tv_bill_withdraw) TextView tvBillWithdraw;
  @BindView(R2.id.tv_bill_can_withdraw) TextView tvBillCanWithdraw;
  @BindView(R2.id.tv_bill_settlement) TextView tvBillSettlement;
  @BindView(R2.id.tv_bill_time) TextView tvBillTime;
  @BindView(R2.id.tv_bill_crash) TextView tvBillCrash;
  @BindView(R2.id.tv_bill_income) TextView tvBillIncome;
  @BindView(R2.id.recycler_bill) RecyclerView recyclerBill;
  @BindView(R2.id.container) FrameLayout container;
  @BindView(R2.id.tv_filter) TextView tvFilter;
  @BindView(R2.id.frag_bill_filter) FrameLayout fragBillFilter;
  @BindView(R2.id.drawer_filter) DrawerLayout drawerFilter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_bill_home, container, false);
    unbinder = ButterKnife.bind(this, view);
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag_bill_filter, new FragmentBillFilter())
        .commit();
    drawerFilter.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
    drawerFilter.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (drawerFilter.isDrawerOpen(GravityCompat.END)){
          drawerFilter.closeDrawer(GravityCompat.END);
        }
      }
    });
    fragBillFilter.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (drawerFilter.isDrawerOpen(GravityCompat.END)){
          drawerFilter.closeDrawer(GravityCompat.END);
        }
      }
    });
    container.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (drawerFilter.isDrawerOpen(GravityCompat.END)){
          drawerFilter.closeDrawer(GravityCompat.END);
        }
      }
    });

    return view;
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
}
