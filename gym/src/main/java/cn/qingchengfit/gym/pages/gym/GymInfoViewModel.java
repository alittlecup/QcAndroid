package cn.qingchengfit.gym.pages.gym;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.gym.bean.GymType;
import cn.qingchengfit.gym.bean.GymTypeData;
import cn.qingchengfit.gym.bean.ShopCreateBody;
import cn.qingchengfit.gym.responsitory.IGymResponsitory;
import cn.qingchengfit.gym.responsitory.network.IGymModel;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.SingleLiveEvent;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class GymInfoViewModel extends BaseViewModel {
  @Inject IGymResponsitory gymResponsitory;
  public LiveData<List<GymType>> gymTypes;
  public LiveData<Boolean> deleteResult;
  private MutableLiveData<String> deleteShopID = new MutableLiveData<>();
  private ActionLiveEvent loadGymTypes = new ActionLiveEvent();
  public MutableLiveData<Boolean> quiteResult = new MutableLiveData<>();
  @Inject IGymModel gymModel;
  public SingleLiveEvent<Shop> shopSingleLiveEvent = new SingleLiveEvent<>();

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


  public LiveData<Boolean> editShop(Shop shop) {
    return Transformations.map(gymResponsitory.editGymIntro(shop.id, shop),
        new Function<Resource<Boolean>, Boolean>() {
          @Override public Boolean apply(Resource<Boolean> booleanResource) {
            return dealResource(booleanResource);
          }
        });
  }

  public void quiteGym(Map<String,Object> params) {
    gymModel.qcQuitGym(params).compose(RxHelper.schedulersTransformer()).subscribe(response -> {
      if (ResponseConstant.checkSuccess(response)) {
        quiteResult.setValue(true);
      } else {
        quiteResult.setValue(false);
      }
    }, throwable -> quiteResult.setValue(false));
  }
  public void createShop(ShopCreateBody shop,boolean istrainer) {
    gymModel.qcSystemInit(shop,istrainer).compose(RxHelper.schedulersTransformer()).subscribe(response -> {
      if (ResponseConstant.checkSuccess(response)) {
        shopSingleLiveEvent.setValue(response.data);
      } else {
        shopSingleLiveEvent.setValue(null);
      }
    }, throwable -> shopSingleLiveEvent.setValue(null));
  }
}
