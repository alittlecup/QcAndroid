package cn.qingchengfit.saasbase.turnovers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.TurnoversHomePageBinding;
import cn.qingchengfit.saasbase.staff.listener.OnRecycleItemClickListener;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcFilterToggle;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;

@Leaf(module = "staff", path = "/turnover/home") public class TurnoversHomePage
    extends SaasBindingFragment<TurnoversHomePageBinding, TurnoversVM>
    implements OnRecycleItemClickListener {
  TurnoversFilterFragement filterFragement;
  CommonFlexAdapter adapter;
  TurnoverChartFragment chartFragment;
  // 这两个就是为了静态的存对象，保证能够在 item 里面使用到，因为接口返回数据的问题
  public static Map<String, TurFilterData> paymentChannels = new HashMap<>();
  public static Map<Integer, TurnoverTradeType> trade_types = new HashMap<>();

  @Override protected void subscribeUI() {
    mViewModel.getOrderDatas().observe(this, orderDatas -> {
      if (orderDatas != null && !orderDatas.isEmpty()) {
        List<TurnoverOrderItem> items = new ArrayList<>();
        for (ITurnoverOrderItemData data : orderDatas) {
          TurnoverOrderItem turnoverOrderItem = new TurnoverOrderItem(data);
          turnoverOrderItem.setListener(this);
          items.add(turnoverOrderItem);
        }
        adapter.updateDataSet(items);
      } else {
        List<AbstractFlexibleItem> items = new ArrayList<>();
        items.add(new TurNoDataItem(R.drawable.turnover_no_orders, "没有流水账单"));
        adapter.updateDataSet(items);
      }
    });
    mViewModel.filterVisible.observe(this, aBoolean -> {
      if (aBoolean) {
        mBinding.flFilter.setVisibility(View.VISIBLE);
        mBinding.flFilter.setAlpha(0f);
        mBinding.flFilter.animate().alpha(1).setDuration(20).start();
      } else {
        mBinding.flFilter.animate().alpha(0).setDuration(20).withEndAction(new Runnable() {
          @Override public void run() {
            mBinding.flFilter.setVisibility(View.GONE);
          }
        }).start();
      }
    });

    mViewModel.getPayments().observe(this, datas -> {
      if (datas != null && !datas.isEmpty()) {
        paymentChannels.clear();
        for (ITurnoverFilterItemData data : datas) {
          if (data instanceof TurFilterData) {
            paymentChannels.put(((TurFilterData) data).getChannel(), (TurFilterData) data);
          }
        }
      }
    });
    mViewModel.getFeature().observe(this, datas -> {
      if (datas != null && !datas.isEmpty()) {
        trade_types.clear();
        for (ITurnoverFilterItemData data : datas) {
          if (data instanceof TurnoverTradeType) {
            trade_types.put(((TurnoverTradeType) data).getTrade_type(), (TurnoverTradeType) data);
          }
        }
        TurnoverTradeType tradeType = new TurnoverTradeType();
        tradeType.setTrade_type(-1);
        tradeType.setColor("e4e4e4");
        tradeType.setDesc("其他");
        trade_types.put(-1, tradeType);
      }
    });
    mViewModel.getChartResponse().observe(this, response -> {
      if (response == null || response.total == null || response.total.getAmount() <= 0) {
        mViewModel.chartVisible.setValue(false);
      } else {
        mViewModel.chartVisible.setValue(true);
      }
    });
    mViewModel.orderListUpdateTime.observe(this, time -> {
      if (TextUtils.isEmpty(time)) {
        mBinding.tvUpdateTime.setVisibility(View.GONE);
      } else {
        mBinding.tvUpdateTime.setVisibility(View.VISIBLE);
        mBinding.tvUpdateTime.setText("(上次更新时间为：" + time + ")");
      }
    });
    mViewModel.getOrderItemDataMutableLiveData().observe(this, data -> {
      if (data != null) {
        IFlexible item = adapter.getItem(pos);
        if (item instanceof TurnoverOrderItem) {
          ((TurnoverOrderItem) item).setData(data);
          adapter.notifyItemChanged(pos);
        }
      }
    });
  }

  @Override
  public TurnoversHomePageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = TurnoversHomePageBinding.inflate(inflater, container, false);
    mBinding.setPage(this);
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    return mBinding;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    filterFragement = new TurnoversFilterFragement();
    chartFragment = new TurnoverChartFragment();
    stuff(filterFragement);
    stuff(R.id.fl_chart, chartFragment);
    initToolbar();
    initRecyclerView();
    mViewModel.loadFilterOptions();
    mViewModel.loadSellerItems();
    RxBusAdd(Staff.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<Staff>() {
          @Override public void onNext(Staff staff) {
            mViewModel.putTurnoverSellerId(mViewModel.turId, staff.getId());
          }
        });
  }

  public void onDateFilter(View view) {
    showFilterView(((QcFilterToggle) view).isChecked(), 0);
  }

  public void onFeatureFilter(View view) {
    showFilterView(((QcFilterToggle) view).isChecked(), 1);
  }

  public void onSellerFilter(View view) {
    showFilterView(((QcFilterToggle) view).isChecked(), 2);
  }

  public void onPaymentFilter(View view) {
    showFilterView(((QcFilterToggle) view).isChecked(), 3);
  }

  public void onDateArrowLeft() {
    mViewModel.turnDatePre();
  }

  public void onDateArrowRight() {
    mViewModel.turnDateNext();
  }

  public void showFilterView(boolean show, int index) {
    if (show) {
      filterFragement.showPage(index);
      mViewModel.filterVisible.setValue(true);
    } else {
      mViewModel.filterVisible.setValue(false);
    }
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("营业流水"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
  }

  @Override public int getLayoutRes() {
    return R.id.fl_filter;
  }

  private int pos = 0;

  @Override public void onItemClick(View v, int pos) {
    IFlexible item = adapter.getItem(pos);
    if (item instanceof TurnoverOrderItem) {
      String id = ((TurnoverOrderItem) item).getData().getID();
      mViewModel.setTurId(id);
      this.pos = pos;
      routeTo("staff", "/choose/saler/", null);
    }
  }
}
