package cn.qingchengfit.shop.ui.home.productlist;

import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageProductListBinding;
import cn.qingchengfit.shop.ui.items.product.ProductListItem;
import cn.qingchengfit.shop.ui.product.ShopProductModifyPageParams;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopProductsListPage
    extends ShopBaseFragment<PageProductListBinding, ShopProductsViewModel>
    implements FlexibleAdapter.OnItemClickListener, FlexibleAdapter.EndlessScrollListener {
  CommonFlexAdapter adapter;

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
        mViewModel.items.set(items);
      }
    });

    mViewModel.getProductEvent().observe(this, aVoid -> {
      routeTo("/product/add", null);
    });
  }

  @Override
  public PageProductListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageProductListBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    initRecyclerView();
    loadData();
    initRxbus();
    initSearchProduct();
    mBinding.addOnRebindCallback(new OnRebindCallback() {
      @Override public void onBound(ViewDataBinding binding) {
        if (adapter.isEmpty()) {
          Log.d("TAG", "onBound: ");
          setEmptyView();
        }
      }
    });
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
                mViewModel.loadSource(mViewModel.getParams());
              }
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
    CommonNoDataItem item =
        new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无出售中商品，赶快去添加吧～");
    List<AbstractFlexibleItem> items = new ArrayList<>();
    items.add(item);
    adapter.updateDataSet(items);
  }

  private boolean status;

  private void loadData() {
    if (getArguments() != null) {
      int status = getArguments().getInt("status");
      this.status = status == 1;
      mViewModel.setStatus(status);
    } else {
      LogUtil.e("TAG", "loadData: cant find this current page status");
    }
    mViewModel.loadSource(mViewModel.getParams());
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.setAdapter(adapter);
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

  //todo 加载更多的问题，是全量还是分页
  @Override public void onLoadMore(int lastPosition, int currentPage) {
    Log.d("TAG", "onLoadMore: +" + lastPosition + "--> " + currentPage);
    Integer page = (Integer) mViewModel.getParams().get("page");
    mViewModel.getParams().put("page", page + 1);
    mViewModel.loadSource(mViewModel.getParams());
  }
}

