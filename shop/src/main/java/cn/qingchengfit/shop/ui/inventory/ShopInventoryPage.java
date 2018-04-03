package cn.qingchengfit.shop.ui.inventory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.student.views.filtertime.FilterTimesFragment;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.common.DoubleListFilterFragment;
import cn.qingchengfit.shop.databinding.PageShopInventoryBinding;
import cn.qingchengfit.shop.vo.ShopSensorsConstants;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/shop/inventory") public class ShopInventoryPage
    extends ShopBaseFragment<PageShopInventoryBinding, ShopInventoryViewModel> {
  InventoryFilterView filterView;
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, item -> {
      if (item == null || item.isEmpty()) {
        setEmptyView();
      } else {
        mViewModel.items.set(item);
      }
    });
    mViewModel.indexEvent.observe(this, index -> {
      filterView.showPage(index);
    });
  }

  private void setEmptyView() {
    String hintString = "暂无库存商品，赶快去添加吧～";

    CommonNoDataItem item = new CommonNoDataItem(R.drawable.vd_img_empty_universe, hintString);
    List<AbstractFlexibleItem> items = new ArrayList<>();
    items.add(item);
    adapter.updateDataSet(items);
  }

  long startTime = 0;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    startTime = System.currentTimeMillis() / 100;
    SensorsUtils.track(ShopSensorsConstants.SHOP_COMMODITY_INVENTORT_RECORDS_VISIT)
        .commit(getContext());
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (startTime > 0) {
      SensorsUtils.track(ShopSensorsConstants.SHOP_COMMODITY_INVENTORY_RECORDS_LEAVE)
          .addProperty(ShopSensorsConstants.QC_PAGE_STAY_TIME,
              System.currentTimeMillis() / 1000 - startTime)
          .commit(getContext());
    }
  }

  @Override
  public PageShopInventoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageShopInventoryBinding.inflate(inflater, container, false);
    initFragment();
    initToolbar();
    initRecyclerView();
    mBinding.setViewModel(mViewModel);
    mViewModel.loadSource(mViewModel.getParams());
    mBinding.inventoryContent.setVisibility(View.GONE);
    mViewModel.allProductClick.call();
    initRxbus();
    return mBinding;
  }

  private void initRxbus() {
    RxRegiste(RxBus.getBus()
        .register(DoubleListFilterFragment.class, String.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(s -> {
          mBinding.qftProduct.setText(s);
          mBinding.qftProduct.setColorOff(getResources().getColor(R.color.primary));
          mBinding.qftProduct.setTextColorRes(R.color.primary);
        }));
    RxRegiste(RxBus.getBus()
        .register(FilterTimesFragment.class, String.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(s -> {
          mBinding.qftDate.setText(s);
          mBinding.qftDate.setColorOff(getResources().getColor(R.color.primary));
          mBinding.qftDate.setTextColorRes(R.color.primary);
        }));
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerview.setAdapter(adapter);
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel(getString(R.string.inventory_reocrd)));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initFragment() {
    filterView = new InventoryFilterView();
    stuff(R.id.frag_filter, filterView);
  }
}
