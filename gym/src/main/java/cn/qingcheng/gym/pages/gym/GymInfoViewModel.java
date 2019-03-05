package cn.qingcheng.gym.pages.gym;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingcheng.gym.bean.GymType;
import cn.qingcheng.gym.bean.GymTypeData;
import cn.qingcheng.gym.responsitory.IGymResponsitory;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class GymInfoViewModel extends BaseViewModel {
  @Inject IGymResponsitory gymResponsitory;
  public LiveData<List<GymType>> gymTypes;
  public LiveData<Boolean> deleteResult;
  private MutableLiveData<String> deleteShopID = new MutableLiveData<>();
  private ActionLiveEvent loadGymTypes = new ActionLiveEvent();

  @Inject public GymInfoViewModel(IGymResponsitory gymResponsitory) {
    this.gymResponsitory = gymResponsitory;
    gymTypes = Transformations.switchMap(loadGymTypes,
        aVoid -> Transformations.map(gymResponsitory.qcGetGymTypes(), gymTypeDataResource -> {
          GymTypeData gymTypeData = dealResource(gymTypeDataResource);
          return gymTypeData == null ? new ArrayList<>() : gymTypeData.gym_types;
        }));
    deleteResult = Transformations.switchMap(deleteShopID,
        id -> Transformations.map(gymResponsitory.qcDeleteShop(id), booleanResource -> {
          Boolean aBoolean = dealResource(booleanResource);
          return aBoolean == null ? false : aBoolean;
        }));
  }

  public void loadGymTypes() {
    loadGymTypes.call();
  }

  public void deleteShop(String shop_id) {
    deleteShopID.setValue(shop_id);
  }

  public LiveData<Shop> createShop(Shop shop) {
    return Transformations.map(gymResponsitory.qcSystemInit(shop),
        new Function<Resource<Shop>, Shop>() {
          @Override public Shop apply(Resource<Shop> shopResource) {
            return dealResource(shopResource);
          }
        });
  }

  public LiveData<Boolean> editShop(Shop shop) {
    return Transformations.map(gymResponsitory.editGymIntro(shop.id, shop), new Function<Resource<Boolean>, Boolean>() {
      @Override public Boolean apply(Resource<Boolean> booleanResource) {
        return dealResource(booleanResource);
      }
    });
  }
}
