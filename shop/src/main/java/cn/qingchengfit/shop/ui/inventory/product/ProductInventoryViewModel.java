package cn.qingchengfit.shop.ui.inventory.product;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
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
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.shop.vo.Record;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ProductInventoryViewModel
    extends FlexibleViewModel<List<Record>, InventoryRecordItem, HashMap<String, Object>> {
  public final ObservableField<List<InventoryRecordItem>> items = new ObservableField<>();
  public final ObservableField<String> total_inventory = new ObservableField<>();

  private final ActionLiveEvent addInventoryEvent = new ActionLiveEvent();

  public final ActionLiveEvent getAddInventoryEvent() {
    return addInventoryEvent;
  }

  public final ActionLiveEvent getReduceInventoryEvent() {
    return reduceInventoryEvent;
  }

  public final MutableLiveData<Integer> indexEvent = new MutableLiveData<>();
  public final MutableLiveData<Boolean> fragVisible = new MutableLiveData<>();

  private final ActionLiveEvent reduceInventoryEvent = new ActionLiveEvent();

  public LiveData<List<Good>> getGoodNames() {
    return goodNames;
  }

  private final LiveData<List<Good>> goodNames;
  private final MutableLiveData<Integer> productId = new MutableLiveData<>();

  public HashMap<String, Object> getParams() {
    return params;
  }

  private HashMap<String, Object> params = new HashMap<>();
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject ShopRepository repository;

  @Inject public ProductInventoryViewModel() {
    indexEvent.setValue(0);
    fragVisible.setValue(false);
    goodNames = Transformations.switchMap(productId,
        productId -> Transformations.map(loadGoodNames(productId),input -> input));
  }

  public void onAddInventoryClick() {
    addInventoryEvent.call();
  }

  public void onReduceInventoryClick() {
    reduceInventoryEvent.call();
  }

  public void onShowFragmentByIndex(Boolean isChecked, Integer index) {
    if (isChecked) {
      fragVisible.setValue(true);
      indexEvent.setValue(index);
    } else {
      fragVisible.setValue(false);
    }
  }

  @Override public void loadSource(@NonNull HashMap<String, Object> map) {
    identifier.setValue(map);
  }

  private LiveData<List<Good>> loadGoodNames(Integer id) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("product_id", id);
    return repository.qcLoadGoodInfo(loginStatus.staff_id(), params);
  }

  @NonNull @Override
  protected LiveData<List<Record>> getSource(@NonNull HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return Transformations.map(repository.qcLoadInventoryRecord(loginStatus.staff_id(), params),
        input -> {
          StringBuilder stringBuilder = new StringBuilder("总库存：");
          stringBuilder.append(input.total_inventory).append("  ");
          if (input.stat != null && !input.stat.isEmpty()) {
            for (Record.Stat stat : input.stat) {
              stringBuilder.append(stat.getName());
              stringBuilder.append("  ");
              stringBuilder.append(stat.getInventory());
            }
          }
          total_inventory.set(stringBuilder.toString());
          return input.records;
        });
  }

  @Override protected boolean isSourceValid(@Nullable List<Record> inventories) {
    return inventories != null && !inventories.isEmpty();
  }

  @Override protected List<InventoryRecordItem> map(@NonNull List<Record> inventories) {
    return FlexibleItemProvider.with(
        new CommonItemFactory<Record, InventoryRecordItem>(InventoryRecordItem.class))
        .from(inventories);
  }
}
