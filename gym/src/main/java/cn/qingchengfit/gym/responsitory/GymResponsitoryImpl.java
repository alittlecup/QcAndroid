package cn.qingchengfit.gym.responsitory;

import android.arch.lifecycle.LiveData;
import cn.qingchengfit.gym.bean.BrandPostBody;
import cn.qingchengfit.gym.bean.BrandResponse;
import cn.qingchengfit.gym.bean.BrandsResponse;
import cn.qingchengfit.gym.bean.BransShopsPremissions;
import cn.qingchengfit.gym.bean.GymApplyOrderResponse;
import cn.qingchengfit.gym.bean.GymApplyOrderResponses;
import cn.qingchengfit.gym.bean.GymPosition;
import cn.qingchengfit.gym.bean.GymPositions;
import cn.qingchengfit.gym.bean.GymSearchResponse;
import cn.qingchengfit.gym.bean.GymTypeData;
import cn.qingchengfit.gym.bean.ShopCreateBody;
import cn.qingchengfit.gym.bean.ShopsResponse;
import cn.qingchengfit.gym.responsitory.network.IGymModel;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.mvvm.LiveDataTransfer;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Flowable;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;

public class GymResponsitoryImpl implements IGymResponsitory {
  @Inject IGymModel gymModel;

  @Inject GymResponsitoryImpl() {

  }

  static <T> LiveData<Resource<T>> toLiveData(Flowable<QcDataResponse<T>> observable) {
    return LiveDataTransfer.fromPublisher(observable.compose(RxHelper.schedulersTransformerFlow()));
  }

  static <T> LiveData<Resource<T>> toLiveData(Observable<QcDataResponse<T>> observable) {
    return toLiveData(RxJavaInterop.toV2Flowable(observable));
  }

  @Override public LiveData<Resource<BrandsResponse>> qcGetBrands() {
    return toLiveData(gymModel.qcGetBrands());
  }

  @Override public LiveData<Resource<ShopsResponse>> qcGetBrandAllShops(String brand_id) {
    return toLiveData(gymModel.qcGetBrandAllShops(brand_id));
  }

  @Override public LiveData<Resource<GymTypeData>> qcGetGymTypes() {
    return toLiveData(gymModel.qcLoadGymTypes());
  }

  @Override public LiveData<Resource<Boolean>> qcDeleteShop(String shop_id) {
    return toLiveData(gymModel.qcDelGym(shop_id)
        .map((Func1<QcDataResponse, QcDataResponse<Boolean>>) qcResponse -> {
          qcResponse.setData(qcResponse.status == 200);
          return qcResponse;
        }));
  }

  @Override public LiveData<Resource<BrandResponse>> qcCreateBrand(BrandPostBody body) {
    return toLiveData(gymModel.qcCreatBrand(body));
  }

  @Override public LiveData<Resource<Shop>> qcSystemInit(ShopCreateBody body,boolean istrainer) {
    return toLiveData(gymModel.qcSystemInit(body,istrainer));
  }

  @Override public LiveData<Resource<Boolean>> editGymIntro(String gymId, Shop body) {
    return toLiveData(gymModel.editGymIntro(gymId, body)
        .map((Func1<QcDataResponse, QcDataResponse<Boolean>>) qcResponse -> {
          qcResponse.setData(qcResponse.status == 200);
          return qcResponse;
        }));
  }

  @Override public LiveData<Resource<GymSearchResponse>> qcGetGymsByName(String name) {
    return toLiveData(gymModel.qcGetGymsByName(name));
  }

    @Override public LiveData<Resource<GymPositions>> qcGetGymPositions(String id) {
    return toLiveData(gymModel.qcGetGymPositions(id));
  }

  @Override
  public LiveData<Resource<GymApplyOrderResponse>> qcPostGymApply(Map<String, Object> body) {
    return toLiveData(gymModel.qcPostGymApply(body));
  }

  @Override
  public LiveData<Resource<GymApplyOrderResponses>> qcGetGymApplyOrder(Map<String, Object> body) {
    return toLiveData(gymModel.qcGetGymApplyOrder(body));
  }

  @Override public LiveData<Resource<GymPosition>> qcGetGymUserPosition(String id, String type) {
    return toLiveData(gymModel.qcGetGymUserPosition(id, type));
  }

  @Override public LiveData<Resource<GymApplyOrderResponse>> qcGetGymApplyOrderInfo(String gymId,
      String orderId) {
    return toLiveData(gymModel.qcGetGymApplyOrderInfo(gymId, orderId));
  }

  @Override
  public LiveData<Resource<GymApplyOrderResponse>> qcDealGymApplyOrder(String gymId, String orderId,
      Map<String, Object> body) {
    return toLiveData(gymModel.qcDealGymApplyOrder(gymId, orderId, body));
  }

  @Override
  public LiveData<Resource<BransShopsPremissions>> qcGetBrandShopsPermission(String brandID,
      String permission) {
    return toLiveData(gymModel.qcGetBrandShopsPermission(brandID,permission));
  }
}
