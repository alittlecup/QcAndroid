package cn.qingcheng.gym.responsitory;

import android.arch.lifecycle.LiveData;
import cn.qingcheng.gym.bean.BrandsResponse;
import cn.qingcheng.gym.bean.ShopsResponse;
import cn.qingcheng.gym.responsitory.network.IGymModel;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.mvvm.LiveDataTransfer;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Flowable;
import javax.inject.Inject;
import rx.Observable;

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
}
