package cn.qingchengfit.shop.ui.inventory;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saasbase.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.saasbase.common.flexble.FlexibleViewModel;
import cn.qingchengfit.shop.ui.items.CommonItemFactory;
import cn.qingchengfit.shop.ui.items.inventory.InventoryRecordItem;
import cn.qingchengfit.shop.vo.Record;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopInventoryViewModel
    extends FlexibleViewModel<List<Record>, InventoryRecordItem, String> {

  public final ObservableField<List<InventoryRecordItem>> items = new ObservableField<>();
  public final MutableLiveData<Integer> indexEvent = new MutableLiveData<>();
  public final MutableLiveData<Boolean> fragVisible = new MutableLiveData<>();

  @Inject public ShopInventoryViewModel() {
    indexEvent.setValue(0);
    fragVisible.setValue(false);
  }

  public void onShowFragmentByIndex(boolean isChecked,Integer index) {
    if(isChecked){
      fragVisible.setValue(true);
      indexEvent.setValue(index);
    }else{
      fragVisible.setValue(false);
    }
  }

  @NonNull @Override protected LiveData<List<Record>> getSource(@NonNull String s) {


    return null;
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
