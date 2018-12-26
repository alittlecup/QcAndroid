package cn.qingchengfit.checkout.view.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.checkout.bean.OrderListItemData;
import cn.qingchengfit.checkout.databinding.ChCheckoutOrderListBinding;
import cn.qingchengfit.checkout.item.OrderListItem;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "checkout", path = "/order/list") public class CheckoutOrderListPage
    extends CheckoutCounterFragment<ChCheckoutOrderListBinding, CheckoutOrderListVM>
     {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getDatas().observe(this, datas -> {
      mBinding.swipeLayout.setRefreshing(false);
      if (datas != null && !datas.isEmpty()) {
        List<AbstractFlexibleItem> items = new ArrayList<>();
        for (OrderListItemData data : datas) {
          items.add(new OrderListItem(data));
        }
       items.add(SimpleTextItemItem.newBuilder()
            .bg(R.color.transparent)
            .gravity(Gravity.CENTER)
            .text("我是底线~")
            .build());
        adapter.updateDataSet(items);
      }else{
        List<AbstractFlexibleItem> items = new ArrayList<>();
        items.add(new CommonNoDataItem(0, "", "暂无二维码收款记录"));
        adapter.updateDataSet(items);
      }
    });
  }

  @Override
  public ChCheckoutOrderListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return ChCheckoutOrderListBinding.inflate(inflater, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar();
    initRecyclerView();
    initRefreshLayout();

  }

  private void initRefreshLayout() {
    mBinding.swipeLayout.setOnRefreshListener(this::onRefresh);
  }

  private void onRefresh() {
    mViewModel.onRefresh();
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("二维码收款记录"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
