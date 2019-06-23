package cn.qingchengfit.saasbase.course.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.FragmentScheduleOrdersBinding;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "course", path = "/schedule/orders") public class ScheduleOrdersFragment
    extends SaasBindingFragment<FragmentScheduleOrdersBinding, ScheduleDetailVM> implements
    FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;
  @Need ScheduleOrders orders;
  @Need String scheduleID;

  @Override protected void subscribeUI() {
    mViewModel.cancelResult.observe(this,aBoolean -> {
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
    initToolbar();
    initRecyclerView();
    updateOrderItems(orders);
    mViewModel.loadCouseOrders(scheduleID);
    return mBinding;
  }

  private void updateOrderItems(ScheduleOrders scheduleOrders) {
    if (scheduleOrders != null
        && scheduleOrders.orders != null
        && !scheduleOrders.orders.isEmpty()) {
      List<ScheduleOrderItem> items = new ArrayList<>();
      for (ScheduleOrders.ScheduleOrder order : scheduleOrders.orders) {
        items.add(new ScheduleOrderItem(order));
      }
      adapter.updateDataSet(items);
    } else {
      List<CommonNoDataItem> items = new ArrayList<>();
      items.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无预约记录"));
      adapter.updateDataSet(items);
    }
  }

  private void initRecyclerView() {
    mBinding.recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerOrders.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(),this));
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("预约详情"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if(item instanceof ScheduleOrderItem){
      ScheduleOrders.ScheduleOrder data = ((ScheduleOrderItem) item).getData();
      mViewModel.postCancelOrder(data.getId());
    }
    return false;
  }
}
