package cn.qingcheng.gym.responsitory;

import android.arch.lifecycle.LiveData;
import cn.qingcheng.gym.bean.BrandPostBody;
import cn.qingcheng.gym.bean.BrandResponse;
import cn.qingcheng.gym.bean.BrandsResponse;
import cn.qingcheng.gym.bean.GymApplyOrderResponse;
import cn.qingcheng.gym.bean.GymPositions;
import cn.qingcheng.gym.bean.GymSearchResponse;
import cn.qingcheng.gym.bean.GymTypeData;
import cn.qingcheng.gym.bean.ShopsResponse;
import cn.qingcheng.gym.responsitory.network.IGymModel;
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

  @Override public LiveData<Resource<Shop>> qcSystemInit(Shop body) {
    return toLiveData(gymModel.qcSystemInit(body));
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
  public LiveData<Resource<GymApplyOrderResponse>> qcGetGymApplyOrder(Map<String, Object> body) {
    return toLiveData(gymModel.qcGetGymApplyOrder(body));
  }
}
