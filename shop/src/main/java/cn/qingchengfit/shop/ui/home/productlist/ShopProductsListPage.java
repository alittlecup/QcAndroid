package cn.qingchengfit.shop.ui.home.productlist;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageProductListBinding;
import cn.qingchengfit.shop.ui.items.product.ProductListItem;
import cn.qingchengfit.shop.ui.product.ShopProductModifyPageParams;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;

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
      mViewModel.items.set(items);
    });

    mViewModel.getProductEvent().observe(this, aVoid -> {
      Uri uri = Uri.parse("shop://shop/product/add");
      routeTo(uri, null);
    });
  }

  @Override
  public PageProductListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageProductListBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    initRecyclerView();
    loadData();
    return mBinding;
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
    Uri uri = Uri.parse("shop://shop/product/modify");
    IFlexible item = adapter.getItem(position);
    if (item instanceof ProductListItem) {
      String productId = ((ProductListItem) item).getData().getProductId();
      if (productId != null) {
        routeTo(uri, new ShopProductModifyPageParams().productId(productId).productStatus(status).build());
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
}

