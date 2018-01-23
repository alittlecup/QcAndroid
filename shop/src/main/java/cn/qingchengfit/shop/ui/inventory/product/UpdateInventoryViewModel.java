package cn.qingchengfit.shop.ui.inventory.product;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.Observable;
import android.databinding.Observable.OnPropertyChangedCallback;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
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
  public final ObservableInt curInventory = new ObservableInt(0);
  public final ObservableField<String> offSet = new ObservableField<>();
  public final ObservableInt offSetInventory = new ObservableInt();
  public final ObservableField<String> productName = new ObservableField<>();
  public final ObservableField<String> goodName = new ObservableField<>();
  private final MutableLiveData<Integer> productId = new MutableLiveData<>();
  private final MutableLiveData<Integer> goodId = new MutableLiveData<>();
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
    offSet.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable sender, int propertyId) {
        if (offSet.get() != null && offSet.get().length() > 0) {
          try {
            offSetInventory.set(Integer.valueOf(offSet.get()));
          } catch (NumberFormatException e) {

          }
        } else {
          offSetInventory.set(0);
        }
      }
    });
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

  private LiveData<List<Good>> loadGoodInfo(Integer id) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("product_id", id);
    return repository.qcLoadGoodInfo(loginStatus.staff_id(), params);
  }

  public void loadSource(Integer id) {
    productId.setValue(id);
  }

  public void postRecord(Integer good_id) {
    goodId.setValue(good_id);
  }

  private LiveData<Boolean> upDateInventoryRecord(Integer id) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("action_type", action);
    params.put("offset", Integer.valueOf(offSet.get()));
    params.put("goods_id", id);
    return repository.qcUpdateInventoryRecord(loginStatus.staff_id(), params);
  }
}
