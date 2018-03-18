package cn.qingchengfit.shop.ui.home.inventorylist;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageInventoryListBinding;
import cn.qingchengfit.shop.ui.inventory.product.ProductInventoryPageParams;
import cn.qingchengfit.shop.ui.items.inventory.InventoryListItem;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.jakewharton.rxbinding.widget.RxTextView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopInventoryListPage
    extends ShopBaseFragment<PageInventoryListBinding, ShopInventoryListViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getShowAllRecord().observe(this, aVoid -> {
      routeTo("/shop/inventory", null);
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
    initSearchProduct();
    mViewModel.loadSource(mViewModel.getParams());
    mBinding.allInventoryRecord.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
    mBinding.allInventoryRecord.getPaint().setAntiAlias(true);
    return mBinding;
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerview.setAdapter(adapter);
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    adapter.addListener(this);
  }

  private void initSearchProduct() {
    RxTextView.afterTextChangeEvents(mBinding.etSearch)
        .debounce(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(event -> {
          String key = event.toString().trim();
          if (!TextUtils.isEmpty(key)) {
            mViewModel.getParams().put("q", key);
            mViewModel.loadSource(mViewModel.getParams());
          } else {
            if (mViewModel.getParams().containsKey("q")) {
              mViewModel.getParams().remove("q");
              mViewModel.loadSource(mViewModel.getParams());
            }
          }
        });
  }

  @Override public boolean onItemClick(int position) {
    InventoryListItem item = (InventoryListItem) adapter.getItem(position);
    if (item.getData() instanceof Product) {
      routeTo("/product/inventory",
          new ProductInventoryPageParams().productId(((Product) item.getData()).getId()).build());
    }
    return false;
  }
}
