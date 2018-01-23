package cn.qingchengfit.shop.ui.home.inventorylist;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageInventoryListBinding;
import cn.qingchengfit.shop.ui.inventory.product.ProductInventoryPageParams;
import cn.qingchengfit.shop.ui.inventory.product.UpdateInventoryPage;
import cn.qingchengfit.shop.ui.inventory.product.UpdateInventoryPageParams;
import cn.qingchengfit.shop.ui.items.inventory.InventoryListItem;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopInventoryListPage
    extends ShopBaseFragment<PageInventoryListBinding, ShopInventoryListViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getShowAllRecord().observe(this, aVoid -> {
      //Uri uri = Uri.parse("shop://shop/shop/inventory");
      Uri uri = Uri.parse("shop://shop/update/inventory");
      routeTo(uri,
          new UpdateInventoryPageParams().action(UpdateInventoryPage.REDUCE).productID(10).build());
    });
    mViewModel.getLiveItems().observe(this, items -> {
      mViewModel.items.set(items);
    });
  }

  @Override
  public PageInventoryListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageInventoryListBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    initRecyclerView();
    mViewModel.loadSource(mViewModel.getParams());
    return mBinding;
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerview.setAdapter(adapter);
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter.addListener(this);
  }

  @Override public boolean onItemClick(int position) {
    InventoryListItem item = (InventoryListItem) adapter.getItem(position);
    if (item.getData() instanceof Product) {
      Uri uri = Uri.parse("shop://shop/product/inventory");
      routeTo(uri, new ProductInventoryPageParams().product((Product) item.getData()).build());
    }
    return false;
  }
}
