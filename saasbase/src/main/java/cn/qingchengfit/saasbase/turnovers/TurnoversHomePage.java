package cn.qingchengfit.saasbase.turnovers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.TurnoversHomePageBinding;
import cn.qingchengfit.saasbase.staff.listener.OnRecycleItemClickListener;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcFilterToggle;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

@Leaf(module = "staff", path = "/turnover/home") public class TurnoversHomePage
    extends SaasBindingFragment<TurnoversHomePageBinding, TurnoversVM>
    implements OnRecycleItemClickListener, FlexibleAdapter.OnItemClickListener,
    FlexibleAdapter.EndlessScrollListener {
  TurnoversFilterFragement filterFragement;
  CommonFlexAdapter adapter;
  TurnoverChartFragment chartFragment;
  // 这两个就是为了静态的存对象，保证能够在 item 里面使用到，因为接口返回数据的问题
  public static Map<String, TurFilterData> paymentChannels = new HashMap<>();
  public static Map<Integer, TurnoverTradeType> trade_types = new HashMap<>();
  @Inject IPermissionModel permissionModel;

  @Override protected void subscribeUI() {
    mViewModel.orderDatas.observe(this, orderDatas -> {
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
      mBinding.recyclerView.scrollToPosition(0);
    });
    mViewModel.loadMoreOrderDatas.observe(this, orderDatas -> {
      if (orderDatas != null && !orderDatas.isEmpty()) {
        List<TurnoverOrderItem> items = new ArrayList<>();
        for (ITurnoverOrderItemData data : orderDatas) {
          TurnoverOrderItem turnoverOrderItem = new TurnoverOrderItem(data);
          turnoverOrderItem.setListener(this);
          items.add(turnoverOrderItem);
        }
        adapter.onLoadMoreComplete(items, 200);
      } else {
        adapter.onLoadMoreComplete(null);
      }
    });
    mViewModel.isRest.observe(this, aBoolean -> {
      LogUtil.d("isRest");
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
        mBinding.tvUpdateTime.setText("(上次更新时间" + time + ")");
      }
    });
    mViewModel.getMoreItems().observe(this, items -> {
      if (items != null && !items.isEmpty()) {
        List<TurnoverOrderItem> list = new ArrayList<>();
        for (ITurnoverOrderItemData data : items) {
          TurnoverOrderItem turnoverOrderItem = new TurnoverOrderItem(data);
          turnoverOrderItem.setListener(this);
          list.add(turnoverOrderItem);
        }
        adapter.onLoadMoreComplete(list, 200);
      } else {
        ToastUtils.show("没有更多数据了");
      }
    });
    mViewModel.totalCount.observe(this, totalCount -> {
      initLoadMore(totalCount);
    });
    mViewModel.getOrderItemDataMutableLiveData().observe(this, data -> {
      // REFACTOR: 2019/1/4 这里其实应该是使用 onResume 相应的 LiveData  不过赶工期先实现功能了
      this.data = data;
      updateSimpleItem();
    });
  }

  private void updateSimpleItem() {
    if (data != null && pos != -1) {
      IFlexible item = adapter.getItem(pos);
      if (item instanceof TurnoverOrderItem) {
        ((TurnoverOrderItem) item).setData(data);
        adapter.notifyItemChanged(pos);
        data = null;
      }
    }
  }

  private ITurnoverOrderItemData data;

  @Override
  public TurnoversHomePageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mBinding == null) {
      mBinding = TurnoversHomePageBinding.inflate(inflater, container, false);
      mBinding.setPage(this);
      mBinding.setViewModel(mViewModel);
      mBinding.setLifecycleOwner(this);
    }
    return mBinding;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (filterFragement == null) {
      filterFragement = new TurnoversFilterFragement();
      chartFragment = new TurnoverChartFragment();
      stuff(filterFragement);
      stuff(R.id.fl_chart, chartFragment);
      initToolbar();
      initRecyclerView();
      mViewModel.loadFilterOptions();
      mViewModel.loadSellerItems();
    }
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

  private ProgressItem progressItem;

  private void initLoadMore(int count) {
    adapter.setEndlessScrollListener(this, progressItem)
        .setEndlessPageSize(30)
        .setEndlessTargetCount(count);
  }

  private void initRecyclerView() {
    progressItem = new ProgressItem(getContext());
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    initLoadMore(100);
  }

  @Override public int getLayoutRes() {
    return R.id.fl_filter;
  }

  private int pos = -1;

  @Override public void onItemClick(View v, int pos) {
    if (!permissionModel.check(PermissionServerUtils.SHOP_TURNOVER_CHANGE)) {
      DialogUtils.showAlert(getContext(), R.string.alert_permission_forbid);
      return;
    }
    IFlexible item = adapter.getItem(pos);
    if (item instanceof TurnoverOrderItem) {
      String id = ((TurnoverOrderItem) item).getData().getID();
      mViewModel.setTurId(id);
      this.pos = pos;
      routeTo("staff", "/choose/saler/",
          new BundleBuilder().withBoolean("hasNoStaff", false).build());
    }
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof TurnoverOrderItem) {
      String id = ((TurnoverOrderItem) item).getData().getID();
      mViewModel.setTurId("");
      pos=position;
      routeTo("staff", "/turnover/order", new BundleBuilder().withString("turId", id).build());
    }

    return false;
  }

  @Override public void noMoreLoad(int newItemsSize) {
  }

  @Override public void onLoadMore(int lastPosition, int currentPage) {
    mViewModel.loadMore();
  }
}
