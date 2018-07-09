package cn.qingchengfit.shop.ui.home.inventorylist;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.base.ShopPermissionUtils;
import cn.qingchengfit.shop.databinding.PageInventoryListBinding;
import cn.qingchengfit.shop.listener.ShopHomePageTabStayListener;
import cn.qingchengfit.shop.ui.inventory.product.ProductInventoryPageParams;
import cn.qingchengfit.shop.ui.items.inventory.InventoryListItem;
import cn.qingchengfit.shop.ui.items.inventory.InventorySingleTextItem;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.shop.vo.ShopSensorsConstants;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.jakewharton.rxbinding.widget.RxTextView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopInventoryListPage
    extends ShopBaseFragment<PageInventoryListBinding, ShopInventoryListViewModel>
    implements FlexibleAdapter.OnItemClickListener, ShopHomePageTabStayListener {
  CommonFlexAdapter adapter;
  @Inject IPermissionModel permissionModel;


  @Override protected void subscribeUI() {
    mViewModel.getShowAllRecord().observe(this, aVoid -> {
      routeTo("/shop/inventory", null);
    });
    mViewModel.getLiveItems().observe(this, items -> {
      if (items == null || items.isEmpty()) {
        setEmptyView();
      } else {
        List<AbstractFlexibleItem> item = new ArrayList<>();
        item.add(new InventorySingleTextItem());
        item.addAll(items);
        mViewModel.items.set(item);
        mBinding.allInventoryRecord.setVisibility(View.GONE);
        mBinding.swipeRefresh.setRefreshing(false);
      }
    });
  }

  private void initRxbus() {
    RxRegiste(RxBus.getBus()
        .register(SwipeRefreshLayout.class, Boolean.class)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aBoolean -> {
          if (!aBoolean) {
            mBinding.swipeRefresh.setRefreshing(false);
          }
        }));
  }

  private void setEmptyView() {
    String hintString = "";
    if (mViewModel.getParams().containsKey("q")) {
      hintString = getString(R.string.not_found_match_result);
    } else {
      hintString = getString(R.string.inventort_emptry);
    }
    CommonNoDataItem item = new CommonNoDataItem(R.drawable.vd_img_empty_universe, hintString);
    List<AbstractFlexibleItem> items = new ArrayList<>();
    items.add(item);
    adapter.updateDataSet(items);
    mBinding.allInventoryRecord.setVisibility(View.VISIBLE);
  }

  @Override
  public PageInventoryListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageInventoryListBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    initRecyclerView();
    if (permissionModel.check(ShopPermissionUtils.COMMODITY_INVENTORY)) {
      mViewModel.loadSource(mViewModel.getParams());
      mBinding.allInventoryRecord.setVisibility(View.GONE);
      initSearchProduct();
      initRxbus();
    } else {
      List<AbstractFlexibleItem> items = new ArrayList<>();
      mBinding.allInventoryRecord.setVisibility(View.VISIBLE);
      items.add(
          new CommonNoDataItem(R.drawable.vd_img_empty_universe, getString(R.string.no_access),
              getString(R.string.no_current_page_permission)));
      mViewModel.items.set(items);
      mBinding.fragmentMark.setVisibility(View.VISIBLE);
      mBinding.swipeRefresh.setEnabled(false);
    }
    mBinding.allInventoryRecord.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
    mBinding.allInventoryRecord.getPaint().setAntiAlias(true);
    mBinding.allInventoryRecord.setOnClickListener(v -> mViewModel.getShowAllRecord().call());
    return mBinding;
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerview.setAdapter(adapter);
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    adapter.addListener(this);
    mBinding.swipeRefresh.setOnRefreshListener(() -> mViewModel.loadSource(mViewModel.getParams()));
  }

  private void initSearchProduct() {
    RxTextView.afterTextChangeEvents(mBinding.etSearch)
        .debounce(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(event -> {
          String key = event.editable().toString().trim();
          if (!TextUtils.isEmpty(key)) {
            mViewModel.getParams().put("q", key);
            mViewModel.loadSource(mViewModel.getParams());
          } else {
            if (mViewModel.getParams().containsKey("q")) {
              mViewModel.getParams().remove("q");
            }
            mViewModel.loadSource(mViewModel.getParams());
          }
        });
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof InventoryListItem) {
      if (((InventoryListItem) item).getData() instanceof Product) {
        routeTo("/product/inventory", new ProductInventoryPageParams().productId(
            ((Product) ((InventoryListItem) item).getData()).getId()).build());
      }
    } else if (item instanceof InventorySingleTextItem) {
      mViewModel.getShowAllRecord().call();
    }
    return false;
  }

  long startTime = 0;

  @Override public void onVisit() {
    startTime = System.currentTimeMillis() / 1000;
    SensorsUtils.track(ShopSensorsConstants.SHOP_COMMODITY_INVENTORY_VISIT).commit(getContext());
  }

  @Override public void onLeave() {
    SensorsUtils.track(ShopSensorsConstants.SHOP_COMMODITY_INVENTORY_LEAVE)
        .addProperty(ShopSensorsConstants.QC_PAGE_STAY_TIME,
            System.currentTimeMillis() / 1000 - startTime)
        .commit(getContext());
  }
}
