package cn.qingchengfit.shop.ui.inventory;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
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
import cn.qingchengfit.shop.ui.items.inventory.InventoryRecordItem;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.shop.vo.Record;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopInventoryViewModel
    extends FlexibleViewModel<List<Record>, InventoryRecordItem, HashMap<String, Object>> {

  public final ObservableField<List<InventoryRecordItem>> items = new ObservableField<>();
  public final ObservableField<String> total_inventory = new ObservableField<>();

  public final MutableLiveData<Integer> indexEvent = new MutableLiveData<>();
  public ObservableBoolean filterVisible = new ObservableBoolean(false);
  public final ActionLiveEvent allProductClick = new  ActionLiveEvent();

  public HashMap<String, Object> getParams() {
    return params;
  }

  public LiveData<List<Product>> getProducts() {
    return products;
  }

  private final LiveData<List<Product>> products;
  private HashMap<String, Object> params = new HashMap<>();

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject ShopRepository repository;

  @Inject public ShopInventoryViewModel() {
    products = Transformations.switchMap(allProductClick, index -> loadAllProductGoods());
  }

  public void onShowFragmentByIndex(boolean isChecked, Integer index) {
    if (isChecked) {
      filterVisible.set(true);
      indexEvent.setValue(index);
    } else {
      filterVisible.set(false);
    }
  }

  @Override public void loadSource(@NonNull HashMap<String, Object> map) {
    identifier.setValue(map);
  }

  private LiveData<List<Product>> loadAllProductGoods() {
    HashMap<String, Object> params = gymWrapper.getParams();
    return repository.qcLoadAllProductInfo(loginStatus.staff_id(), params);
  }

  @NonNull @Override
  protected LiveData<List<Record>> getSource(@NonNull HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return Transformations.map(repository.qcLoadInventoryRecord(loginStatus.staff_id(), params),
        input -> input.records);
  }

  @Override protected boolean isSourceValid(@Nullable List<Record> inventories) {
    return inventories != null;
  }

  @Override protected List<InventoryRecordItem> map(@NonNull List<Record> inventories) {
    return FlexibleItemProvider.with(
        new CommonItemFactory<Record, InventoryRecordItem>(InventoryRecordItem.class))
        .from(inventories);
  }
}
