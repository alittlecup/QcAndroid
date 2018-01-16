package cn.qingchengfit.shop.ui.home.inventorylist;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saasbase.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.saasbase.common.flexble.FlexibleViewModel;
import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.ui.items.CommonItemFactory;
import cn.qingchengfit.shop.ui.items.inventory.InventoryListItem;
import cn.qingchengfit.shop.vo.Product;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopInventoryListViewModel
    extends FlexibleViewModel<List<Product>, InventoryListItem, String> {

  public final ObservableField<List<InventoryListItem>> items = new ObservableField<>();

  public final ActionLiveEvent getShowAllRecord() {
    return showAllRecord;
  }

  private final ActionLiveEvent showAllRecord = new ActionLiveEvent();

  @Inject public ShopInventoryListViewModel() {
  }

  public void onAllInventoryListClick() {
    showAllRecord.call();
  }

  @NonNull @Override protected LiveData<List<Product>> getSource(@NonNull String s) {
    return null;
  }

  @Override protected boolean isSourceValid(@Nullable List<Product> product) {
    return false;
  }

  @Override protected List<InventoryListItem> map(@NonNull List<Product> product) {
    return FlexibleItemProvider.with(
        new CommonItemFactory<Product, InventoryListItem>(InventoryListItem.class)).from(product);
  }
}
