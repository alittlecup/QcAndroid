package cn.qingchengfit.card.network;

import android.arch.lifecycle.LiveData;
import cn.qingchengfit.card.bean.CouponResponse;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.mvvm.LiveDataTransfer;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Flowable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;

public class CardRespositoryImpl implements CardRespository {
  @Inject CardRealModel cardModel;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  @Inject public CardRespositoryImpl() {
  }

  static <T> LiveData<Resource<T>> toLiveData(Flowable<QcDataResponse<T>> observable) {
    return LiveDataTransfer.fromPublisher(observable.compose(RxHelper.schedulersTransformerFlow()));
  }

  static <T> LiveData<Resource<T>> toLiveData(Observable<QcDataResponse<T>> observable) {
    return toLiveData(RxJavaInterop.toV2Flowable(observable));
  }

  @Override public LiveData<Resource<CouponResponse>> loadCoupons(Map<String, Object> bodys) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.putAll(bodys);
    return toLiveData(cardModel.qcLoadCoupons(loginStatus.staff_id(), params));
  }
}
