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
import cn.qingchengfit.shop.ui.items.product.IProductItemData;
import cn.qingchengfit.shop.ui.items.product.ProductListItem;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopProductsListPage
    extends ShopBaseFragment<PageProductListBinding, ShopProductsViewModel>
    implements FlexibleAdapter.OnItemClickListener, FlexibleAdapter.EndlessScrollListener {
  CommonFlexAdapter adapter;

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
      Uri uri = Uri.parse("shop://shop/shop/product");
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

  private void loadData() {
    if (getArguments() != null) {
      mViewModel.setStatus((Integer) getArguments().get("status"));
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
    List<ProductListItem> items = new ArrayList<>();
    IProductItemData data = new IProductItemData() {
      @Override public String getProductName() {
        return "Nam,e";
      }

      @Override public String getProductImage() {
        return "";
      }

      @Override public String getProductAddTime() {
        return "2018-12-12 10:10:10";
      }

      @Override public String getProductPrices() {
        return "30";
      }

      @Override public int getProductSales() {
        return 10;
      }

      @Override public int getProductInventory() {
        return 20;
      }

      @Override public int getProductPriority() {
        return 30;
      }

      @Override public boolean getProductStatus() {
        return false;
      }
    };

    items.add(new ProductListItem(data));
    mViewModel.items.set(items);
  }

  @Override public boolean onItemClick(int position) {
    Uri uri = Uri.parse("shop://shop/shop/product");
    routeTo(uri, null);
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

