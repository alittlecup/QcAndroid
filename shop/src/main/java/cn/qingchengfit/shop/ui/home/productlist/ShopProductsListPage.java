package cn.qingchengfit.shop.ui.home.productlist;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.base.ShopPermissionUtils;
import cn.qingchengfit.shop.databinding.PageProductListBinding;
import cn.qingchengfit.shop.listener.ShopHomePageTabStayListener;
import cn.qingchengfit.shop.ui.items.product.ProductListItem;
import cn.qingchengfit.shop.ui.product.ShopProductModifyPageParams;
import cn.qingchengfit.shop.vo.ShopSensorsConstants;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.CommonInputViewAdapter;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopProductsListPage
    extends ShopBaseFragment<PageProductListBinding, ShopProductsViewModel>
    implements FlexibleAdapter.OnItemClickListener, FlexibleAdapter.EndlessScrollListener,
    ShopHomePageTabStayListener {
  CommonFlexAdapter adapter;
  @Inject IPermissionModel permissionModel;

  /**
   * 0 已下架
   * 1 出售中
   */
  public static ShopProductsListPage newInstance(@IntRange(from = 0, to = 1) int status) {
    ShopProductsListPage page = new ShopProductsListPage();
    Bundle bundle = new Bundle();
    bundle.putInt("status", status);
    page.setArguments(bundle);
    return page;
  }

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      mViewModel.isLoading.set(false);
      if (items == null || items.isEmpty()) {
        setEmptyView();
      } else {
        mViewModel.items.set(new ArrayList<>(items));
      }
    });

    mViewModel.getProductEvent().observe(this, aVoid -> {
      if (permissionModel.check(ShopPermissionUtils.COMMODITY_LIST_CAN_WRITE)) {
        routeTo("/product/add", null);
      } else {
        showAlert(getString(R.string.sorry_for_no_permission));
      }
    });
  }

  @Override
  public PageProductListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageProductListBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);

    if (getArguments() != null) {
      int status = getArguments().getInt("status");
      this.status = status == 1;
      mViewModel.setStatus(status);
      if (this.status) {
        onVisit();
      }
    } else {
      LogUtil.e("TAG", "loadData: cant find this current page status");
    }

    initRecyclerView();
    if (permissionModel.check(ShopPermissionUtils.COMMODITY_LIST)) {
      loadData();
      initRxbus();
      initSearchProduct();
    } else {
      List<AbstractFlexibleItem> items = new ArrayList<>();
      items.add(new CommonNoDataItem(R.drawable.ic_403, getString(R.string.no_access),
          getString(R.string.no_current_page_permission)));
      mViewModel.items.set(items);
      mBinding.fragmentMark.setVisibility(View.VISIBLE);
    }

    return mBinding;
  }

  private void initSearchProduct() {
    RxTextView.afterTextChangeEvents(mBinding.etSearch)
        .debounce(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
          @Override public void call(TextViewAfterTextChangeEvent event) {
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
          }
        });
  }

  private void initRxbus() {
    RxRegiste(RxBus.getBus()
        .register(SwipeRefreshLayout.class, Boolean.class)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aBoolean -> mViewModel.isLoading.set(aBoolean)));
  }

  private void setEmptyView() {
    String hintString = "";
    if (mViewModel.getParams().containsKey("q")) {
      hintString = getString(R.string.not_found_match_result);
    } else {
      hintString =
          getString(status ? R.string.product_empty_text : R.string.product_empty_offsale_text);
    }
    CommonNoDataItem item = new CommonNoDataItem(R.drawable.vd_img_empty_universe, hintString);
    List<AbstractFlexibleItem> items = new ArrayList<>();
    items.add(item);
    adapter.updateDataSet(items);
  }

  private boolean status;

  private void loadData() {

    mViewModel.loadSource(mViewModel.getParams());
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.setAdapter(adapter);
    mBinding.recyclerview.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    adapter.addListener(this);
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof ProductListItem) {
      String productId = ((ProductListItem) item).getData().getProductId();
      if (productId != null) {
        routeTo("/product/modify",
            new ShopProductModifyPageParams().productId(productId).productStatus(status).build());
      }
    }

    return false;
  }

  @Override public void noMoreLoad(int newItemsSize) {
    Log.d("TAG", "noMoreLoad: " + newItemsSize);
  }

  @Override public void onLoadMore(int lastPosition, int currentPage) {
    Log.d("TAG", "onLoadMore: +" + lastPosition + "--> " + currentPage);
    Integer page = (Integer) mViewModel.getParams().get("page");
    mViewModel.getParams().put("page", page + 1);
    mViewModel.loadSource(mViewModel.getParams());
  }

  long startTime = 0;

  @Override public void onVisit() {
    startTime = System.currentTimeMillis() / 1000;
    SensorsUtils.track(status ? ShopSensorsConstants.SHOP_ACTIVED_COMMODITY_LIST_VISIT
        : ShopSensorsConstants.SHOP_INACTIVED_COMMODITY_LIST_VISIT).commit(getContext());
  }

  @Override public void onLeave() {
    SensorsUtils.track(status ? ShopSensorsConstants.SHOP_ACTIVED_COMMODITY_LIST_LEAVE
        : ShopSensorsConstants.SHOP_INACTIVED_COMMODITY_LIST_LEAVE)
        .addProperty(ShopSensorsConstants.QC_PAGE_STAY_TIME,
            System.currentTimeMillis() / 1000 - startTime)
        .commit(getContext());
  }
}

