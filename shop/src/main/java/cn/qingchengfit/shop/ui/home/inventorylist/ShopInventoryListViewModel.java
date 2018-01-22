package cn.qingchengfit.shop.ui.home.inventorylist;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.saasbase.common.flexble.FlexibleViewModel;
import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.ui.items.CommonItemFactory;
import cn.qingchengfit.shop.ui.items.inventory.InventoryListItem;
import cn.qingchengfit.shop.vo.Product;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopInventoryListViewModel
    extends FlexibleViewModel<List<Product>, InventoryListItem, HashMap<String, Object>> {

  public final ObservableField<List<InventoryListItem>> items = new ObservableField<>();

  public final ActionLiveEvent getShowAllRecord() {
    return showAllRecord;
  }

  private final ActionLiveEvent showAllRecord = new ActionLiveEvent();
  private HashMap<String, Object> params = new HashMap<>();
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject ShopRepository repository;

  public HashMap<String, Object> getParams() {
    return params;
  }

  @Inject public ShopInventoryListViewModel() {
    params.put("show_all", true);
  }

  public void onAllInventoryListClick() {
    showAllRecord.call();
  }

  @NonNull @Override
  protected LiveData<List<Product>> getSource(@NonNull HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return repository.qcLoadProductList(loginStatus.staff_id(), params);
  }

  @Override protected boolean isSourceValid(@Nullable List<Product> product) {
    return product != null && !product.isEmpty();
  }

  @Override protected List<InventoryListItem> map(@NonNull List<Product> product) {
    return FlexibleItemProvider.with(
        new CommonItemFactory<Product, InventoryListItem>(InventoryListItem.class)).from(product);
  }
}
