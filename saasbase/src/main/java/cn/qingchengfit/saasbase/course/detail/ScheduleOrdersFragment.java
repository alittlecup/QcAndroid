package cn.qingchengfit.saasbase.course.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.FragmentScheduleOrdersBinding;
import cn.qingchengfit.saasbase.items.FunHeaderItem;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Leaf(module = "course", path = "/schedule/orders") public class ScheduleOrdersFragment
    extends SaasBindingFragment<FragmentScheduleOrdersBinding, ScheduleDetailVM>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;
  @Need ScheduleOrders orders;
  @Need String scheduleID;

  @Override protected void subscribeUI() {
    mViewModel = ViewModelProviders.of(getParentFragment()).get(ScheduleDetailVM.class);
    mViewModel.cancelResult.observe(this, aBoolean -> {
      mViewModel.loadCouseOrders(scheduleID);
    });
    mViewModel.detailOrders.observe(this, this::updateOrderItems);
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  @Override
  public FragmentScheduleOrdersBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = FragmentScheduleOrdersBinding.inflate(inflater, container, false);
    initRecyclerView();
    updateOrderItems(orders);
    mViewModel.loadCouseOrders(scheduleID);
    return mBinding;
  }

  int orderCount = 0;

  private void updateOrderItems(ScheduleOrders scheduleOrders) {
    if (scheduleOrders != null
        && scheduleOrders.orders != null
        && !scheduleOrders.orders.isEmpty()) {
      List<ScheduleOrderItem> items = new ArrayList<>();
      List<ScheduleOrders.ScheduleOrder> orders = scheduleOrders.orders;
      Collections.sort(orders, (o1, o2) -> {
        int status = o1.getStatus();
        int toStatus = o2.getStatus();
        if (toStatus == status) {
          return 0;
        } else if (status == 2) {
          return 1;
        } else if (status > toStatus || toStatus == 2) {
          return -1;
        } else {
          return 1;
        }
      });
      orderCount = 0;
      for (ScheduleOrders.ScheduleOrder order : scheduleOrders.orders) {
        if (order.getStatus() != 2) {
          orderCount += order.getCount();
        }
        ScheduleOrderItem scheduleOrderItem = new ScheduleOrderItem(order);
        scheduleOrderItem.setHeader(new FunHeaderItem(order.getStatusString()));
        items.add(scheduleOrderItem);
      }
      adapter.updateDataSet(items);
    } else {
      List<CommonNoDataItem> items = new ArrayList<>();
      items.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无预约记录"));
      adapter.updateDataSet(items);
    }
    setTitle();
  }

  private void initRecyclerView() {
    mBinding.recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerOrders.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    mBinding.recyclerOrders.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof ScheduleOrderItem) {
      ScheduleOrders.ScheduleOrder data = ((ScheduleOrderItem) item).getData();
      mViewModel.postCancelOrder(data.getId());
    }
    return false;
  }

  private void setTitle() {
    StringBuilder stringBuilder = new StringBuilder("约课人数");
    if (orderCount != 0) {
      stringBuilder.append("(").append(orderCount).append(")");
    }
    mViewModel.orderDetailFirstTab.setValue(stringBuilder.toString());
  }
}
