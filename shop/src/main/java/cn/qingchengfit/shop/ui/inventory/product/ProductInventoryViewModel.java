package cn.qingchengfit.shop.ui.inventory.product;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saasbase.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.saasbase.common.flexble.FlexibleViewModel;
import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.ui.items.CommonItemFactory;
import cn.qingchengfit.shop.ui.items.inventory.InventoryRecordItem;
import cn.qingchengfit.shop.vo.Inventory;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ProductInventoryViewModel
    extends FlexibleViewModel<List<Inventory>, InventoryRecordItem, String> {
  public final ObservableField<List<InventoryRecordItem>> items = new ObservableField<>();

  private final ActionLiveEvent addInventoryEvent = new ActionLiveEvent();

  public final ActionLiveEvent getAddInventoryEvent() {
    return addInventoryEvent;
  }

  public final ActionLiveEvent getReduceInventoryEvent() {
    return reduceInventoryEvent;
  }

  public final MutableLiveData<Integer> indexEvent=new MutableLiveData<>();
  public final MutableLiveData<Boolean> fragVisible=new MutableLiveData<>();

  private final ActionLiveEvent reduceInventoryEvent = new ActionLiveEvent();

  @Inject public ProductInventoryViewModel() {
    indexEvent.setValue(0);
    fragVisible.setValue(false);
  }

  public void onAddInventoryClick() {
    addInventoryEvent.call();
  }

  public void onReduceInventoryClick() {
    reduceInventoryEvent.call();
  }

  public void onShowFragmentByIndex(Boolean isChecked,Integer index){
    if(isChecked){
      fragVisible.setValue(true);
      indexEvent.setValue(index);
    }else{
      fragVisible.setValue(false);
    }
  }

  @NonNull @Override protected LiveData<List<Inventory>> getSource(@NonNull String s) {
    return null;
  }

  @Override protected boolean isSourceValid(@Nullable List<Inventory> inventories) {
    return inventories != null && !inventories.isEmpty();
  }

  @Override protected List<InventoryRecordItem> map(@NonNull List<Inventory> inventories) {
    return FlexibleItemProvider.with(
        new CommonItemFactory<Inventory, InventoryRecordItem>(InventoryRecordItem.class))
        .from(inventories);
  }
}
