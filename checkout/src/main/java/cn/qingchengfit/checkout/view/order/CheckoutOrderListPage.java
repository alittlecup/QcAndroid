package cn.qingchengfit.checkout.view.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.bean.OrderListItemData;
import cn.qingchengfit.checkout.databinding.ChCheckoutOrderListBinding;
import cn.qingchengfit.checkout.item.OrderListItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "checkout", path = "/order/list") public class CheckoutOrderListPage
    extends CheckoutCounterFragment<ChCheckoutOrderListBinding, CheckoutOrderListVM>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getDatas().observe(this, datas -> {
      mBinding.swipeLayout.setRefreshing(false);
      if (datas != null && !datas.isEmpty()) {
        List<OrderListItem> items = new ArrayList<>();
        for (OrderListItemData data : datas) {
          items.add(new OrderListItem(data));
        }
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

    mViewModel.test();
  }

  private void initRefreshLayout() {
    mBinding.swipeLayout.setOnRefreshListener(this::onRefresh);
  }

  private void onRefresh() {
    mViewModel.loadOrderList();
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("二维码收款记录"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  @Override public boolean onItemClick(int position) {
    OrderListItemData data = ((OrderListItem) adapter.getItem(position)).getData();
    Bundle bundle = new Bundle();
    bundle.putParcelable("order", data);
    routeTo("/order/confirm", bundle);
    return false;
  }
}
