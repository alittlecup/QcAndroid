package cn.qingchengfit.shop.ui.inventory.product;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.base.ShopBaseViewModel;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.vo.Good;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class UpdateInventoryViewModel extends ShopBaseViewModel {
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject ShopRepository repository;

  public final ObservableField<String> unit = new ObservableField<>();
  public final ObservableBoolean isAdd = new ObservableBoolean(true);
  public final ObservableLong curInventory = new ObservableLong(0);
  public final ObservableInt offSetInventory = new ObservableInt(0);
  public final ObservableField<String> productName = new ObservableField<>();
  public final ObservableField<String> goodName = new ObservableField<>();
  private final MutableLiveData<String> productId = new MutableLiveData<>();
  private final MutableLiveData<String> goodId = new MutableLiveData<>();
  public final ActionLiveEvent chooseGoodEvent = new ActionLiveEvent();

  public LiveData<List<Good>> getGoods() {
    return goods;
  }

  @UpdateInventoryPage.UpdateAction int action;
  private final LiveData<List<Good>> goods;

  public LiveData<Boolean> getUpdateResult() {
    return updateResult;
  }

  private final LiveData<Boolean> updateResult;

  @Inject public UpdateInventoryViewModel() {
    goods = Transformations.switchMap(productId, this::loadGoodInfo);
    updateResult = Transformations.switchMap(goodId, this::upDateInventoryRecord);

  }

  public void setAction(int action) {
    if (action == UpdateInventoryPage.ADD) {
      isAdd.set(true);
    } else if (action == UpdateInventoryPage.REDUCE) {
      isAdd.set(false);
    }
    this.action = action;
  }

  public void onChooseGoodClick() {
    chooseGoodEvent.call();
  }

  private LiveData<List<Good>> loadGoodInfo(String id) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("product_id", id);
    return repository.qcLoadGoodInfo(loginStatus.staff_id(), params);
  }

  public void loadSource(String id) {
    productId.setValue(id);
  }

  public void postRecord(String good_id) {
    goodId.setValue(good_id);
  }

  private LiveData<Boolean> upDateInventoryRecord(String id) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("action_type", action);
    params.put("offset", offSetInventory.get());
    params.put("goods_id", id);
    return repository.qcUpdateInventoryRecord(loginStatus.staff_id(), params);
  }
}
